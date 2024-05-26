package poc.common.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;

public interface ChartNode {
    void buildFlow();

    void buildOutgoingFlow();

    void buildInternalFlow();

    void goesTo(ChartNode successor);

    String name();

    ChartNodeType type();

    void accept(ChartNodeVisitor visitor, int level);

    void accept(ChartNodeVisitor visitor, int level, int maxLevel);

    ParseTree getExecutionContext();

    void addIncomingNode(ChartNode chartNode);

//    DomainDocument getNotes();
}
