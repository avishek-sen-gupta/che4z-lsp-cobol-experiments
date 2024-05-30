package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.eclipse.lsp.cobol.common.poc.PersistentData;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.*;

public class ConditionalStatementChartNode extends CompositeCobolNode {
    public ConditionalStatementChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
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
