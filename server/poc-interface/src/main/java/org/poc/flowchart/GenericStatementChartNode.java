package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

public class GenericStatementChartNode extends CobolChartNode {
    public GenericStatementChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
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
