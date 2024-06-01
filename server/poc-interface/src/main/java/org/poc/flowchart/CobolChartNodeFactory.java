package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.StatementIdentity;

public class CobolChartNodeFactory {
    public static ChartNode newNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        if (StatementIdentity.isStatementOfType(parseTree, CobolParser.IfStatementContext.class))
            return new IfChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isStatementOfType(parseTree, CobolParser.GoToStatementContext.class))
            return new GoToChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isStatementOfType(parseTree, CobolParser.NextSentenceContext.class))
            return new NextSentenceChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isStatementOfType(parseTree, CobolParser.PerformStatementContext.class))
            return new PerformChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.DialectStatementContext.class))
            return new DialectStatementChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.ConditionalStatementCallContext.class))
            return new ConditionalStatementChartNode(parseTree, scope, nodeService);
        // This needs to come last in all the statement classifications, or things will break
        else if (StatementIdentity.isOfType(parseTree, CobolParser.StatementContext.class))
            return new GenericStatementChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, TerminalNodeImpl.class))
            return new SymbolChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.ProcedureSectionHeaderContext.class))
            return new SectionHeaderChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.ParagraphDefinitionNameContext.class))
            return new ParagraphNameChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.SentenceContext.class))
            return new SentenceChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.IfThenContext.class))
            return new IfThenChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.IfElseContext.class))
            return new IfElseChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.ParagraphsContext.class))
            return new ParagraphsChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.ParagraphContext.class))
            return new ParagraphChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.ProcedureSectionContext.class))
            return new SectionChartNode(parseTree, scope, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.ProcedureDivisionBodyContext.class))
            return new ProcedureDivisionBodyChartNode(parseTree, scope, nodeService);
        else if (isCompositeNode(parseTree))
            return new CompositeCobolNode(parseTree, scope, nodeService);

        return new CobolChartNode(parseTree, scope, nodeService);
    }

    private static boolean isCompositeNode(ParseTree executionContext) {
        return
//                  executionContext.getClass() == CobolParser.ProcedureSectionContext.class ||
//                executionContext.getClass() == CobolParser.ParagraphsContext.class ||
//                executionContext.getClass() == CobolParser.ParagraphContext.class ||
//                executionContext.getClass() == CobolParser.SentenceContext.class ||
//                executionContext.getClass() == CobolParser.ProcedureDivisionBodyContext.class ||
//                executionContext.getClass() == CobolParser.IfThenContext.class ||
//                executionContext.getClass() == CobolParser.IfElseContext.class ||
//                executionContext.getClass() == CobolParser.ConditionalStatementCallContext.class ||
                executionContext.getClass() == CobolParser.PerformInlineStatementContext.class ||
                executionContext.getClass() == CobolParser.DialectSectionContext.class ||
                executionContext.getClass() == IdmsParser.IdmsIfStatementContext.class ||
                executionContext.getClass() == IdmsContainerNode.class ||
                executionContext.getClass() == IdmsParser.InquireMapIfStatementContext.class
                ;
    }
}
