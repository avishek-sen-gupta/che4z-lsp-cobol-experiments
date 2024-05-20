package org.eclipse.lsp.cobol.cli.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class ChartNodeService {
    private List<ChartNode> nodes = new ArrayList<>();

    public ChartNode register(ChartNode chartNode) {
        int index = nodes.indexOf(chartNode);
        if (index != -1) return nodes.get(index);
        nodes.add(chartNode);
        return chartNode;
    }

    public ChartNode node(ParseTree parseTree) {
        ChartNode n = new ChartNode(parseTree, this);
        int index = nodes.indexOf(n);
        if (index != -1) return nodes.get(index);
        nodes.add(n);
        return n;
    }
}
