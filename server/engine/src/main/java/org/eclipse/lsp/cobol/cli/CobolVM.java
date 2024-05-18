package org.eclipse.lsp.cobol.cli;

import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CobolVM {
    @Getter
    private final List<InstructionPointerOperation> log;
    private CobolEntityNavigator navigator;
    private CobolStackFrame frame;
    private Stack<CobolStackFrame> stack;

    public CobolVM(CobolStackFrame frame, CobolEntityNavigator navigator, Stack<CobolStackFrame> stack) {
        this.navigator = navigator;
        this.frame = frame;
        this.log = new ArrayList<>();
        this.stack = stack;
    }

    private void windStack() {
        this.stack.push(frame);
    }

    public InstructionPointerOperation interpret(CobolParser.StatementContext statement) {
        ParseTree typedStatement = statement.getChild(0);
        if (typedStatement.getClass() == CobolParser.IfStatementContext.class) {
            CobolParser.IfStatementContext ifStatement = (CobolParser.IfStatementContext) typedStatement;
            CobolParser.IfThenContext ifThen = ifStatement.ifThen();
            CobolParser.IfElseContext ifElse = ifStatement.ifElse();
            CobolEntityNavigator ifThenNavigator = new CobolEntityNavigator(ifThen);
            CobolStackFrame ifThenFrame = new CobolStackFrame(ifThenNavigator, statement, ifThen, 0);
            CobolVM ifThenVM = new CobolVM(ifThenFrame, ifThenNavigator, stack);

            InstructionPointerOperation currentInstruction = new ZeroethInstruction(ifThenNavigator);
            ifThenVM.run(currentInstruction);
//            CobolParser.StatementContext ifThenStmt = ifThenVM.apply(currentInstruction);
//            while (ifThenStmt != null) { // Need to allow the user to choose which branch here
//                currentInstruction = ifThenVM.interpretWithLog(ifThenStmt);
//                ifThenStmt = ifThenVM.apply(currentInstruction);
//            }
            log.add(new IfBranchInstruction(ifThenVM.log));
        }

        if (typedStatement.getClass() == CobolParser.GoToStatementContext.class) {
            // The logic below needs to move inside the instruction itself
            CobolEntityNavigator goToNavigator = CobolEntityNavigatorFactory.navigator();
            CobolParser.GoToStatementContext gotoStatement = (CobolParser.GoToStatementContext) typedStatement;
            System.out.println("Executing " + gotoStatement.getText() + " " + gotoStatement.start.getLine());
            CobolParser.ProcedureNameContext gotoTargetName = gotoStatement.procedureName().get(0);
            ParseTree gotoTarget = goToNavigator.findTarget(gotoTargetName);
            InstructionContext targetInstructionContext = goToNavigator.transferSiteContext(gotoTarget);
            CobolStackFrame goToFrame = new CobolStackFrame(goToNavigator, statement, gotoTargetName, targetInstructionContext.getStatementPointer());
            CobolVM goToVM = new CobolVM(goToFrame, goToNavigator, stack);
            InstructionPointerOperation currentInstruction = new GoToJmpInstruction(targetInstructionContext, goToNavigator);
            goToVM.run(currentInstruction);
//            CobolParser.StatementContext currentStatement = goToVM.apply(currentInstruction);
//            while (currentStatement != null) { // Need to allow the user to choose which branch here
//                currentInstruction = goToVM.interpretWithLog(currentStatement);
//                currentStatement = goToVM.apply(currentInstruction);
//            }
            log.add(currentInstruction);
        }

        if (typedStatement.getClass() == CobolParser.PerformStatementContext.class) {
            CobolEntityNavigator globalNavigator = CobolEntityNavigatorFactory.navigator();
            CobolParser.PerformStatementContext performStatement = (CobolParser.PerformStatementContext) typedStatement;
            CobolParser.ProcedureNameContext performTargetName = performStatement.performProcedureStatement().procedureName();
            ParserRuleContext procedure = globalNavigator.findTarget(performTargetName);
            CobolEntityNavigator procedureNavigator = new CobolEntityNavigator(procedure);
//            InstructionContext targetInstructionContext = globalNavigator.transferSiteContext(procedure);
            CobolStackFrame goToFrame = new CobolStackFrame(procedureNavigator, statement, procedure);
            CobolVM performVM = new CobolVM(goToFrame, procedureNavigator, stack);
            ZeroethInstruction firstInstruction = new ZeroethInstruction(navigator);
            performVM.run(firstInstruction);
            log.add(firstInstruction);
        }

        if (typedStatement.getClass() == CobolParser.ExitStatementContext.class) {
            CobolEntityNavigator performNavigator = CobolEntityNavigatorFactory.navigator();
            CobolParser.ExitStatementContext exitStatement = (CobolParser.ExitStatementContext) typedStatement;

//            log.add(firstInstruction);
        }
        return new NextInstruction(statement, navigator);
    }

    public CobolStackFrame run(InstructionPointerOperation firstInstruction) {
        windStack();
        InstructionPointerOperation instruction = firstInstruction;
        CobolParser.StatementContext s = apply(instruction);

        while (s != null) {
            instruction = interpret(s);
            s = apply(instruction);
        }
        return unwindStack();
    }

    private CobolStackFrame unwindStack() {
        return stack.pop();
    }
    //    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfElseContext ifElse) {
//        if (ifElse == null) return new ArrayList<>();
//        return ifElse.conditionalStatementCall().stream().map(conditionalCallContext -> conditionalCallContext.statement()).collect(Collectors.toList());
//    }
//
//    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfThenContext ifThen) {
//        return ifThen.conditionalStatementCall().stream().map(conditionalCallContext -> conditionalCallContext.statement()).collect(Collectors.toList());
//    }

    public CobolParser.StatementContext apply(InstructionPointerOperation op) {
        log.add(op);
        InstructionContext next = op.nextContext();
        frame.setInstructionPointer(next.getStatementPointer());
        if (next == InstructionContext.TERM) return null;
        return navigator.instruction(frame.getInstructionPointer());
    }
}
