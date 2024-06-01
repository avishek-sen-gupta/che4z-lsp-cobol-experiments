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
    @Deprecated List<ChartNode> getOutgoingNodes();

    ChartNode next(ChartNodeCondition nodeCondition, ChartNode startingNode, boolean isComplete);

    void linkParentToChild(ChartNodeVisitor visitor);
    void accept(ChartNodeVisitor visitor, int level);
    void accept(ChartNodeVisitor visitor, int level, int maxLevel);
    void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel);

    List<? extends ParseTree> getChildren();

    ChartNode find(ChartNodeCondition nodeCondition, ChartNode startingNode);

    DomainDocument getNotes();
    void reset();
    @Deprecated void remove();
    boolean accessesDatabase();
    boolean isMergeable();
    boolean contains(ChartNode node);

    String id();
    String label();
    String name();
    String originalText();
    ParseTree getExecutionContext();
    ChartNodeType type();

    ChartNode passthrough();
    boolean isPassthrough();
}
