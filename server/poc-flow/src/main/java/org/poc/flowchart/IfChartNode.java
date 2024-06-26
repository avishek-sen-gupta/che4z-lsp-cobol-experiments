package org.poc.flowchart;

import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

public class IfChartNode extends CobolChartNode {
    @Getter private ChartNode ifThenBlock;
    @Getter private ChartNode ifElseBlock;
    @Getter private CobolParser.ConditionContext condition;

    public IfChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.IfStatementContext ifStatement = new SyntaxIdentity<CobolParser.IfStatementContext>(getExecutionContext()).get();
        condition = (CobolParser.ConditionContext) ifStatement.getChild(1);
        ChartNode ifThenBlock = nodeService.node(ifStatement.ifThen(), this, staticFrameContext.add(this));
        ifThenBlock.buildFlow();
        this.ifThenBlock = ifThenBlock;
        CobolParser.IfElseContext ifElseCtx = ifStatement.ifElse();
        if (ifElseCtx == null) return;
        ChartNode ifElseBlock = nodeService.node(ifElseCtx, this, staticFrameContext.add(this));
        ifElseBlock.buildFlow();
        this.ifElseBlock = ifElseBlock;
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {
        super.acceptUnvisited(visitor, level);
        visitor.visitParentChildLink(this, ifThenBlock, new VisitContext(level), nodeService);
        if (ifElseBlock != null) visitor.visitParentChildLink(this, ifElseBlock, new VisitContext(level), nodeService);

        ifThenBlock.accept(visitor, level);
        if (ifElseBlock != null) ifElseBlock.accept(visitor, level);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.IF_BRANCH;
    }

    @Override
    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl) {
        CobolVmSignal signal = interpreter.scope(this).executeIf(this, nodeService);
        return flowControl.apply((Void) -> continueOrAbort(signal, interpreter, nodeService), signal);
    }

    @Override
    public String name() {
        CobolParser.IfStatementContext ifStatement = new SyntaxIdentity<CobolParser.IfStatementContext>(getExecutionContext()).get();
        CobolParser.ConditionContext condition = (CobolParser.ConditionContext) ifStatement.getChild(1);
        String codeText = NodeText.originalText(condition, NodeText::PASSTHROUGH);
        return "IS \n" + truncated(codeText, 40) + "?\n";
    }

}
