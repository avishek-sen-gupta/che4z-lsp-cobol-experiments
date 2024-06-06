package org.poc.flowchart;

import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;

import java.util.List;

public class ChartNodes {

    private final List<ChartNode> nodes;
    private final ChartNodeService nodeService;

    public ChartNodes(List<ChartNode> nodes, ChartNodeService nodeService) {
        this.nodes = nodes;
        this.nodeService = nodeService;
    }

    public ChartNode first() {
        if (nodes.isEmpty()) return new NullChartNode(nodeService);
        return nodes.getFirst();
    }
}
