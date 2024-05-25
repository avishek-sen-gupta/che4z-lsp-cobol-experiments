package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.eclipse.lsp.cobol.core.CobolParser;
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
        else if (StatementIdentity.isOfType(parseTree, IdmsContainerNode.class))
            return new IdmsContainerChartNode(parseTree, nodeService);

        return new CobolChartNode(parseTree, nodeService);
    }
}
