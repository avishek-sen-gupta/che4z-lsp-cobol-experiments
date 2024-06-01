package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import org.poc.common.navigation.CobolEntityNavigator;

import java.util.ArrayList;
import java.util.List;

public class ChartNodeServiceImpl implements ChartNodeService {
    int counter = 0;
    private List<ChartNode> nodes = new ArrayList<>();
    private CobolEntityNavigator navigator;

    public ChartNodeServiceImpl(CobolEntityNavigator navigator) {
        this.navigator = navigator;
    }

    public ChartNode register(ChartNode chartNode) {
        int index = nodes.indexOf(chartNode);
        if (index != -1) return nodes.get(index);
        nodes.add(chartNode);
        return chartNode;
    }

    public ChartNode node(ParseTree parseTree, ChartNode scope) {
        if (parseTree == null) return new DummyChartNode(this);
        ChartNode n = CobolChartNodeFactory.newNode(parseTree, scope, this);
        int index = nodes.indexOf(n);
        if (index != -1) return nodes.get(index);
        nodes.add(n);
        return n;
    }

    @Override
    public ChartNode sectionOrParaWithName(String name) {
        ParseTree target = navigator.target(name);
        ChartNode existingGroup = existingNode(target);

        // Only place where it's acceptable to have null scope since this section/para is not part of the flowchart's AST, so it's a dummy placeholder
        return existingGroup != null ? existingGroup : node(target, null);
    }

    @Override
    public CobolEntityNavigator getNavigator() {
        return navigator;
    }

    @Override
    public ChartNode existingNode(ParseTree parseTree) {
        return nodes.stream().filter(n -> n.getExecutionContext() == parseTree).findFirst().orElse(null);
    }

    @Override
    public int counter() {
        counter ++;
        return counter;
    }
}
