package org.poc.flowchart;

import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.List;

public class MoveChartNode extends CobolChartNode {

    @Getter private CobolParser.MoveToSendingAreaContext from;
    @Getter private List<CobolParser.GeneralIdentifierContext> tos;

    public MoveChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.MoveStatementContext moveStatement = new SyntaxIdentity<CobolParser.MoveStatementContext>(executionContext).get();
        from = moveStatement.moveToStatement().moveToSendingArea();
        tos = moveStatement.moveToStatement().generalIdentifier();
        super.buildInternalFlow();
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.MOVE;
    }

    @Override
    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl) {
        CobolVmSignal signal = interpreter.scope(this).executeMove(this, nodeService);
        return flowControl.apply((Void) -> continueOrAbort(signal, interpreter, nodeService), signal);
    }

    @Override
    public boolean isMergeable() {
        return true;
    }
}
