package org.poc.flowchart;

import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.List;

public class AddChartNode extends CobolChartNode {
    @Getter private List<CobolParser.AddFromContext> from;
    @Getter private List<CobolParser.AddToContext> tos;

    public AddChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.AddStatementContext addStatement = new SyntaxIdentity<CobolParser.AddStatementContext>(executionContext).get();
        from = addStatement.addToStatement().addFrom();
        tos = addStatement.addToStatement().addTo();
        super.buildInternalFlow();
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.ADD;
    }

    @Override
    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl) {
        CobolVmSignal signal = interpreter.scope(this).executeAdd(this, nodeService);
        return flowControl.apply((Void) -> continueOrAbort(signal, interpreter, nodeService), signal);
    }

    @Override
    public boolean isMergeable() {
        return true;
    }
}
