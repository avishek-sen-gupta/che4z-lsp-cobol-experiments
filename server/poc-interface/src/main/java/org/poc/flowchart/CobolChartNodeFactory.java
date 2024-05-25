package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;

public class CobolChartNodeFactory {
    public static ChartNode newNode(ParseTree parseTree, ChartNodeService nodeService) {
        if (StatementIdentity.is(parseTree, CobolParser.IfStatementContext.class))
            return new IfChartNode(parseTree, nodeService);
        return new CobolChartNode(parseTree, nodeService);
    }
}
