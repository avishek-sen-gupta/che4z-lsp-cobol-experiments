package org.poc.flowchart;

import poc.common.flowchart.ChartNode;

import java.util.List;

public class ChartOverlay {
    private final List<GenericProcessingChartNode> groups;

    public ChartOverlay(List<GenericProcessingChartNode> groups) {
        this.groups = groups;
    }

    public ChartNode block(ChartNode node) {
//        return node;
        List<GenericProcessingChartNode> containingGroups = groups.stream().filter(g -> g.contains(node)).toList();
        if (containingGroups.isEmpty()) return node;
        if (containingGroups.size() == 1) return containingGroups.getFirst();
        // Filters out lower-level groups which are GenericStatement aggregations
        return containingGroups.stream().filter(g -> g.getType() != GenericStatementChartNode.class).findFirst().get();

//        Optional<GenericProcessingChartNode> first = groups.stream().filter(g -> g.contains(node)).findFirst();
//        if (first.isPresent()) return first.get();
//        return node;
    }
}
