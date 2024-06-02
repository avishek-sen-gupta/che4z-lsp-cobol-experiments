package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;

public class PerformTestChartNode extends CobolChartNode {
    public PerformTestChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.PERFORM_TEST;
    }

    @Override
    public String label() {
        return originalText();
    }
}
