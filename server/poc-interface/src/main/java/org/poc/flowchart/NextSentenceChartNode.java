package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class NextSentenceChartNode extends CobolChartNode {

    private ChartNode destinationSentenceNode;

    public NextSentenceChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public void buildOutgoingFlow() {
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.acceptUnvisited(visitor, level, maxLevel);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.NEXT_SENTENCE;
    }
}
