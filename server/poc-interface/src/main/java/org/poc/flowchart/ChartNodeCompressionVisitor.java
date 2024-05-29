package org.poc.flowchart;

import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class ChartNodeCompressionVisitor implements ChartNodeVisitor {
    private final ChartNode enclosingScope;
    private GenericProcessingChartNode head;
    private List<GenericProcessingChartNode> groups;
//    private final ChartNodeCompressionVisitor parentVisitor;
    private List<ChartNodeVisitor> childVisitors = new ArrayList<>();

    public ChartNodeCompressionVisitor(ChartNode enclosingScope) {
        this(enclosingScope, new ArrayList<>());
//        tail = new GenericProcessingChartNode(null, enclosingScope);
//        head = tail;
    }

    public ChartNodeCompressionVisitor(ChartNode enclosingScope, List<GenericProcessingChartNode> groups) {
        this.enclosingScope = enclosingScope;
        this.groups = groups;
    }

    @Override
    public void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, ChartNodeService nodeService) {
        if (node.getClass() == SentenceChartNode.class && node.isMergeable()) {
            System.out.println("MERGEABLE : " + node);
            if (head == null) {
                head = new GenericProcessingChartNode(node, enclosingScope);
            } else {
                head.add(node);
            }
        } else {
            if (head != null) {
                groups.add(head);
                head = null;
            }
        }
    }

    @Override
    public void visitCluster(ChartNode node, ChartNodeService nodeService) {

    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService) {
    }

    @Override
    public void visitControlTransfer(ChartNode from, ChartNode to) {

    }

    @Override
    public ChartNodeVisitor newScope(ChartNode enclosingScope) {
        return new ChartNodeCompressionVisitor(enclosingScope, groups);
//        return this;
//        if (enclosingScope.getExecutionContext().getClass() == CobolParser.SentenceContext.class ||
//                enclosingScope.getExecutionContext().getClass() == CobolParser.ParagraphsContext.class) return this;
//        ChartNodeCompressionVisitor childVisitor = new ChartNodeCompressionVisitor(enclosingScope, this);
//        childVisitors.add(childVisitor);
//        return childVisitor;
    }

    public void report() {
        groups.forEach(System.out::println);
    }
}
