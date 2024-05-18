package org.eclipse.lsp.cobol.cli;

import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CobolVM {
    @Getter
    private final List<InstructionPointerOperation> log;
    private CobolEntityNavigator navigator;
    private CobolStackFrame frame;

    public CobolVM(CobolStackFrame frame, CobolEntityNavigator navigator) {
        this.navigator = navigator;
        this.frame = frame;
        this.log = new ArrayList<>();
    }

    public InstructionPointerOperation log(CobolParser.StatementContext statement) {
        InstructionPointerOperation ptrOp = interpret(statement);
        return ptrOp;
    }

    public InstructionPointerOperation interpret(CobolParser.StatementContext statement) {
        ParseTree typedStatement = statement.getChild(0);
        if (typedStatement.getClass() == CobolParser.IfStatementContext.class) {
            CobolParser.IfStatementContext ifStatement = (CobolParser.IfStatementContext) typedStatement;
            CobolParser.IfThenContext ifThen = ifStatement.ifThen();
            CobolParser.IfElseContext ifElse = ifStatement.ifElse();
            CobolEntityNavigator ifThenNavigator = new CobolEntityNavigator(ifThen);
            CobolStackFrame ifThenFrame = new CobolStackFrame(ifThenNavigator);
            CobolVM ifThenVM = new CobolVM(ifThenFrame, ifThenNavigator);

            InstructionPointerOperation currentInstruction = new ZeroethInstruction(ifThenNavigator);
            CobolParser.StatementContext ifThenStmt = ifThenVM.apply(currentInstruction);
            while (ifThenStmt != null) { // Need to allow the user to choose which branch here
                currentInstruction = ifThenVM.log(ifThenStmt);
                ifThenStmt = ifThenVM.apply(currentInstruction);
            }
            log.add(new IfBranchInstruction(ifThenVM.log));
        }
        return new NextInstruction(statement, navigator);
    }

    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfElseContext ifElse) {
        if (ifElse == null) return new ArrayList<>();
        return ifElse.conditionalStatementCall().stream().map(conditionalCallContext -> conditionalCallContext.statement()).collect(Collectors.toList());
    }

    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfThenContext ifThen) {
        return ifThen.conditionalStatementCall().stream().map(conditionalCallContext -> conditionalCallContext.statement()).collect(Collectors.toList());
    }

    public CobolParser.StatementContext apply(InstructionPointerOperation op) {
        log.add(op);
        InstructionContext next = op.nextContext();
        frame.setInstructionPointer(next.getStatementPointer());
        if (next == InstructionContext.TERM) return null;
        return navigator.instruction(frame.getInstructionPointer());
    }
}
