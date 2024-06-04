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

    @Deprecated List<ChartNode> getOutgoingNodes();

    // TODO: The implementations need rewrite. This is currently not elegant.
    ChartNode next(ChartNodeCondition nodeCondition, ChartNode startingNode, boolean isComplete);

    void linkParentToChild(ChartNodeVisitor visitor, int level);
    void accept(ChartNodeVisitor visitor, int level);
    void acceptUnvisited(ChartNodeVisitor visitor, int level);

    List<? extends ParseTree> getChildren();

    ChartNode findUpwards(ChartNodeCondition nodeCondition, ChartNode startingNode);
    ChartNode tail();

    DomainDocument getNotes();
    void reset();

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

    CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl);
}
