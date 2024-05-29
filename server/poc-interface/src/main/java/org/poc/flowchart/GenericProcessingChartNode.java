package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeType;
import poc.common.flowchart.ChartNodeVisitor;
import poc.common.flowchart.DomainDocument;

import java.util.ArrayList;
import java.util.List;

public class GenericProcessingChartNode implements ChartNode {
    private List<ChartNode> outgoingNodes = new ArrayList<>();
    private final ChartNode enclosingScope;
    private final ChartNode node;

    public GenericProcessingChartNode(ChartNode node, ChartNode enclosingScope) {
        this.enclosingScope = enclosingScope;
        this.node = node;
    }

    @Override
    public void buildFlow() {

    }

    @Override
    public void buildOutgoingFlow() {

    }

    @Override
    public void buildInternalFlow() {

    }

    @Override
    public void goesTo(ChartNode successor) {
        outgoingNodes.add(successor);
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public ChartNodeType type() {
        return null;
    }

    @Override
    public void accept(ChartNodeVisitor visitor, int level) {

    }

    @Override
    public void accept(ChartNodeVisitor visitor, int level, int maxLevel) {

    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {

    }

    @Override
    public ParseTree getExecutionContext() {
        return null;
    }

    @Override
    public void addIncomingNode(ChartNode chartNode) {

    }

    @Override
    public DomainDocument getNotes() {
        return new DomainDocument();
    }

    @Override
    public void reset() {

    }

    @Override
    public void remove() {

    }

    @Override
    public void removeOutgoingNode(ChartNode chartNode) {

    }

    @Override
    public void removeIncomingNode(ChartNode chartNode) {

    }

    @Override
    public boolean accessesDatabase() {
        return false;
    }

    public void append(ChartNode vanillaNode) {

    }
}
