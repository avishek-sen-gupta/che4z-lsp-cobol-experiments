package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class DynamicFlowAnalyser {
    private ParserRuleContext tree;
    private CobolVM vm;

    public DynamicFlowAnalyser(ParserRuleContext tree) {
        this.tree = tree;
    }

    public void run() {
        ParseTree compilationUnit = tree.getChild(0);
        ParseTree programUnit = compilationUnit.getChild(0);
        ParseTree procedureDivision = programUnit.getChild(3);
        ParserRuleContext procedureDivisionBody = (ParserRuleContext) procedureDivision.getChild(3);
        CobolEntityNavigator navigator = new CobolEntityNavigator(tree);
        CobolEntityNavigatorFactory.procedureDivisionBody = procedureDivisionBody;

        Stack<CobolStackFrame> stack = new Stack<>();
        CobolStackFrame frame1 = new CobolStackFrame(navigator, 0);
        CobolVM vm = new CobolVM(frame1, navigator, stack);

        InstructionPointerOperation instruction = new ZeroethInstruction(navigator);
        CobolParser.StatementContext s = vm.apply(instruction);

        while (s != null) {
            instruction = vm.interpret(s);
            s = vm.apply(instruction);
        }
        System.out.println("One instruction");
    }

    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfElseContext ifElse) {
        if (ifElse == null) return new ArrayList<>();
        return ifElse.conditionalStatementCall().stream().map(conditionalCallContext -> conditionalCallContext.statement()).collect(Collectors.toList());
    }

    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfThenContext ifThen) {
        return ifThen.conditionalStatementCall().stream().map(conditionalCallContext -> conditionalCallContext.statement()).collect(Collectors.toList());
    }
}
