package org.poc.flowchart;

import poc.common.flowchart.*;

import java.util.List;

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
        return ifThenBlock.acceptInterpreter(this, nodeService, FlowControl::CONTINUE);
    }

    @Override
    public CobolVmSignal executePerformProcedure(List<ChartNode> procedures, ChartNodeService nodeService) {
        return procedures.getFirst().acceptInterpreter(this, nodeService, FlowControl::STOP);
    }

    @Override
    public CobolVmSignal executeGoto(List<ChartNode> destinationNodes, ChartNodeService nodeService) {
        ChartNode destination = destinationNodes.getFirst();
        ChartNode continuationNode = actualDestination(destination, nodeService);
        CobolVmSignal signal = continuationNode.acceptInterpreter(this, nodeService, FlowControl::CONTINUE);
        System.out.println("Exiting program");
        System.exit(0);
        return signal;
    }

    private ChartNode actualDestination(ChartNode destination, ChartNodeService nodeService) {
        if (destination.getClass() == SectionChartNode.class) return destination;
        ParagraphChartNode paragraph = (ParagraphChartNode) destination;
        return paragraph.parentOrSelf();
    }
}
