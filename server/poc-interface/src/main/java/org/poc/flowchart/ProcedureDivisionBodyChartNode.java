package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeCondition;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;

public class ProcedureDivisionBodyChartNode extends CompositeCobolNode {
    @Override
    public ChartNodeType type() {
        return ChartNodeType.SECTION;
    }

    public ProcedureDivisionBodyChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public ChartNode next(ChartNodeCondition nodeCondition, ChartNode startingNode) {
        return new DummyChartNode(nodeService);
    }
}
