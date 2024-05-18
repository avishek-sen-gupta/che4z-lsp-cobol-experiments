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

    public void buildPipeline() {
        ParseTree compilationUnit = tree.getChild(0);
        ParseTree programUnit = compilationUnit.getChild(0);
        ParseTree procedureDivision = programUnit.getChild(3);
        ParserRuleContext procedureDivisionBody = (ParserRuleContext) procedureDivision.getChild(3);
        FlowUnit top = new FlowUnit(procedureDivisionBody);
        top.buildChildren(1);
        Stack<CobolFrame> stack = new Stack<>();
        FlowNavigator flowNavigator = new FlowNavigator(top.units());
        CobolFrame frame = new CobolFrame(flowNavigator, null, null);
        CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
        CobolVmInstruction runResult = vm2.run();
    }
    public void run() {
        ParseTree compilationUnit = tree.getChild(0);
        ParseTree programUnit = compilationUnit.getChild(0);
        ParseTree procedureDivision = programUnit.getChild(3);
        ParserRuleContext procedureDivisionBody = (ParserRuleContext) procedureDivision.getChild(3);
        CobolEntityNavigator navigator = new CobolEntityNavigator(tree);
        CobolEntityNavigatorFactory.procedureDivisionBody = procedureDivisionBody;

        Stack<CobolStackFrame> stack = new Stack<>();
        CobolStackFrame frame = new CobolStackFrame(navigator, procedureDivisionBody, procedureDivisionBody, 0);
        CobolVM vm = new CobolVM(frame, navigator, stack);
        InstructionPointerOperation instruction = new ZeroethInstruction(navigator);
        vm.run(instruction);
//        CobolParser.StatementContext s = vm.apply(instruction);
//
//        while (s != null) {
//            instruction = vm.interpret(s);
//            s = vm.apply(instruction);
//        }
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
