package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;

public class SymbolChartNode extends CobolChartNode {
    public SymbolChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.SYMBOL;
    }

    @Override
    public boolean isPassthrough() {
        return true;
    }

    @Override
    public ChartNode passthrough() {
        return outgoingNodes.getFirst();
    }
}
