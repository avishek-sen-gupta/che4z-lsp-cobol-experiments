package poc.common.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class ChartNodes {

    private final List<ChartNode> nodes;

    public ChartNodes(List<ChartNode> nodes) {
        this.nodes = nodes;
    }

    public ChartNode first() {
        if (nodes.isEmpty()) return new NullChartNode();
        return nodes.getFirst();
    }
}
