package vm;

import org.poc.flowchart.CobolStackFrames;
import org.poc.flowchart.IfChartNode;
import org.poc.flowchart.ParagraphChartNode;
import org.poc.flowchart.SectionChartNode;
import poc.common.flowchart.*;

import java.util.List;

public class SmolCobolInterpreter implements CobolInterpreter {
    private StackFrames runtimeStackFrames;
    private ExecuteCondition condition;

    public SmolCobolInterpreter(ExecuteCondition condition, StackFrames runtimeStackFrames) {
        this.condition = condition;
        this.runtimeStackFrames = runtimeStackFrames;
    }

    public SmolCobolInterpreter() {
        this(new CobolStackFrames());
    }

    public SmolCobolInterpreter(StackFrames dynamicStackFrames) {
        this(ExecuteCondition.ALWAYS_EXECUTE, dynamicStackFrames);
    }

    @Override
    public CobolInterpreter scope(ChartNode scope) {
        return new SmolCobolInterpreter(condition, runtimeStackFrames.add(scope));
    }

    @Override
    public CobolVmSignal execute(ChartNode node) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        System.out.println("Executing " + node.getClass().getSimpleName() + node.label());
        return CobolVmSignal.CONTINUE;
    }

    @Override
    public void enter(ChartNode node) {
        condition.evaluate(node);
        System.out.println("Entering " + node.getClass().getSimpleName() + node.label());
    }

    @Override
    public void exit(ChartNode node) {
        System.out.println("Exiting " + node.getClass().getSimpleName() + node.label());
    }

    @Override
    public CobolVmSignal executeIf(ChartNode node, ChartNodeService nodeService) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        System.out.println("Executing an IF condition");
        IfChartNode ifNode = (IfChartNode) node;
        ChartNode ifThenBlock = ifNode.getIfThenBlock();
        return ifThenBlock.acceptInterpreter(this, nodeService, FlowControl::CONTINUE);
    }

    @Override
    public CobolVmSignal executePerformProcedure(List<ChartNode> procedures, ChartNodeService nodeService) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        CobolVmSignal signal = procedures.getFirst().acceptInterpreter(this, nodeService, FlowControl::STOP);
        // If a PERFORM has returned (early or normal termination), do not propagate termination any higher
        return CobolVmSignal.CONTINUE;
    }

    @Override
    public CobolVmSignal executeGoto(List<ChartNode> destinationNodes, ChartNodeService nodeService) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        ChartNode destination = destinationNodes.getFirst();
        ChartNode continuationNode = actualDestination(destination, nodeService);
        CobolVmSignal signal = continuationNode.acceptInterpreter(locator(destination, continuationNode), nodeService, FlowControl::CONTINUE);
        System.out.println("Exiting program");
        System.exit(0);
        return signal;
    }

    @Override
    public CobolVmSignal executeExit(ChartNodeService nodeService) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        System.out.println("Processing EXIT");
//        System.out.println(runtimeStackFrames.stackTrace());

        CobolVmSignal signal = runtimeStackFrames.callSite();
        System.out.println("EXIT instruction is " + signal);
        return signal;
    }

    @Override
    public CobolVmSignal executeNextSentence(ChartNodeService nodeService) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        System.out.println("Processing NEXT SENTENCE");
        return CobolVmSignal.NEXT_SENTENCE;
    }

    private CobolInterpreter locator(ChartNode specificLocation, ChartNode continuationNode) {
        return new SmolCobolInterpreter(new ExecuteAtTargetFlipCondition(specificLocation), runtimeStackFrames);
    }

    private ChartNode actualDestination(ChartNode destination, ChartNodeService nodeService) {
        if (destination.getClass() == SectionChartNode.class) return destination;
        ParagraphChartNode paragraph = (ParagraphChartNode) destination;
        return paragraph.parentOrSelf();
    }
}
