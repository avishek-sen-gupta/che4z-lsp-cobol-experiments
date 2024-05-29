package org.poc.flowchart;

import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class ChartNodeCompressionVisitor implements ChartNodeVisitor {
    private final ChartNode enclosingScope;
    private ChartNode tail;
    private ChartNode head;
    private final ChartNodeCompressionVisitor parentVisitor;
    private List<ChartNodeVisitor> childVisitors = new ArrayList<>();

    public ChartNodeCompressionVisitor(ChartNode enclosingScope, ChartNodeCompressionVisitor parentVisitor) {
        this.enclosingScope = enclosingScope;
//        tail = new GenericProcessingChartNode(null, enclosingScope);
//        head = tail;
        this.parentVisitor = parentVisitor;
    }

    @Override
    public void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, ChartNodeService nodeService) {
        if (node.isMergeable()) {
            System.out.println("MERGEABLE : " + node);
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
        return this;
//        if (enclosingScope.getExecutionContext().getClass() == CobolParser.SentenceContext.class ||
//                enclosingScope.getExecutionContext().getClass() == CobolParser.ParagraphsContext.class) return this;
//        ChartNodeCompressionVisitor childVisitor = new ChartNodeCompressionVisitor(enclosingScope, this);
//        childVisitors.add(childVisitor);
//        return childVisitor;
    }
}
