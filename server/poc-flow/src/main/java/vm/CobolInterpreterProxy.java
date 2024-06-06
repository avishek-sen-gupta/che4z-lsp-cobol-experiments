package vm;

import org.poc.flowchart.CobolStackFrames;
import org.poc.flowchart.IfChartNode;
import org.poc.flowchart.ParagraphChartNode;
import org.poc.flowchart.SectionChartNode;
import poc.common.flowchart.*;

import java.util.List;

public class CobolInterpreterProxy implements CobolInterpreter {
    private ExecuteCondition condition;
    private final CobolInterpreter interpreter;

    public CobolInterpreterProxy(ExecuteCondition condition, CobolInterpreter interpreter) {
        this.condition = condition;
        this.interpreter = interpreter;
        this.interpreter.setParent(this);
    }

    @Override
    public CobolInterpreter scope(ChartNode scope) {
        return new CobolInterpreterProxy(condition, interpreter.scope(scope));
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
        interpreter.enter(node);
    }

    @Override
    public void exit(ChartNode node) {
        System.out.println("Exiting " + node.getClass().getSimpleName() + node.label());
        interpreter.exit(node);
    }

    @Override
    public CobolVmSignal executeIf(ChartNode node, ChartNodeService nodeService) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        return interpreter.executeIf(node, nodeService);
    }

    @Override
    public CobolVmSignal executePerformProcedure(List<ChartNode> procedures, ChartNodeService nodeService) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        return interpreter.executePerformProcedure(procedures, nodeService);
    }

    @Override
    public CobolVmSignal executeGoto(List<ChartNode> destinationNodes, ChartNodeService nodeService) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        return interpreter.executeGoto(destinationNodes, nodeService);
    }

    @Override
    public CobolVmSignal executeExit(ChartNodeService nodeService) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        return interpreter.executeExit(nodeService);
    }

    @Override
    public CobolVmSignal executeNextSentence(ChartNodeService nodeService) {
        if (!condition.shouldExecute()) return CobolVmSignal.CONTINUE;
        return interpreter.executeNextSentence(nodeService);
    }

    @Override
    public void setParent(CobolInterpreter interpreter) {
        throw new UnsupportedOperationException("CANNOT");
    }
}
