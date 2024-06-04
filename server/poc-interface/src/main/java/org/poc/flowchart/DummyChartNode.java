package org.poc.flowchart;

import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;
import poc.common.flowchart.ChartNodeVisitor;
import poc.common.flowchart.StackFrames;

public class DummyChartNode extends CobolChartNode {
    public DummyChartNode(ChartNodeService nodeService, StackFrames stackFrames) {
        super(null, null, nodeService, stackFrames);
    }

    @Override
    public void buildFlow() {
        super.buildFlow();
    }

    @Override
    public String toString() {
        return "DUMMY_NODE";
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {
//        super.acceptUnvisited(visitor, level);
    }

    @Override
    public String name() {
        return "DUMMY_EXECUTION_CONTEXT";
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.DUMMY;
    }
}
