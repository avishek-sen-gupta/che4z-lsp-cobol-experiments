package org.eclipse.lsp.cobol.cli;

import java.util.ArrayList;
import java.util.List;

public class FlowNode {
    protected List<FlowNode> incomingNodes;
    protected List<FlowNode> outgoingNodes;

    public FlowNode() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public FlowNode(List<FlowNode> incomingNodes, List<FlowNode> outgoingNodes) {
        this.incomingNodes = incomingNodes;
        this.outgoingNodes = outgoingNodes;
    }

    public void addIncomingNode(FlowNode node) {
        if (node == null) return;
        incomingNodes.add(node);
    }

    public void addOutgoingNode(FlowNode node) {
        if (node == null) return;
        outgoingNodes.add(node);
    }

    public boolean canReturn() {
        return true;
    }
}
