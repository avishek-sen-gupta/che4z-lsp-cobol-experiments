package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;
import org.flowchart.ChartNodeType;
import org.flowchart.ChartNodeVisitor;

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
    public void accept(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.accept(visitor, level, maxLevel);
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
