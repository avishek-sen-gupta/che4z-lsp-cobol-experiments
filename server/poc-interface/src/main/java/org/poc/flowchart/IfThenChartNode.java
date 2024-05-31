package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

public class IfThenChartNode extends CompositeCobolNode {
    public IfThenChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.IF_YES;
    }

    @Override
    public String shortLabel() {
        return "Yes";
    }

    @Override
    public String name() {
        return "Yes";
    }

    @Override
    public void linkParentToChild(ChartNodeVisitor visitor) {
        visitor.visitParentChildLink(this, internalTreeRoot, nodeService, CHILD_IS_CONDITIONAL_STATEMENT);
    }
}
