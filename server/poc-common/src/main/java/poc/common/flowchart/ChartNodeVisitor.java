package poc.common.flowchart;

import java.util.List;

public interface ChartNodeVisitor {
    void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, ChartNodeService nodeService);
    void visitCluster(ChartNode node, ChartNodeService nodeService);
    void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService);
    void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService, ChartNodeCondition hideStrategy);
    void visitControlTransfer(ChartNode from, ChartNode to);
    ChartNodeVisitor newScope(ChartNode enclosingScope);
}
