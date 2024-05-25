package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;
import org.flowchart.ChartNodeVisitor;

public class DialectStatementChartNode extends CobolChartNode {
    private ChartNode dialectContainerChartNode;

    public DialectStatementChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public void accept(ChartNodeVisitor visitor, int level, int maxLevel) {
        visitor.visitParentChildLink(this, dialectContainerChartNode, nodeService);
        visitor.visit(dialectContainerChartNode, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        ParseTree dialectNodeFiller = executionContext.getChild(0);
        dialectContainerChartNode = innerMostIdmsBlock(dialectNodeFiller);
        dialectContainerChartNode.buildFlow();
    }

    private ChartNode innerMostIdmsBlock(ParseTree dialectNodeFiller) {
        return nodeService.node(idmsContainerNode(dialectNodeFiller).getChild(0));
    }

    private ParseTree idmsContainerNode(ParseTree dialectNodeFiller) {
        for (int i = 0; i < dialectNodeFiller.getChildCount(); i++) {
            if (dialectNodeFiller.getChild(i).getClass() == IdmsContainerNode.class) {
                return dialectNodeFiller.getChild(i);
            }
        }
        return null;
    }
}
