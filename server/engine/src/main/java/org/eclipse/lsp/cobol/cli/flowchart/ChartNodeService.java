package org.eclipse.lsp.cobol.cli.flowchart;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.vm.CobolEntityNavigator;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;

public class ChartNodeService {
    private List<ChartNode> nodes = new ArrayList<>();
    private CobolEntityNavigator navigator;

    public ChartNodeService(CobolEntityNavigator navigator) {
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
        ChartNode n = new ChartNode(parseTree, this);
        int index = nodes.indexOf(n);
        if (index != -1) return nodes.get(index);
        nodes.add(n);
        return n;
    }

    public ChartNode sectionOrParaWithName(String name) {
        ParseTree target = navigator.findTarget(name);
        return node(target);
    }
}
