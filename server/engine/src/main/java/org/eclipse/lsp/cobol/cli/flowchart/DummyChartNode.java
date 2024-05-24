package org.eclipse.lsp.cobol.cli.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class DummyChartNode extends ChartNode {
    public DummyChartNode(ChartNodeService nodeService) {
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
