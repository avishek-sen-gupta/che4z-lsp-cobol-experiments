package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;

public class IdmsContainerChartNode extends CobolChartNode {
    public IdmsContainerChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }
}
