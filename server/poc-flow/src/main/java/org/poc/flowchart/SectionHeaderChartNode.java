package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class SectionHeaderChartNode extends CobolChartNode {
    public SectionHeaderChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.SECTION_HEADER;
    }

    @Override
    public ChartNode passthrough() {
        return outgoingNodes.getFirst();
    }

    @Override
    public boolean isPassthrough() {
        return true;
    }
}
