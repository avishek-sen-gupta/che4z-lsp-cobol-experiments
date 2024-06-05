package org.poc.flowchart;

import poc.common.flowchart.*;

import java.util.List;

public class SmolCobolInterpreter implements CobolInterpreter {
    private StackFrames runtimeStackFrames;
    private ExecuteCondition flip;

    public SmolCobolInterpreter(ExecuteCondition flip, StackFrames runtimeStackFrames) {
        this.flip = flip;
        this.runtimeStackFrames = runtimeStackFrames;
    }

    public SmolCobolInterpreter() {
        this(new StackFrames());
    }

    public SmolCobolInterpreter(StackFrames dynamicStackFrames) {
        this(ExecuteCondition.ALWAYS_EXECUTE, dynamicStackFrames);
    }

    @Override
    public CobolInterpreter scope(ChartNode scope) {
        return new SmolCobolInterpreter(flip, runtimeStackFrames.add(scope));
    }

    @Override
    public CobolVmSignal execute(ChartNode node) {
        if (!flip.shouldExecute()) return CobolVmSignal.CONTINUE;
        System.out.println("Executing " + node.getClass().getSimpleName() + node.label());
        return CobolVmSignal.CONTINUE;
    }

    @Override
    public void enter(ChartNode node) {
        flip.evaluate(node);
        System.out.println("Entering " + node.getClass().getSimpleName() + node.label());
    }

    @Override
    public void exit(ChartNode node) {
        System.out.println("Exiting " + node.getClass().getSimpleName() + node.label());
    }

    @Override
    public CobolVmSignal executeIf(ChartNode node, ChartNodeService nodeService) {
        if (!flip.shouldExecute()) return CobolVmSignal.CONTINUE;
        System.out.println("Executing an IF condition");
        IfChartNode ifNode = (IfChartNode) node;
        ChartNode ifThenBlock = ifNode.getIfThenBlock();
        return ifThenBlock.acceptInterpreter(this, nodeService, FlowControl::CONTINUE);
    }

    @Override
    public CobolVmSignal executePerformProcedure(List<ChartNode> procedures, ChartNodeService nodeService) {
        if (!flip.shouldExecute()) return CobolVmSignal.CONTINUE;
        CobolVmSignal signal = procedures.getFirst().acceptInterpreter(this, nodeService, FlowControl::STOP);
        // If a PERFORM has returned (early or normal termination), do not propagate termination any higher
        return CobolVmSignal.CONTINUE;
    }

    @Override
    public CobolVmSignal executeGoto(List<ChartNode> destinationNodes, ChartNodeService nodeService) {
        if (!flip.shouldExecute()) return CobolVmSignal.CONTINUE;
        ChartNode destination = destinationNodes.getFirst();
        ChartNode continuationNode = actualDestination(destination, nodeService);
        CobolVmSignal signal = continuationNode.acceptInterpreter(locator(destination, continuationNode), nodeService, FlowControl::CONTINUE);
        System.out.println("Exiting program");
        System.exit(0);
        return signal;
    }

    @Override
    public CobolVmSignal executeExit(ChartNodeService nodeService) {
        System.out.println("Processing EXIT");
        return CobolVmSignal.CONTINUE;
    }

    private CobolInterpreter locator(ChartNode specificLocation, ChartNode continuationNode) {
//        if (specificLocation == continuationNode) return this;
        return new SmolCobolInterpreter(new ExecuteAtTargetFlipCondition(specificLocation), runtimeStackFrames);
    }

    private ChartNode actualDestination(ChartNode destination, ChartNodeService nodeService) {
        if (destination.getClass() == SectionChartNode.class) return destination;
        ParagraphChartNode paragraph = (ParagraphChartNode) destination;
        return paragraph.parentOrSelf();
    }
}
