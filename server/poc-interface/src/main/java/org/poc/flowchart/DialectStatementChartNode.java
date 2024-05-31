package org.poc.flowchart;

import com.google.common.collect.ImmutableList;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.eclipse.lsp.cobol.common.poc.PersistentData;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import poc.common.flowchart.*;
import org.poc.common.navigation.CobolEntityNavigator;

import java.util.List;

public class DialectStatementChartNode extends CobolChartNode {
    private ChartNode idmsChildNode;
    private boolean databaseAccess = false;


    public DialectStatementChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.acceptUnvisited(visitor, level, maxLevel);
        visitor.visitParentChildLink(this, idmsChildNode, nodeService);
        idmsChildNode.accept(visitor, level, maxLevel);
    }

    // TODO: Rewrite this monstrosity
    @Override
    public String name() {
        CobolEntityNavigator navigator = nodeService.getNavigator();
        ParseTree dialectGuidContext = navigator.findByCondition(executionContext, t -> t.getClass() == CobolParser.DialectGuidContext.class);
//        ParseTree dialectGuidContext = executionContext.getChild(0).getChild(1);
        String guid = dialectGuidContext.getText();

        ParseTree idmsTextNode = PersistentData.getDialectNode("IDMS-" + guid);
        String codeText = CobolContextAugmentedTreeNode.originalText(idmsTextNode, CobolEntityNavigator::PASSTHROUGH);
        return truncated(codeText, 30);
    }

    @Override
    public void buildInternalFlow() {
        CobolEntityNavigator navigator = nodeService.getNavigator();
        ParseTree containerChild = executionContext.getChild(0);
        System.out.println("IDMS DATA: " + containerChild.getText());
        // TODO: Replace with proper type checking
        if (containerChild.getText().contains("PUT")) {
            System.out.println("FOUND DB ACCESS");
            databaseAccess = true;
        }

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

    @Override
    public boolean accessesDatabase() {
        return databaseAccess;
    }

    @Override
    public List<ChartNode> getTerminalOutgoingNodes() {
        return ImmutableList.of(idmsChildNode);
    }
}
