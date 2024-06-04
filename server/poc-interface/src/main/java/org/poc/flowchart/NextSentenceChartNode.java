package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class NextSentenceChartNode extends CobolChartNode {
    private ChartNode destinationSentenceNode;

    public NextSentenceChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public void buildControlFlow() {
        // scope is the actual SentenceChartNode
        ChartNodeCondition isSentence = n -> n.getClass() == SentenceChartNode.class;
        ChartNode containingSentence = scope.findUpwards(isSentence, null);
        destinationSentenceNode = containingSentence.next(isSentence, containingSentence, true);
        System.out.println("Next sentence is " + destinationSentenceNode);
    }

    @Override
    public void buildOutgoingFlow() {
        // Don't call super here, because flow never returns here
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {
        super.acceptUnvisited(visitor, level);
        visitor.visitControlTransfer(this, destinationSentenceNode, new VisitContext(level));
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.NEXT_SENTENCE;
    }

    @Override
    public String label() {
        return "Next Sentence";
    }
}
