package poc.common.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public interface ChartNode {
    void buildFlow();

    void buildOutgoingFlow();

    void buildInternalFlow();

    void goesTo(ChartNode successor);

    String name();

    ChartNodeType type();

    void accept(ChartNodeVisitor visitor, int level);

    void accept(ChartNodeVisitor visitor, int level, int maxLevel);

    void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel);

    ParseTree getExecutionContext();

    void addIncomingNode(ChartNode chartNode);

    DomainDocument getNotes();

    void reset();

    void remove();

    void removeOutgoingNode(ChartNode chartNode);

    void removeIncomingNode(ChartNode chartNode);

    boolean accessesDatabase();

    boolean isMergeable();

    boolean contains(ChartNode node);

    List<ChartNode> getOutgoingNodes();

    String shortLabel();

    ChartNode passthrough();

    boolean isPassthrough();
}
