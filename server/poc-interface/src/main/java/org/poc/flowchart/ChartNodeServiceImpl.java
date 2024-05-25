package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;
import org.poc.common.navigation.CobolEntityNavigator;

import java.util.ArrayList;
import java.util.List;

public class ChartNodeServiceImpl implements ChartNodeService {
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

    public ChartNode node(ParseTree parseTree) {
        if (parseTree == null) return new DummyChartNode(this);
        ChartNode n = CobolChartNodeFactory.newNode(parseTree, this);
        int index = nodes.indexOf(n);
        if (index != -1) return nodes.get(index);
        nodes.add(n);
        return n;
    }

    @Override
    public ChartNode sectionOrParaWithName(String name) {
        ParseTree target = navigator.target(name);
        return node(target);
    }
}
