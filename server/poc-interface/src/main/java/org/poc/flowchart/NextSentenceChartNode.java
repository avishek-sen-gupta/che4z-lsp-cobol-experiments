package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.*;

public class NextSentenceChartNode extends CobolChartNode {

    private ChartNode destinationSentenceNode;

    public NextSentenceChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public void buildControlFlow() {
        // scope is the actual SentenceChartNode
        ChartNodeCondition isSentence = n -> n.getClass() == SentenceChartNode.class;
        ChartNode containingSentence = scope.find(isSentence);
        destinationSentenceNode = containingSentence.next(isSentence, containingSentence);
        System.out.println("Next sentence is " + destinationSentenceNode);
    }

    @Override
    public void buildOutgoingFlow() {
        // Don't call super here, because flow never returns here
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.acceptUnvisited(visitor, level, maxLevel);
        visitor.visitControlTransfer(this, destinationSentenceNode);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.NEXT_SENTENCE;
    }
}
