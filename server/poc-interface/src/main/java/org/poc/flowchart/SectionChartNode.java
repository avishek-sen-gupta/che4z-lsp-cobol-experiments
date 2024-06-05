package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class SectionChartNode extends CompositeCobolNode {
    @Override
    public ChartNodeType type() {
        return ChartNodeType.SECTION;
    }

    public SectionChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl) {
        interpreter.enter(this);
        CobolVmSignal signal = internalTreeRoot != null ? internalTreeRoot.acceptInterpreter(interpreter.scope(this), nodeService, FlowControl::CONTINUE) : CobolVmSignal.CONTINUE;
        interpreter.exit(this);
        if (signal == CobolVmSignal.EXIT_PERFORM) return CobolVmSignal.EXIT_PERFORM;
        if (signal == CobolVmSignal.EXIT_SCOPE)
            return flowControl.apply((Void) -> continueOrAbort(signal, interpreter, nodeService), CobolVmSignal.CONTINUE);
        return flowControl.apply((Void) -> continueOrAbort(signal, interpreter, nodeService), signal);
    }

    @Override
    protected CobolVmSignal continueOrAbort(CobolVmSignal signal, CobolInterpreter interpreter, ChartNodeService nodeService) {
        if (signal == CobolVmSignal.TERMINATE || signal == CobolVmSignal.EXIT_PERFORM) return signal;
        if (outgoingNodes.size() > 1) {
            System.out.println("WARNING: ROGUE NODE " + this.label());
        }
        if (outgoingNodes.isEmpty()) return signal;
        return outgoingNodes.getFirst().acceptInterpreter(interpreter, nodeService, FlowControl::CONTINUE);
    }
}
