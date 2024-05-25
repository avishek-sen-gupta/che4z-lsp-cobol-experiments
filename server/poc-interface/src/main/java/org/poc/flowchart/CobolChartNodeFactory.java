package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;

public class CobolChartNodeFactory {
    public static ChartNode newNode(ParseTree parseTree, ChartNodeService nodeService) {
        if (StatementIdentity.isStatementOfType(parseTree, CobolParser.IfStatementContext.class))
            return new IfChartNode(parseTree, nodeService);
        else if (StatementIdentity.isStatementOfType(parseTree, CobolParser.GoToStatementContext.class))
            return new GoToChartNode(parseTree, nodeService);
        else if (StatementIdentity.isStatementOfType(parseTree, CobolParser.PerformStatementContext.class))
            return new PerformChartNode(parseTree, nodeService);
        else if (StatementIdentity.isOfType(parseTree, CobolParser.DialectStatementContext.class))
            return new DialectStatementChartNode(parseTree, nodeService);
        else if (isCompositeNode(parseTree))
            return new CompositeCobolNode(parseTree, nodeService);

        return new CobolChartNode(parseTree, nodeService);
    }

    private static boolean isCompositeNode(ParseTree executionContext) {
        return executionContext.getClass() == CobolParser.ProcedureSectionContext.class ||
                executionContext.getClass() == CobolParser.ParagraphsContext.class ||
                executionContext.getClass() == CobolParser.ParagraphContext.class ||
                executionContext.getClass() == CobolParser.SentenceContext.class ||
                executionContext.getClass() == CobolParser.ProcedureDivisionBodyContext.class ||
                executionContext.getClass() == CobolParser.IfThenContext.class ||
                executionContext.getClass() == CobolParser.IfElseContext.class ||
                executionContext.getClass() == CobolParser.ConditionalStatementCallContext.class ||
                executionContext.getClass() == CobolParser.DialectSectionContext.class ||
                executionContext.getClass() == IdmsParser.IdmsIfStatementContext.class ||
                executionContext.getClass() == IdmsContainerNode.class ||
                executionContext.getClass() == IdmsParser.InquireMapIfStatementContext.class
//                executionContext.getClass() == IdmsParser.IdmsStatementsContext.class
                ;
    }
}
