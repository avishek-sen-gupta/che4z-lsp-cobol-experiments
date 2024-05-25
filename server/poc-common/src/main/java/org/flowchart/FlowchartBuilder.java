package org.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public interface FlowchartBuilder {
    FlowchartBuilder draw(List<ParseTree> roots, int maxLevel);
    FlowchartBuilder draw(ParseTree root, int maxLevel);
    FlowchartBuilder draw(List<ParseTree> roots);
    FlowchartBuilder draw(ParseTree root);
    FlowchartBuilder write(String dotFilePath);
}
