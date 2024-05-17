package org.eclipse.lsp.cobol.cli;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class ConditionFlowNode extends FlowNode {
    @Getter private FlowNode trueOutgoingPathRoot;
    @Getter private FlowNode falseOutgoingPathRoot;

    public ConditionFlowNode(ParseTree conditionCtx) {
        super(conditionCtx.getText(), new ArrayList<>(), new DeadEndFlowNode());
        truePathRoot(new PlaceholderFlowNode());
        falsePathRoot(new PlaceholderFlowNode());
    }

    public void truePathRoot(FlowNode trueOutgoingPathRoot) {
        this.trueOutgoingPathRoot = trueOutgoingPathRoot;
        this.trueOutgoingPathRoot.addIncomingNode(this);
    }

    public void falsePathRoot(FlowNode falseOutgoingPathRoot) {
        this.falseOutgoingPathRoot = falseOutgoingPathRoot;
        this.falseOutgoingPathRoot.addIncomingNode(this);
    }

    @Override
    public boolean canReturn() {
        return trueOutgoingPathRoot.canReturn() || falseOutgoingPathRoot.canReturn();
    }

    @Override
    protected List<FlowNode> getChildren() {
        return ImmutableList.of(trueOutgoingPathRoot, falseOutgoingPathRoot);
    }

    @Override
    public void connectTo(FlowNode successorNode) {
        List<FlowNode> allReturningPaths = new ArrayList<>();
        allReturningPaths.addAll(trueOutgoingPathRoot.returningPaths());
        allReturningPaths.addAll(falseOutgoingPathRoot.returningPaths());
        allReturningPaths.forEach(p -> {
            p.setOutgoingNode(successorNode);
            successorNode.addIncomingNode(p);
        });
    }
}
