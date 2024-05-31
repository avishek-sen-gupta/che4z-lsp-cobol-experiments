package org.poc.flowchart;

import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.ArrayList;
import java.util.List;

public class ChartNodeOverlayVisitor implements ChartNodeVisitor {
    private final ChartNode enclosingScope;
    private GenericProcessingChartNode head;
    private List<GenericProcessingChartNode> groups;

    public ChartNodeOverlayVisitor(ChartNode enclosingScope) {
        this(enclosingScope, new ArrayList<>());
    }

    public ChartNodeOverlayVisitor(ChartNode enclosingScope, List<GenericProcessingChartNode> groups) {
        this.enclosingScope = enclosingScope;
        this.groups = groups;
    }

    @Override
    public void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, ChartNodeService nodeService) {
        if ((node.getClass() == SentenceChartNode.class ||
                node.getClass() == ConditionalStatementChartNode.class ||
                node.getClass() == GenericStatementChartNode.class // This condition is a little sus because technically, statements inside sentences could also get their own groups which would show up in addition to their parent sentence groups. It's working now, need to investigate with a small test program.
        ) && node.isMergeable()) {
            System.out.println("MERGEABLE : " + node);
            if (head == null) {
                head = new GenericProcessingChartNode(node, enclosingScope);
                groups.add(head);
            } else {
                head.add(node);
            }
        } else {
            if (head != null) {
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
        return new ChartNodeOverlayVisitor(enclosingScope, groups);
    }

    public void report() {
        groups.forEach(System.out::println);
    }

    public ChartOverlay overlay() {
        return new ChartOverlay(groups);
    }
}