package org.flowchart;

public interface ChartNodeVisitor {
    void visit(ChartNode node, ChartNodeService nodeService);
    void visitCluster(ChartNode node, ChartNodeService nodeService);
    void visitSpecific(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService);
    void visitControlTransfer(ChartNode from, ChartNode to);
}
