package org.eclipse.lsp.cobol.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlowNode {
    private String text;
    protected List<FlowNode> incomingNodes;
    protected List<FlowNode> outgoingNodes;

    public FlowNode(String text) {
        this(text, new ArrayList<>(), new ArrayList<>());
    }

    public FlowNode(String text, List<FlowNode> incomingNodes, List<FlowNode> outgoingNodes) {
        this.text = text;
        this.incomingNodes = incomingNodes;
        this.outgoingNodes = outgoingNodes;
    }

    public void addIncomingNode(FlowNode node) {
        if (node == null) return;
        incomingNodes.add(node);
    }

    public void setOutgoingNode(FlowNode node) {
        outgoingNodes = new ArrayList<>();
        outgoingNodes.add(node);
    }

    public boolean canReturn() {
        return true;
    }

    public void connectTo(FlowNode successorNode) {
        if (!canReturn()) return;
        this.setOutgoingNode(successorNode);
        successorNode.addIncomingNode(this);
    }

    protected List<FlowNode> returningPaths() {
        List<FlowNode> terminalNodes = new ArrayList<FlowNode>();
        recurse(this, terminalNodes);
        return terminalNodes.stream().filter(FlowNode::canReturn).collect(Collectors.toList());
    }

    private static void recurse(FlowNode tail, List<FlowNode> terminalNodes) {
        List<FlowNode> children = tail.getChildren();
        if (children.isEmpty()) {
            terminalNodes.add(tail);
            return;
        }
        for (int i = 0; i <= children.size() - 1; i++) {
            recurse(children.get(i), terminalNodes);
        }
    }

    protected List<FlowNode> getChildren() {
//        if (outgoingNodes == null) return ImmutableList.of();
        return outgoingNodes;
    }
}
