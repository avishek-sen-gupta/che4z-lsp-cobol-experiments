package vm;

import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.flowchart.IfChartNode;
import org.poc.flowchart.MoveChartNode;
import org.poc.flowchart.ParagraphChartNode;
import org.poc.flowchart.SectionChartNode;
import poc.common.flowchart.*;

import java.util.List;

public class SmolCobolInterpreter implements CobolInterpreter {
    private StackFrames runtimeStackFrames;
    private final ExecuteCondition condition;

    public SmolCobolInterpreter(StackFrames runtimeStackFrames, ExecuteCondition condition) {
        this.runtimeStackFrames = runtimeStackFrames;
        this.condition = condition;
    }

    @Override
    public CobolInterpreter scope(ChartNode scope) {
        return new SmolCobolInterpreter(runtimeStackFrames.add(scope), condition);
    }

    @Override
    public CobolVmSignal execute(ChartNode node) {
        return condition.run((Void) -> {
            System.out.println("Executing " + node.getClass().getSimpleName() + node.label());
            return CobolVmSignal.CONTINUE;
        });
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
        return condition.run((Void) -> {
            System.out.println("Executing an IF condition");
            IfChartNode ifNode = (IfChartNode) node;
            ChartNode ifThenBlock = ifNode.getIfThenBlock();
            return ifThenBlock.acceptInterpreter(this, nodeService, FlowControl::CONTINUE);
        });
    }

    @Override
    public CobolVmSignal executePerformProcedure(List<ChartNode> procedures, ChartNodeService nodeService) {
        return condition.run((Void) -> {
            CobolVmSignal signal = procedures.getFirst().acceptInterpreter(this, nodeService, FlowControl::STOP);
            // If a PERFORM has returned (early or normal termination), do not propagate termination any higher
            return CobolVmSignal.CONTINUE;
        });
    }

    @Override
    public CobolVmSignal executeGoto(List<ChartNode> destinationNodes, ChartNodeService nodeService) {
        return condition.run((Void) -> {
            System.out.println("Executing a GOTO statement; " + destinationNodes.getFirst());
            ChartNode destination = destinationNodes.getFirst();
            ChartNode continuationNode = actualDestination(destination, nodeService);
            CobolVmSignal signal = continuationNode.acceptInterpreter(locator(destination), nodeService, FlowControl::CONTINUE);
            System.out.println("Exiting program");
            System.exit(0);
            return signal;
        });
    }

    @Override
    public CobolVmSignal executeExit(ChartNodeService nodeService) {
        return condition.run((Void) -> {
            System.out.println("Processing EXIT");
//        System.out.println(runtimeStackFrames.stackTrace());
            CobolVmSignal signal = runtimeStackFrames.callSite();
            System.out.println("EXIT instruction is " + signal);
            return signal;
        });
    }

    @Override
    public CobolVmSignal executeNextSentence(ChartNodeService nodeService) {
        return condition.run((Void) -> {
            System.out.println("Processing NEXT SENTENCE");
            return CobolVmSignal.NEXT_SENTENCE;
        });
    }

    @Override
    public CobolVmSignal executeDisplay(List<CobolParser.DisplayOperandContext> messages, ChartNodeService nodeService) {
        return condition.run((Void) -> {
            messages.forEach(m -> System.out.println("CONSOLE >> " + m.getText()));
            return CobolVmSignal.CONTINUE;
        });
    }

    @Override
    public CobolVmSignal executeMove(ChartNode moveChartNode, ChartNodeService nodeService) {
        return condition.run((Void) -> {
            MoveChartNode move = (MoveChartNode) moveChartNode;
            move.getTos().forEach(to -> System.out.println(String.format("%s was affected by %s", to.getText(), move.getFrom().getText())));
            return CobolVmSignal.CONTINUE;
        });
    }

    private CobolInterpreter locator(ChartNode specificLocation) {
//        return new SmolCobolInterpreter(new ExecuteAtTargetFlipCondition(specificLocation), runtimeStackFrames);
        return CobolInterpreterFactory.interpreter(new ExecuteAtTargetFlipCondition(specificLocation), runtimeStackFrames);
    }

    private ChartNode actualDestination(ChartNode destination, ChartNodeService nodeService) {
        if (destination.getClass() == SectionChartNode.class) return destination;
        ParagraphChartNode paragraph = (ParagraphChartNode) destination;
        return paragraph.parentOrSelf();
    }
}
