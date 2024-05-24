package flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class DummyChartNode extends CobolChartNode {
    public DummyChartNode(ChartNodeServiceImpl nodeService) {
        super(null, nodeService);
    }

    @Override
    public void buildFlow() {

    }

    @Override
    protected boolean isCompositeNode(ParseTree executionContext) {
        return false;
    }

    @Override
    public List<ChartNode> getOutgoingNodes() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "DUMMY_NODE";
    }

    @Override
    public void accept(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.accept(visitor, level, maxLevel);
    }

    @Override
    public String executionContextName() {
        return "DUMMY_EXECUTION_CONTEXT";
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }
}
