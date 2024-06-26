package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class PerformTestChartNode extends CobolChartNode {
    public PerformTestChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
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
