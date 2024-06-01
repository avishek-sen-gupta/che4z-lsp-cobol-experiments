package poc.common.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;

public interface FlowchartBuilder {
    FlowchartBuilder buildGraphic(int maxLevel);
    FlowchartBuilder buildGraphic();
    FlowchartBuilder buildChartAST(ParseTree node);
    FlowchartBuilder write(String dotFilePath);
    FlowchartBuilder outline(ParseTree groupRoot, String clusterLabel);
    FlowchartBuilder connectToComment(String explanation, ParseTree symbol);
    FlowchartBuilder createComment(String comment);
    FlowchartBuilder buildOverlay();
    FlowchartBuilder buildControlFlow();
}
