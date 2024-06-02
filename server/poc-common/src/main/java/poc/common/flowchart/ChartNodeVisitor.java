package poc.common.flowchart;


import java.util.List;

public interface ChartNodeVisitor {
    void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, VisitContext context, ChartNodeService nodeService);
    void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, VisitContext ctx, ChartNodeService nodeService);
    void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, VisitContext ctx, ChartNodeService nodeService, ChartNodeCondition hideStrategy);
    void visitControlTransfer(ChartNode from, ChartNode to, VisitContext visitContext);
    ChartNodeVisitor newScope(ChartNode enclosingScope);
    void group(ChartNode root, ChartNode headConnection, ChartNode tailConnection);
}
