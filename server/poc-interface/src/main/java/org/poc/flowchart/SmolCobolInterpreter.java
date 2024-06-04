package org.poc.flowchart;

import poc.common.flowchart.*;

public class SmolCobolInterpreter implements CobolInterpreter {
    @Override
    public CobolInterpreter scope(ChartNode scope) {
        return this;
    }

    @Override
    public CobolVmSignal execute(ChartNode node) {
        System.out.println("Executing " + node.getClass().getSimpleName() + node.label());
        return CobolVmSignal.CONTINUE;
    }

    @Override
    public void enter(ChartNode node) {
        System.out.println("Entering " + node.getClass().getSimpleName() + node.label());
    }

    @Override
    public void exit(ChartNode node) {
        System.out.println("Exiting " + node.getClass().getSimpleName() + node.label());
    }

    @Override
    public CobolVmSignal executeIf(ChartNode node, ChartNodeService nodeService) {
        System.out.println("Executing an IF condition");
        IfChartNode ifNode = (IfChartNode) node;
        ChartNode ifThenBlock = ifNode.getIfThenBlock();
        return ifThenBlock.acceptInterpreter(this.scope(ifThenBlock), nodeService);
    }
}
