package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class ParagraphNameChartNode extends CobolChartNode {
    public ParagraphNameChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.PARAGRAPH_NAME;
    }

    @Override
    public ChartNode passthrough() {
        System.out.println(String.format("%s has %s outgoing nodes", executionContext.getText(), outgoingNodes.size()));
        // In case of an empty paragraph, there is nothing to terminate with, so we return this
        // TODO: But check why IDMS copy book MAP-BINDS is missing
        if (outgoingNodes.isEmpty()) return this;
        return outgoingNodes.getFirst();
//        return this;
    }

    @Override
    public boolean isPassthrough() {
        return true;
    }
}
