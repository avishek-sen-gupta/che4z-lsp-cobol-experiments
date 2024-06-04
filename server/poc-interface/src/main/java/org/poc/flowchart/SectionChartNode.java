package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class SectionChartNode extends CompositeCobolNode {
    @Override
    public ChartNodeType type() {
        return ChartNodeType.SECTION;
    }

    public SectionChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }
}
