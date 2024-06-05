package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class IfElseChartNode extends CompositeCobolNode {
    @Override
    public String label() {
        return "No";
    }

    public IfElseChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
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
    public void linkParentToChild(ChartNodeVisitor visitor, int level) {
        visitor.visitParentChildLink(this, internalTreeRoot, new VisitContext(level), nodeService, CHILD_IS_CONDITIONAL_STATEMENT);
    }
}
