package poc.common.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;

public interface FlowchartBuilder {
    FlowchartBuilder draw(ParseTree root, int maxLevel);
    FlowchartBuilder draw(ParseTree root);
    FlowchartBuilder buildChartTree(ParseTree node);
    FlowchartBuilder write(String dotFilePath);
    FlowchartBuilder outline(ParseTree groupRoot, String clusterLabel);
    FlowchartBuilder connectToComment(String explanation, ParseTree symbol);
    FlowchartBuilder createComment(String comment);
    FlowchartBuilder buildOverlay(ParseTree node);
    FlowchartBuilder buildControlFlow(ParseTree node);
}
