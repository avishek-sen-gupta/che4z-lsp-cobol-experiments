package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

import java.util.Optional;

public class ParagraphChartNode extends CompositeCobolNode {
    @Override
    public ChartNodeType type() {
        return ChartNodeType.PARAGRAPH;
    }

    public ParagraphChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    public ChartNode parentOrSelf() {
        Optional<ChartNode> parent = staticFrameContext.find(f -> f.getClass() == SectionChartNode.class);
        return parent.orElse(this);
    }

    @Override
    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl forwardFlowControl) {
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
