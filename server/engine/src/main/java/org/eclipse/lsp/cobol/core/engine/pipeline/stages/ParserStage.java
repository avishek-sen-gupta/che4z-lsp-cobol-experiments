/*
 * Copyright (c) 2023 Broadcom.
 * The term "Broadcom" refers to Broadcom Inc. and/or its subsidiaries.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Broadcom, Inc. - initial API and implementation
 *
 */
package org.eclipse.lsp.cobol.core.engine.pipeline.stages;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.lsp.cobol.common.dialects.DialectOutcome;
import org.eclipse.lsp.cobol.common.error.ErrorSeverity;
import org.eclipse.lsp.cobol.common.error.ErrorSource;
import org.eclipse.lsp.cobol.common.error.SyntaxError;
import org.eclipse.lsp.cobol.common.mapping.OriginalLocation;
import org.eclipse.lsp.cobol.common.message.MessageService;
import org.eclipse.lsp.cobol.common.model.tree.Node;
import org.eclipse.lsp.cobol.core.*;
import org.eclipse.lsp.cobol.core.engine.analysis.AnalysisContext;
import org.eclipse.lsp.cobol.core.engine.pipeline.Stage;
import org.eclipse.lsp.cobol.core.engine.pipeline.StageResult;
import org.eclipse.lsp.cobol.core.strategy.CobolErrorStrategy;
import org.eclipse.lsp.cobol.core.visitor.ParserListener;
import org.eclipse.lsp.cobol.visualisation.CobolTreeVisualiser;
import org.eclipse.lsp4j.Location;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser stage
 */
@RequiredArgsConstructor
public class ParserStage implements Stage<ParserStageResult, DialectOutcome> {
    private final MessageService messageService;
    private final ParseTreeListener treeListener;

    @Override
    public StageResult<ParserStageResult> run(AnalysisContext context, StageResult<DialectOutcome> prevStageResult) {
        return context.getBenchmarkSession().measure("pipeline.parser", () -> {
            // Run parser;
            context.setDialectNodes(ImmutableList.<Node>builder()
                    .addAll(context.getDialectNodes())
                    .addAll(prevStageResult.getData().getDialectNodes())
                    .build());
            ParserListener listener = new ParserListener(context.getExtendedDocument(), context.getCopybooksRepository());
            CobolErrorStrategy errorStrategy = new CobolErrorStrategy(messageService);
            AstBuilder parser = ParserUtils.isHwParserEnabled()
                    ? new SplitParser(CharStreams.fromString(context.getExtendedDocument().toString()),
                    listener, errorStrategy, treeListener)
                    : new AntlrCobolParser(CharStreams.fromString(context.getExtendedDocument().toString()),
                    listener, errorStrategy, treeListener);
            CobolParser.StartRuleContext tree = parser.runParser();
            ParseTreeWalker x = new ParseTreeWalker();
            x.walk(new CustomCobolParseTreeListener(), tree);
            new CobolTreeVisualiser().visualiseCobolAST(tree, context.getCobolParseTreeOutputPath());

            context.getAccumulatedErrors().addAll(listener.getErrors());
            context.getAccumulatedErrors().addAll(getParsingError(context, parser));
            return new StageResult<>(new ParserStageResult(parser.getTokens(), tree));
        });
    }

    private List<SyntaxError> getParsingError(AnalysisContext context, AstBuilder parser) {
        return parser.diagnostics().stream().map(diagnostic -> {
            Location location = context.getExtendedDocument().mapLocation(diagnostic.getRange());
            String copybookId = context.getCopybooksRepository().getCopybookIdByUri(location.getUri());
            return SyntaxError.syntaxError()
                    .errorSource(ErrorSource.PARSING)
                    .severity(ErrorSeverity.ERROR)
                    .location(new OriginalLocation(location, copybookId))
                    .suggestion(diagnostic.getMessage())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return "Parsing stage";
    }
}

/**
 * Parser Listener
 */
class CustomCobolParseTreeListener extends CobolParserBaseListener {
    @Override
    public void enterIfThen(CobolParser.IfThenContext ctx) {
        super.enterIfThen(ctx);
    }

    @Override
    public void enterCompilationUnit(CobolParser.CompilationUnitContext ctx) {
        super.enterCompilationUnit(ctx);
        System.out.println("ENTERED A COMPILATION UNIT");
    }

    @Override
    public void enterProcedureDivision(CobolParser.ProcedureDivisionContext ctx) {
        super.enterProcedureDivision(ctx);
        System.out.println("ENTERED A PROCEDURE DIVISION");
    }
    @Override
    public void enterIfStatement(CobolParser.IfStatementContext ctx) {
        System.out.println("ENTERED AN IF STATEMENT");
    }

    @Override public void enterParagraph(CobolParser.ParagraphContext ctx) {
        System.out.println(ctx.paragraphDefinitionName());
    }
}
