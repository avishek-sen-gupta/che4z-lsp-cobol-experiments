package poc.common.flowchart;

import java.util.ArrayList;
import java.util.List;

public class ChartNodeRuleVisitor implements ChartNodeVisitor {
    private final ChartNode enclosingScope;
    private ChartNodeTransformRules rules;
    private List<NodeTransformRuleApplication> ops = new ArrayList<>();

    public ChartNodeRuleVisitor(ChartNode enclosingScope, ChartNodeTransformRules rules) {
        this.enclosingScope = enclosingScope;
        this.rules = rules;
    }

    @Override
    public void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, ChartNodeService nodeService) {
        ops.add( () -> rules.apply(node) );
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
    }

    public void applyRules() {
        ops.forEach(NodeTransformRuleApplication::apply);
    }
}
