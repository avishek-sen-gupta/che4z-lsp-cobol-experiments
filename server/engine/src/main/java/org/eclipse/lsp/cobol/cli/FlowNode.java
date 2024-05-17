package org.eclipse.lsp.cobol.cli;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlowNode {
    private String text;
    protected List<FlowNode> incomingNodes;
    protected FlowNode outgoingNode;

    public FlowNode(String text) {
        this(text, new ArrayList<>(), null);
    }

    public FlowNode(String text, List<FlowNode> incomingNodes, FlowNode outgoingNode) {
        this.text = text;
        this.incomingNodes = incomingNodes;
        this.outgoingNode = outgoingNode;
    }

    public void addIncomingNode(FlowNode node) {
        if (node == null) return;
        incomingNodes.add(node);
    }

    public void setOutgoingNode(FlowNode node) {
        outgoingNode = node;
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
        if (outgoingNode == null) return ImmutableList.of();
        return ImmutableList.of(outgoingNode);
    }
}
