package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.List;

public class DisplayChartNode extends CobolChartNode {

    private String message;
    private List<CobolParser.DisplayOperandContext> operands;

    public DisplayChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.DisplayStatementContext displayStatement = new SyntaxIdentity<CobolParser.DisplayStatementContext>(executionContext).get();
        operands = displayStatement.displayOperand();
        super.buildInternalFlow();
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.DISPLAY;
    }

    @Override
    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl) {
        CobolVmSignal signal = interpreter.scope(this).executeDisplay(operands, nodeService, this);
        return flowControl.apply((Void) -> continueOrAbort(signal, interpreter, nodeService), signal);
    }

    @Override
    public boolean isMergeable() {
        return true;
    }
}
