package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class IfThenChartNode extends CompositeCobolNode {
    public IfThenChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.IF_YES;
    }

    @Override
    public String label() {
        return "Yes";
    }

    @Override
    public String name() {
        return "Yes";
    }

    @Override
    public void linkParentToChild(ChartNodeVisitor visitor, int level) {
        visitor.visitParentChildLink(this, internalTreeRoot, new VisitContext(level), nodeService, CHILD_IS_CONDITIONAL_STATEMENT);
    }
}
