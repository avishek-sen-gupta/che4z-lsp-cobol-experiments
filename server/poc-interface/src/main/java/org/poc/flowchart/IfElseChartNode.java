package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;
import poc.common.flowchart.ChartNodeVisitor;

public class IfElseChartNode extends CompositeCobolNode {
    @Override
    public String shortLabel() {
        return "No";
    }

    public IfElseChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.IF_NO;
    }

    @Override
    public String name() {
        return "No";
    }

    @Override
    public void linkParentToChild(ChartNodeVisitor visitor) {
        visitor.visitParentChildLink(this, internalTreeRoot, nodeService, CHILD_IS_CONDITIONAL_STATEMENT);
    }
}
