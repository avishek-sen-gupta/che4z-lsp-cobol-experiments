package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;
import vm.CobolInterpreterProxy;

public class ExitChartNode extends CobolChartNode {
    public ExitChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl) {
        assert interpreter.getClass() == CobolInterpreterProxy.class;
        return interpreter.scope(this).executeExit(nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.EXIT;
    }
}
