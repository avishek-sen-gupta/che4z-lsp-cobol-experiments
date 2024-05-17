package org.eclipse.lsp.cobol.cli;

import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CobolVM {
    @Getter
    private final List<InstructionPointerOperation> stack;
    private CobolEntityNavigator navigator;
    private int instructionPointer;

    public CobolVM(CobolEntityNavigator navigator) {
        this.navigator = navigator;
        this.stack = new ArrayList<>();
        this.instructionPointer = 0;
    }

    public InstructionPointerOperation log(CobolParser.StatementContext statement) {
        InstructionPointerOperation ptrOp = interpret(statement);
        stack.add(ptrOp);
        return ptrOp;
    }

    public CobolParser.StatementContext read() {
        return navigator.instruction(instructionPointer);
    }
    public InstructionPointerOperation interpret(CobolParser.StatementContext statement) {
        ParseTree typedStatement = statement.getChild(0);
        if (typedStatement.getClass() == CobolParser.IfStatementContext.class) {
            CobolParser.IfStatementContext ifStatement = (CobolParser.IfStatementContext) typedStatement;
            CobolParser.IfThenContext ifThen = ifStatement.ifThen();
            CobolParser.IfElseContext ifElse = ifStatement.ifElse();
            List<CobolParser.StatementContext> thenClause = collectStatements(ifThen);
            List<CobolParser.StatementContext> elseClause = collectStatements(ifElse);

            return new InstructionPointerChange(statement, thenClause.get(0), navigator);
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
        InstructionContext next = op.next();
        instructionPointer = next.getStatementPointer();
        return navigator.instruction(instructionPointer);
    }
}
