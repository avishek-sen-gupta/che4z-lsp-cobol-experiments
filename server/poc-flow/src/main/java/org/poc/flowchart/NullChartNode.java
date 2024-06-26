package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

import java.util.List;

public class NullChartNode implements ChartNode {

    private final int uuid;

    public NullChartNode(ChartNodeService nodeService) {
        uuid = nodeService.counter();
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
    public void buildControlFlow() {

    }

    @Override
    public void goesTo(ChartNode successor) {

    }

    @Override
    public void addIncomingNode(ChartNode chartNode) {

    }

    @Override
    public List<ChartNode> getOutgoingNodes() {
        return List.of();
    }

    @Override
    public ChartNode next(ChartNodeCondition nodeCondition, ChartNode startingNode, boolean isComplete) {
        return null;
    }

    @Override
    public void linkParentToChild(ChartNodeVisitor visitor, int level) {

    }

    @Override
    public void accept(ChartNodeVisitor visitor, int level) {

    }

    @Override
    public List<? extends ParseTree> getChildren() {
        return List.of();
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {

    }

    @Override
    public ChartNode findUpwards(ChartNodeCondition nodeCondition, ChartNode startingNode) {
        return null;
    }

    @Override
    public ChartNode tail() {
        return this;
    }

    @Override
    public ParseTree getExecutionContext() {
        return null;
    }

    @Override
    public DomainDocument getNotes() {
        return null;
    }

    @Override
    public boolean accessesDatabase() {
        return false;
    }

    @Override
    public boolean isMergeable() {
        return false;
    }

    @Override
    public boolean contains(ChartNode node) {
        return false;
    }

    @Override
    public String label() {
        return "";
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public String originalText() {
        return "NULL/" + id();
    }

    @Override
    public ChartNodeType type() {
        return null;
    }

    @Override
    public ChartNode passthrough() {
        return null;
    }

    @Override
    public boolean isPassthrough() {
        return false;
    }

    @Override
    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl) {
        return CobolVmSignal.CONTINUE;
    }

    @Override
    public String id() {
        return "NULL/" + uuid;
    }
}
