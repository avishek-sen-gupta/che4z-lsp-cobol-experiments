package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;
import org.flowchart.ChartNodeVisitor;

import java.util.List;

public class IfChartNode extends CobolChartNode {
    private final ChartNodeService nodeService;
    private ChartNode ifThen;
    private ChartNode ifElse;

    public IfChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
        this.nodeService = nodeService;
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
        visitor.visit(this, nodeService);
        visitor.visitSpecific(this, ifThen, nodeService);
        if (ifElse != null) visitor.visitSpecific(this, ifElse, nodeService);

        ifThen.accept(visitor, level, maxLevel);
        if (ifElse != null) ifElse.accept(visitor, level, maxLevel);

        outgoingNodes.forEach(c -> c.accept(visitor, level, maxLevel));
    }

    @Override
    public ChartNode getInternalRoot() {
        return null;
    }
}
