package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;

public class IfThenChartNode extends CompositeCobolNode {
    public IfThenChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.IF_YES;
    }

    @Override
    public String name() {
        return "Yes";
    }
}
