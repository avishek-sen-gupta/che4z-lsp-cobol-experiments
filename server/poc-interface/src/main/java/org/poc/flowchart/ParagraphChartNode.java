package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeCondition;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;

public class ParagraphChartNode extends CompositeCobolNode {
    @Override
    public ChartNodeType type() {
        return ChartNodeType.PARAGRAPH;
    }

    public ParagraphChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    // TODO: This wont work for stuff which goes into subsequent paragraphs, they will just skip over, fix this ASAP
    @Override
    public ChartNode next(ChartNodeCondition nodeCondition, ChartNode startingNode) {
        System.out.println("Moved up to " + executionContext.getClass());
        if (outgoingNodes.isEmpty()) {
            return scope != null ? scope.next(nodeCondition, startingNode) : new DummyChartNode(nodeService);
        }
        return outgoingNodes.getFirst().next(nodeCondition, startingNode);
    }
}
