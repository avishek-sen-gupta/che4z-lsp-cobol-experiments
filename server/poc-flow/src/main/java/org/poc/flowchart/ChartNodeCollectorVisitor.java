package org.poc.flowchart;

import guru.nidi.graphviz.model.MutableNode;
import lombok.Getter;
import poc.common.flowchart.*;

import java.util.ArrayList;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class ChartNodeCollectorVisitor implements ChartNodeVisitor {

    @Getter private final List<ChartNode> chartNodes;
    private final ChartOverlay overlay;

    public ChartNodeCollectorVisitor(ChartOverlay overlay) {
        this.overlay = overlay;
        chartNodes = new ArrayList<>();
    }

    public void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, VisitContext visitContext, ChartNodeService nodeService) {
        ChartNode block = overlay.block(node.passthrough());
        if (chartNodes.contains(block)) return;
        chartNodes.add(block);
    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, VisitContext visitContext, ChartNodeService nodeService) {
    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, VisitContext visitContext, ChartNodeService nodeService, ChartNodeCondition hideStrategy) {
    }

    @Override
    public void visitControlTransfer(ChartNode from, ChartNode to, VisitContext visitContext) {
    }

    @Override
    public ChartNodeVisitor newScope(ChartNode enclosingScope) {
        return this;
    }

    @Override
    public void group(ChartNode root) {

    }

    private MutableNode styled(ChartNode chartNode, MutableNode node) {
        return FlowchartStylePreferences.scheme(chartNode).apply(node);
    }
}
