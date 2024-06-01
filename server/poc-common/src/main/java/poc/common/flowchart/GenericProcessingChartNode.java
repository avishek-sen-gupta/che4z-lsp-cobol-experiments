package poc.common.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolDataDivisionParser;
import org.poc.common.navigation.CobolEntityNavigator;

import java.util.ArrayList;
import java.util.List;

public class GenericProcessingChartNode implements ChartNode {
    private List<ChartNode> nodes = new ArrayList<>();
    private final ChartNode enclosingScope;
    private String uuid;

    public GenericProcessingChartNode(ChartNode node, ChartNode enclosingScope, ChartNodeService nodeService) {
        this.uuid = String.valueOf(nodeService.counter());
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
    public void buildControlFlow() {

    }

    @Override
    public void goesTo(ChartNode successor) {
        nodes.add(successor);
    }

    @Override
    public String name() {
        return "Processing Block: " + uuid;
    }

    @Override
    public String originalText() {
        return label();
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
    public List<? extends ParseTree> getChildren() {
        return List.of();
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {

    }

    @Override
    public ChartNode find(ChartNodeCondition nodeCondition, ChartNode startingNode) {
        return null;
    }

    @Override
    public ParseTree getExecutionContext() {
        return new CobolDataDivisionParser.LabelRecordsClauseContext(null, 1);
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
        return label() + "/" + uuid;
    }

    public boolean contains(ChartNode node) {
        return nodes.contains(node) || nodes.stream().anyMatch(n -> n.contains(node));
    }

    @Override
    public List<ChartNode> getOutgoingNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String label() {
        StringBuilder builder = new StringBuilder("Processing\n------------------------\n");
        nodes.forEach(n -> builder.append(NodeText.originalText(n.getExecutionContext(), NodeText::PASSTHROUGH)).append("\n"));
        return builder.toString();
    }

    @Override
    public ChartNode passthrough() {
        return this;
    }

    @Override
    public boolean isPassthrough() {
        return false;
    }

    @Override
    public String id() {
        return name() + "/" + uuid;
    }

    @Override
    public ChartNode next(ChartNodeCondition nodeCondition, ChartNode startingNode, boolean isComplete) {
        return null;
    }

    @Override
    public void linkParentToChild(ChartNodeVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
