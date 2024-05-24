package org.flowchart;

public interface ChartNode {
    void buildFlow();

    void goesTo(ChartNode successor);

    void accept(ChartNodeVisitor visitor, int level);

    void accept(ChartNodeVisitor visitor, int level, int maxLevel);

    java.util.List<ChartNode> getOutgoingNodes();

    org.antlr.v4.runtime.tree.ParseTree getExecutionContext();

    void addIncomingNode(ChartNode chartNode);
}
