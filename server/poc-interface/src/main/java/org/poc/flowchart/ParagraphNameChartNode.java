package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.eclipse.lsp.cobol.common.poc.PersistentData;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.*;

public class ParagraphNameChartNode extends CobolChartNode {
    public ParagraphNameChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.PARAGRAPH_NAME;
    }

    @Override
    public ChartNode passthrough() {
        return outgoingNodes.getFirst();
//        return this;
    }

    @Override
    public boolean isPassthrough() {
        return true;
    }
}
