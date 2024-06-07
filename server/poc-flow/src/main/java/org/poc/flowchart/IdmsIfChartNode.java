package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import poc.common.flowchart.*;
import org.poc.common.navigation.CobolEntityNavigator;

public class IdmsIfChartNode extends CobolChartNode {
    private ChartNode ifThenBlock;
    private ChartNode ifElseBlock;
    private ChartNode condition;

    public IdmsIfChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames staticFrameContext) {
        super(parseTree, scope, nodeService, staticFrameContext);
    }

    @Override
    public void buildInternalFlow() {
        ParseTree conditionContext = executionContext.getChild(1);
        condition = conditionChartNode(conditionContext);
        condition.buildFlow();
        CobolEntityNavigator navigator = nodeService.getNavigator();
        ParseTree ifThen = navigator.findByCondition(executionContext, n -> n.getClass() == CobolParser.IfThenContext.class, 1);
        ifThenBlock = nodeService.node(ifThen, this, staticFrameContext);
        ifThenBlock.buildFlow();
        ParseTree ifElse = navigator.findByCondition(executionContext, n -> n.getClass() == CobolParser.IfElseContext.class, 1);
        if (ifElse != null) {
            ifElseBlock = nodeService.node(ifElse, this, staticFrameContext);
            ifElseBlock.buildFlow();
        }
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {
        super.acceptUnvisited(visitor, level);
        visitor.visitParentChildLink(this, condition, new VisitContext(level), nodeService);
        condition.accept(visitor, level);

        visitor.visitParentChildLink(this, ifThenBlock, new VisitContext(level), nodeService);
        ifThenBlock.accept(visitor, level);

        if (ifElseBlock != null) {
            visitor.visitParentChildLink(this, ifElseBlock, new VisitContext(level), nodeService);
            ifElseBlock.accept(visitor, level);
        }
    }

    private ChartNode conditionChartNode(ParseTree dialectStatementNode) {
        return nodeService.node(conditionContext(dialectStatementNode), this, staticFrameContext);
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
