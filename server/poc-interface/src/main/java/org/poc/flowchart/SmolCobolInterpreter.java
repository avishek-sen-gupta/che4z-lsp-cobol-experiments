package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;

import java.util.List;

public class SmolCobolInterpreter implements CobolInterpreter {
    private final ChartNode entryPoint;
    private boolean isInterpreting;

    public SmolCobolInterpreter(ChartNode entryPoint) {
        this.entryPoint = entryPoint;
        isInterpreting = entryPoint == null;
    }

    public SmolCobolInterpreter() {
        this(null);
    }

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
        if (node == entryPoint && !isInterpreting) isInterpreting = true;
        System.out.println("Entering " + node.getClass().getSimpleName() + node.label());
    }

    @Override
    public void exit(ChartNode node) {
        System.out.println("Exiting " + node.getClass().getSimpleName() + node.label());
    }

    @Override
    public CobolVmSignal executeIf(ChartNode node, ChartNodeService nodeService) {
        if (!isInterpreting) return CobolVmSignal.CONTINUE;
        System.out.println("Executing an IF condition");
        IfChartNode ifNode = (IfChartNode) node;
        ChartNode ifThenBlock = ifNode.getIfThenBlock();
        return ifThenBlock.acceptInterpreter(this, nodeService, FlowControl::CONTINUE);
    }

    @Override
    public CobolVmSignal executePerformProcedure(List<ChartNode> procedures, ChartNodeService nodeService) {
        if (!isInterpreting) return CobolVmSignal.CONTINUE;
        CobolVmSignal signal = procedures.getFirst().acceptInterpreter(this, nodeService, FlowControl::STOP);
        // If a PERFORM has returned (early or normal termination), do not propagate termination any higher
        return CobolVmSignal.CONTINUE;
    }

    @Override
    public CobolVmSignal executeGoto(List<ChartNode> destinationNodes, ChartNodeService nodeService) {
        if (!isInterpreting) return CobolVmSignal.CONTINUE;
        ChartNode destination = destinationNodes.getFirst();
        ChartNode continuationNode = actualDestination(destination, nodeService);
        CobolVmSignal signal = continuationNode.acceptInterpreter(locator(destination, continuationNode), nodeService, FlowControl::CONTINUE);
        System.out.println("Exiting program");
        System.exit(0);
        return signal;
    }

    @Override
    public ChartNode entryPoint(ChartNode internalTreeRoot, ChartNode parent, ChartNodeService nodeService) {
        if (isInterpreting) return internalTreeRoot;
        if (entryPoint == null) return internalTreeRoot;

        // Replace this with recursive searh on ChartNode itself
        ParseTree destinationExists = nodeService.getNavigator().findByCondition(parent.getExecutionContext(), n -> n == entryPoint.getExecutionContext());
        if (destinationExists == null) throw new RuntimeException(String.format("No valid entry point %s found", entryPoint));
        ChartNode current = internalTreeRoot;
        while(current != entryPoint && !current.getOutgoingNodes().isEmpty()) {
            current = current.getOutgoingNodes().getFirst();
        }
//        if (current != entryPoint) throw new RuntimeException(String.format("No valid entry point %s found", entryPoint));
        if (current != entryPoint) return internalTreeRoot;
        return current;
    }

    private CobolInterpreter locator(ChartNode specificLocation, ChartNode continuationNode) {
        if (specificLocation == continuationNode) return this;
        return new SmolCobolInterpreter(specificLocation);
    }

    private ChartNode actualDestination(ChartNode destination, ChartNodeService nodeService) {
        if (destination.getClass() == SectionChartNode.class) return destination;
        ParagraphChartNode paragraph = (ParagraphChartNode) destination;
        return paragraph.parentOrSelf();
    }
}
