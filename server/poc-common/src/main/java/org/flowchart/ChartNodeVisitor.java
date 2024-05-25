package org.flowchart;

import java.util.List;

public interface ChartNodeVisitor {
    void visit(ChartNode node, List<ChartNode> outgoingNodes, ChartNodeService nodeService);
    void visitCluster(ChartNode node, ChartNodeService nodeService);
    void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService);
    void visitControlTransfer(ChartNode from, ChartNode to);
}
