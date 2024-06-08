package org.poc.vm;

import org.antlr.v4.runtime.RuleContext;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.flowchart.*;
import poc.common.flowchart.*;

import java.util.List;

import static poc.common.flowchart.ConsoleColors.*;

public class SmolCobolInterpreter implements CobolInterpreter {
    private StackFrames runtimeStackFrames;
    private final ExecuteCondition condition;
    private final ConditionResolver conditionResolver;

    public SmolCobolInterpreter(StackFrames runtimeStackFrames, ExecuteCondition condition, ConditionResolver conditionResolver) {
        this.runtimeStackFrames = runtimeStackFrames;
        this.condition = condition;
        this.conditionResolver = conditionResolver;
    }

    @Override
    public CobolInterpreter scope(ChartNode scope) {
        return new SmolCobolInterpreter(runtimeStackFrames.add(scope), condition, conditionResolver);
    }

    @Override
    public CobolVmSignal execute(ChartNode node) {
        return condition.run((Void) -> {
            System.out.println(coloured("Executing " + node.getClass().getSimpleName() + node.label(), 25));
            return CobolVmSignal.CONTINUE;
        });
    }

    @Override
    public void enter(ChartNode node) {
        condition.evaluate(node);
        System.out.println(coloured("Entering " + node.getClass().getSimpleName() + node.label(), 240));
    }

    @Override
    public void exit(ChartNode node) {
        System.out.println(coloured("Exiting " + node.getClass().getSimpleName() + node.label(), 240));
    }

    @Override
    public CobolVmSignal executeIf(ChartNode node, ChartNodeService nodeService) {
        return condition.run((Void) -> {
            System.out.println("Executing an IF condition");
            IfChartNode ifNode = (IfChartNode) node;
            boolean trueOrFalse = conditionResolver.resolve(node);
            if (trueOrFalse) {
                System.out.println("ROUTING TO IF-THEN");
                ChartNode ifThenBlock = ifNode.getIfThenBlock();
                return ifThenBlock.acceptInterpreter(this, nodeService, FlowControl::CONTINUE);
            } else if (ifNode.getIfElseBlock() != null) {
                System.out.println("ROUTING TO IF-ELSE");
                ChartNode ifElseBlock = ifNode.getIfElseBlock();
                return ifElseBlock.acceptInterpreter(this, nodeService, FlowControl::CONTINUE);
            }
            System.out.println("IF-ELSE BLOCK NOT PRESENT, TERMINATING IF STATEMENT...");
            return CobolVmSignal.CONTINUE;
        });
    }

    @Override
    public CobolVmSignal executePerformProcedure(List<ChartNode> procedures, ChartNodeService nodeService) {
        return condition.run((Void) -> {
            System.out.println(cyan("Executing a PERFORM statement: " + procedures.getFirst()));
            CobolVmSignal signal = procedures.getFirst().acceptInterpreter(this, nodeService, FlowControl::STOP);
            System.out.println(cyan("Returned from PERFORM statement: " + procedures.getFirst()));
            // If a PERFORM has returned (early or normal termination), do not propagate termination any higher
            return CobolVmSignal.CONTINUE;
        });
    }

    @Override
    public CobolVmSignal executeGoto(List<ChartNode> destinationNodes, ChartNodeService nodeService) {
        return condition.run((Void) -> {
            System.out.println(red("Executing a GOTO statement: " + destinationNodes.getFirst()));
            ChartNode destination = destinationNodes.getFirst();
            ChartNode continuationNode = actualDestination(destination);
            CobolVmSignal signal = continuationNode.acceptInterpreter(locator(destination), nodeService, FlowControl::CONTINUE);
            System.out.println(red("Exiting program"));
            System.exit(0);
            return signal;
        });
    }

    @Override
    public CobolVmSignal executeExit(ChartNodeService nodeService) {
        return condition.run((Void) -> {
            System.out.println(coloured("Processing EXIT", 52));
//        System.out.println(runtimeStackFrames.stackTrace());
            CobolVmSignal signal = runtimeStackFrames.callSite();
            System.out.println("EXIT instruction is " + coloured(signal.name(), 207));
            return signal;
        });
    }

    @Override
    public CobolVmSignal executeNextSentence(ChartNodeService nodeService) {
        return condition.run((Void) -> {
            System.out.println(purple("Processing NEXT SENTENCE"));
            return CobolVmSignal.NEXT_SENTENCE;
        });
    }

    @Override
    public CobolVmSignal executeDisplay(List<CobolParser.DisplayOperandContext> messages, ChartNodeService nodeService) {
        return condition.run((Void) -> {
            messages.forEach(m -> System.out.println(coloured("CONSOLE >> " + m.getText(), 154)));
            return CobolVmSignal.CONTINUE;
        });
    }

    @Override
    public CobolVmSignal executeMove(ChartNode moveChartNode, ChartNodeService nodeService) {
        return condition.run((Void) -> {
            MoveChartNode move = (MoveChartNode) moveChartNode;
            move.getTos().forEach(to -> System.out.println(coloured(String.format("%s was affected by %s", dataDescription(to, nodeService.getDataStructures()), move.getFrom().getText()), 227)));

            return CobolVmSignal.CONTINUE;
        });
    }

    @Override
    public CobolVmSignal executeAdd(ChartNode addChartNode, ChartNodeService nodeService) {
        return condition.run((Void) -> {
            AddChartNode add = (AddChartNode) addChartNode;
            add.getTos().forEach(to -> System.out.println(coloured(String.format("%s was affected by %s", dataDescription(to.generalIdentifier(), nodeService.getDataStructures()), datas(add.getFrom())), 227)));
            return CobolVmSignal.CONTINUE;
        });
    }

    private String datas(List<CobolParser.AddFromContext> from) {
        return String.join(" , ", from.stream().map(RuleContext::getText).toList());
    }

    private String dataDescription(CobolParser.GeneralIdentifierContext identifier, DataStructure dataStructures) {
        List<DataStructure> path = dataStructures.rootRecord(identifier);
        if (path.isEmpty()) return "[NOT FOUND] " + identifier.getText();
        return String.join(" > ", path.stream().map(DataStructure::toString).toList());
    }

    private CobolInterpreter locator(ChartNode specificLocation) {
//        return new SmolCobolInterpreter(new ExecuteAtTargetFlipCondition(specificLocation), runtimeStackFrames);
        return CobolInterpreterFactory.interpreter(new ExecuteAtTargetFlipCondition(specificLocation), runtimeStackFrames, conditionResolver);
    }

    private ChartNode actualDestination(ChartNode destination) {
        if (destination.getClass() == SectionChartNode.class) return destination;
        ParagraphChartNode paragraph = (ParagraphChartNode) destination;
        return paragraph.parentOrSelf();
    }
}
