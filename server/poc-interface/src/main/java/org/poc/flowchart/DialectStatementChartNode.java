package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;
import org.flowchart.ChartNodeType;
import org.flowchart.ChartNodeVisitor;
import org.poc.common.navigation.CobolEntityNavigator;

public class DialectStatementChartNode extends CobolChartNode {
    private ChartNode idmsChildNode;

    public DialectStatementChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public void accept(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.accept(visitor, level, maxLevel);
        visitor.visitParentChildLink(this, idmsChildNode, nodeService);
        idmsChildNode.accept(visitor, level, maxLevel);
    }

    @Override
    public String name() {
        return truncated(executionContext, 15);
    }

    @Override
    public void buildInternalFlow() {
        CobolEntityNavigator navigator = nodeService.getNavigator();
        ParseTree containerChild = executionContext.getChild(0);
        if (containerChild.getClass() == CobolParser.DialectIfStatmentContext.class) {
            idmsChildNode = new IdmsIfChartNode(containerChild, nodeService);
            nodeService.register(idmsChildNode);
        } else {
            // Treat everything as an IDMS statement for now
            idmsChildNode = nodeService.node(
                    navigator.findByCondition(executionContext,
                            n -> n.getClass() == IdmsParser.IdmsStatementsContext.class));
//            idmsChildNode = idmsContainerChartNode(executionContext);
        }
        idmsChildNode.buildFlow();
    }

    private ChartNode idmsContainerChartNode(ParseTree dialectStatementNode) {
        return nodeService.node(idmsContainerNode(dialectStatementNode));
    }

    private ParseTree idmsContainerNode(ParseTree searchRoot) {
        CobolEntityNavigator navigator = nodeService.getNavigator();
        ParseTree idmsContainer = navigator.findByCondition(searchRoot, n -> n.getClass() == IdmsContainerNode.class);
        return idmsContainer;
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.DIALECT;
    }
}
