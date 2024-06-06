package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;
import vm.CobolInterpreterProxy;

public class SectionChartNode extends CompositeCobolNode {
    @Override
    public ChartNodeType type() {
        return ChartNodeType.SECTION;
    }

    public SectionChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl forwardFlowControl) {
        assert interpreter.getClass() == CobolInterpreterProxy.class;
        CobolVmSignal signal = executeInternalRoot(interpreter, nodeService);
        if (signal == CobolVmSignal.EXIT_SCOPE)
            return forwardFlowControl.apply((Void) -> continueOrAbort(signal, interpreter, nodeService), CobolVmSignal.CONTINUE);
        return forwardFlowControl.apply((Void) -> continueOrAbort(signal, interpreter, nodeService), signal);
    }

    @Override
    protected CobolVmSignal continueOrAbort(CobolVmSignal defaultSignal, CobolInterpreter interpreter, ChartNodeService nodeService) {
        if (defaultSignal == CobolVmSignal.TERMINATE || defaultSignal == CobolVmSignal.EXIT_PERFORM) return defaultSignal;
        return next(defaultSignal, interpreter, nodeService);
    }
}
