package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class ParagraphsChartNode extends CompositeCobolNode {
    public ParagraphsChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.PARAGRAPHS;
    }
}
