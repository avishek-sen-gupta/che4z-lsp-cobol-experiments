package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;

public class IfElseChartNode extends CompositeCobolNode {
    public IfElseChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.IF_NO;
    }

    @Override
    public String name() {
        return "No";
    }
}
