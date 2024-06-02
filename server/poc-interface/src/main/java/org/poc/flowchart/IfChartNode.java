package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

public class IfChartNode extends CobolChartNode {
    private ChartNode ifThenBlock;
    private ChartNode ifElseBlock;

    public IfChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.IfStatementContext ifStatement = new SyntaxIdentity<CobolParser.IfStatementContext>(getExecutionContext()).get();
        ChartNode ifThenBlock = nodeService.node(ifStatement.ifThen(), this);
        ifThenBlock.buildFlow();
        this.ifThenBlock = ifThenBlock;
        CobolParser.IfElseContext ifElseCtx = ifStatement.ifElse();
        if (ifElseCtx == null) return;
        ChartNode ifElseBlock = nodeService.node(ifElseCtx, this);
        ifElseBlock.buildFlow();
        this.ifElseBlock = ifElseBlock;
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.acceptUnvisited(visitor, level, maxLevel);
        visitor.visitParentChildLink(this, ifThenBlock, nodeService);
        if (ifElseBlock != null) visitor.visitParentChildLink(this, ifElseBlock, nodeService);

        ifThenBlock.accept(visitor, level, maxLevel);
        if (ifElseBlock != null) ifElseBlock.accept(visitor, level, maxLevel);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.IF_BRANCH;
    }

    @Override
    public String name() {
        CobolParser.IfStatementContext ifStatement = new SyntaxIdentity<CobolParser.IfStatementContext>(getExecutionContext()).get();
        CobolParser.ConditionContext condition = (CobolParser.ConditionContext) ifStatement.getChild(1);
        String codeText = NodeText.originalText(condition, NodeText::PASSTHROUGH);
        return "IS \n" + truncated(codeText, 40) + "?\n";
    }

    @Override
    public void reset() {
        super.reset();
        ifThenBlock.reset();
        if (ifElseBlock != null) ifElseBlock.reset();
    }
}
