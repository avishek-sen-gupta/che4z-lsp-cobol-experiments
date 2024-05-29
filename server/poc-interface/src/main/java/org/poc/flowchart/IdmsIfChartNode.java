package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;
import poc.common.flowchart.ChartNodeVisitor;
import org.poc.common.navigation.CobolEntityNavigator;

public class IdmsIfChartNode extends CobolChartNode {
    private ChartNode ifThenBlock;
    private ChartNode ifElseBlock;
    private ChartNode condition;

    public IdmsIfChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        ParseTree conditionContext = executionContext.getChild(1);
        condition = conditionChartNode(conditionContext);
        condition.buildFlow();
        CobolEntityNavigator navigator = nodeService.getNavigator();
        ParseTree ifThen = navigator.findByCondition(executionContext, n -> n.getClass() == CobolParser.IfThenContext.class, 1);
        ifThenBlock = nodeService.node(ifThen);
        ifThenBlock.buildFlow();
        ParseTree ifElse = navigator.findByCondition(executionContext, n -> n.getClass() == CobolParser.IfElseContext.class, 1);
        if (ifElse != null) {
            ifElseBlock = nodeService.node(ifElse);
            ifElseBlock.buildFlow();
        }
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.acceptUnvisited(visitor, level, maxLevel);
        visitor.visitParentChildLink(this, condition, nodeService);
        condition.accept(visitor, level, maxLevel);

        visitor.visitParentChildLink(this, ifThenBlock, nodeService);
        ifThenBlock.accept(visitor, level, maxLevel);

        if (ifElseBlock != null) {
            visitor.visitParentChildLink(this, ifElseBlock, nodeService);
            ifElseBlock.accept(visitor, level, maxLevel);
        }
    }

    private ChartNode conditionChartNode(ParseTree dialectStatementNode) {
        return nodeService.node(conditionContext(dialectStatementNode));
    }

    private ParseTree conditionContext(ParseTree searchRoot) {
        CobolEntityNavigator navigator = nodeService.getNavigator();
        ParseTree idmsContainer = navigator.findByCondition(searchRoot, n -> n.getClass() == IdmsParser.IdmsIfStatementContext.class);
        return idmsContainer;
    }

    @Override
    public String name() {
        return "IDMS IF";
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.IF_BRANCH;
    }
}
