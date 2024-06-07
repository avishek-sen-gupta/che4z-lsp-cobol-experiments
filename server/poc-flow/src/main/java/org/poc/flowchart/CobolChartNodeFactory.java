package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import poc.common.flowchart.*;

public class CobolChartNodeFactory {
    public static ChartNode newNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        if (SyntaxIdentity.isStatementOfType(parseTree, CobolParser.IfStatementContext.class))
            return new IfChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isStatementOfType(parseTree, CobolParser.GoToStatementContext.class))
            return new GoToChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isStatementOfType(parseTree, CobolParser.ExitStatementContext.class))
            return new ExitChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isStatementOfType(parseTree, CobolParser.NextSentenceContext.class) ||
                SyntaxIdentity.isOfType(parseTree, CobolParser.NextSentenceWrapperStatementContext.class))
            return new NextSentenceChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.satisfies(parseTree, SyntaxIdentity::PERFORM_PROCEDURE))
            return new PerformProcedureChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.satisfies(parseTree, SyntaxIdentity::PERFORM_INLINE))
            return new PerformInlineChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isStatementOfType(parseTree, CobolParser.SearchStatementContext.class))
            return new SearchChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isStatementOfType(parseTree, CobolParser.GenericOnClauseStatementContext.class))
            return new GenericOnClauseChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isStatementOfType(parseTree, CobolParser.DisplayStatementContext.class))
            return new DisplayChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.DialectStatementContext.class))
            return new DialectStatementChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.ConditionalStatementCallContext.class))
            return new ConditionalStatementChartNode(parseTree, scope, nodeService, stackFrames);
        // This needs to come last in all the statement classifications, or things will break
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.StatementContext.class))
            return new GenericStatementChartNode(parseTree, scope, nodeService, stackFrames);

        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.PerformTypeContext.class))
            return new PerformTestChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, TerminalNodeImpl.class))
            return new SymbolChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.ProcedureSectionHeaderContext.class))
            return new SectionHeaderChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.ParagraphDefinitionNameContext.class))
            return new ParagraphNameChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.SentenceContext.class))
            return new SentenceChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.SearchWhenContext.class))
            return new SearchWhenChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.AtEndPhraseContext.class))
            return new AtEndPhraseChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.IfThenContext.class))
            return new IfThenChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.IfElseContext.class))
            return new IfElseChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.ParagraphsContext.class))
            return new ParagraphsChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.ParagraphContext.class))
            return new ParagraphChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.ProcedureSectionContext.class))
            return new SectionChartNode(parseTree, scope, nodeService, stackFrames);
        else if (SyntaxIdentity.isOfType(parseTree, CobolParser.ProcedureDivisionBodyContext.class))
            return new ProcedureDivisionBodyChartNode(parseTree, scope, nodeService, stackFrames);
        else if (isCompositeNode(parseTree))
            return new CompositeCobolNode(parseTree, scope, nodeService, stackFrames);

        return new CobolChartNode(parseTree, scope, nodeService, stackFrames);
    }

    private static boolean isCompositeNode(ParseTree executionContext) {
        return
                executionContext.getClass() == CobolParser.SearchStatementContext.class ||
                executionContext.getClass() == CobolParser.PerformInlineStatementContext.class ||
                executionContext.getClass() == CobolParser.DialectSectionContext.class ||
                executionContext.getClass() == IdmsParser.IdmsIfStatementContext.class ||
                executionContext.getClass() == IdmsContainerNode.class ||
                executionContext.getClass() == IdmsParser.InquireMapIfStatementContext.class
                ;
    }
}
