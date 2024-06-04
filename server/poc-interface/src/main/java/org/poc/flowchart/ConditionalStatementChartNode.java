package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class ConditionalStatementChartNode extends CompositeCobolNode {
    public ConditionalStatementChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.CONDITIONAL_STATEMENT;
    }

    @Override
    public boolean isMergeable() {
        return internalTreeRoot.isMergeable();
    }

    @Override
    public String name() {
        return executionContext.getText();
    }

    @Override
    public boolean contains(ChartNode node) {
        return internalTreeRoot == node;
    }
}
