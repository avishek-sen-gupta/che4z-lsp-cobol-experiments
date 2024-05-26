package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;
import poc.common.flowchart.ChartNodeVisitor;

public class IfChartNode extends CobolChartNode {
    private ChartNode ifThen;
    private ChartNode ifElse;

    public IfChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.IfStatementContext ifStatement = new StatementIdentity<CobolParser.IfStatementContext>(getExecutionContext()).get();
        ChartNode ifThenBlock = nodeService.node(ifStatement.ifThen());
        ifThenBlock.buildFlow();
        this.ifThen = ifThenBlock;
        CobolParser.IfElseContext ifElseCtx = ifStatement.ifElse();
        if (ifElseCtx == null) return;
        ChartNode ifElseBlock = nodeService.node(ifElseCtx);
        ifElseBlock.buildFlow();
        this.ifElse = ifElseBlock;
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.acceptUnvisited(visitor, level, maxLevel);
        visitor.visitParentChildLink(this, ifThen, nodeService);
        if (ifElse != null) visitor.visitParentChildLink(this, ifElse, nodeService);

        ifThen.accept(visitor, level, maxLevel);
        if (ifElse != null) ifElse.accept(visitor, level, maxLevel);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.CONDITIONAL;
    }
}