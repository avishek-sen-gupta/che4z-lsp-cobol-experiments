package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class ParagraphNameChartNode extends CobolChartNode {
    public ParagraphNameChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.PARAGRAPH_NAME;
    }

    @Override
    public ChartNode passthrough() {
        return outgoingNodes.getFirst();
//        return this;
    }

    @Override
    public boolean isPassthrough() {
        return true;
    }
}
