package poc.common.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public interface ChartNode {
    void buildFlow();
    void buildOutgoingFlow();
    void buildInternalFlow();
    void buildControlFlow();
    void goesTo(ChartNode successor);
    void addIncomingNode(ChartNode chartNode);
    @Deprecated void removeOutgoingNode(ChartNode chartNode);
    @Deprecated void removeIncomingNode(ChartNode chartNode);

    List<ChartNode> getOutgoingNodes();
    void linkParentToChild(ChartNodeVisitor visitor);
    void accept(ChartNodeVisitor visitor, int level);
    void accept(ChartNodeVisitor visitor, int level, int maxLevel);
    void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel);

    ChartNode find(ChartNodeCondition nodeCondition);

    ParseTree getExecutionContext();
    DomainDocument getNotes();
    void reset();
    @Deprecated void remove();
    boolean accessesDatabase();
    boolean isMergeable();
    boolean contains(ChartNode node);
    String shortLabel();
    String name();
    ChartNodeType type();

    ChartNode passthrough();
    boolean isPassthrough();

    ChartNode next(ChartNodeCondition nodeCondition, ChartNode startingNode);
}
