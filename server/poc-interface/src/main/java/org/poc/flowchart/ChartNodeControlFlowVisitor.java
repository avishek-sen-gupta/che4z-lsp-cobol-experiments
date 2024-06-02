package org.poc.flowchart;

import poc.common.flowchart.*;

import java.util.List;

public class ChartNodeControlFlowVisitor implements ChartNodeVisitor {
    @Override
    public void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, VisitContext context, ChartNodeService nodeService) {
        node.buildControlFlow();
    }

    @Override
    public boolean shouldVisit(VisitContext context) {
        return true;
    }

    @Override
    public void visitCluster(ChartNode node, ChartNodeService nodeService) {

    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, VisitContext ctx, ChartNodeService nodeService) {
    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, VisitContext ctx, ChartNodeService nodeService, ChartNodeCondition hideStrategy) {
    }

    @Override
    public void visitControlTransfer(ChartNode from, ChartNode to, VisitContext visitContext) {

    }

    @Override
    public ChartNodeVisitor newScope(ChartNode enclosingScope) {
        return new ChartNodeControlFlowVisitor();
    }
}
