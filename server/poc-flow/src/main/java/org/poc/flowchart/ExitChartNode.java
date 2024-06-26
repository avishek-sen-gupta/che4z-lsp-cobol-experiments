package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

public class ExitChartNode extends CobolChartNode {
    public ExitChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl) {
        return interpreter.scope(this).executeExit(nodeService, this);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.EXIT;
    }
}
