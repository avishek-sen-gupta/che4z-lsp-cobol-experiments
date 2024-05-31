package poc.common.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.poc.common.navigation.CobolEntityNavigator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GenericProcessingChartNode implements ChartNode {
    private List<ChartNode> nodes = new ArrayList<>();
    private final ChartNode enclosingScope;

    public GenericProcessingChartNode(ChartNode node, ChartNode enclosingScope) {
        this.enclosingScope = enclosingScope;
        nodes.add(node);
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
        nodes.add(successor);
    }

    @Override
    public String name() {
        return "Processing Block: " + UUID.randomUUID().toString();
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.GENERIC_PROCESSING;
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

    @Override
    public boolean isMergeable() {
        return false;
    }

    public void append(ChartNode vanillaNode) {

    }

    public void add(ChartNode node) {
        nodes.add(node);
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Processing\n------------------------\n");
        nodes.forEach(n -> builder.append(CobolContextAugmentedTreeNode.originalText(n.getExecutionContext(), CobolEntityNavigator::PASSTHROUGH) + "\n"));
        return builder.toString();
    }

    public boolean contains(ChartNode node) {
        return nodes.contains(node) || nodes.stream().anyMatch(n -> n.contains(node));
    }

    @Override
    public List<ChartNode> getOutgoingNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String shortLabel() {
        return toString();
    }

    @Override
    public List<ChartNode> getTerminalOutgoingNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}