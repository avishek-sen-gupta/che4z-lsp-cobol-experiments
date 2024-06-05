package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class GenericStatementChartNode extends CobolChartNode {
    public GenericStatementChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public boolean isMergeable() {
        return true;
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.GENERIC_STATEMENT;
    }
}
