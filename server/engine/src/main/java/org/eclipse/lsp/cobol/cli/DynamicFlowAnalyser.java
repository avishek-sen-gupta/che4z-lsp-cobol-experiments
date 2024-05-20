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

    public DynamicFlowAnalyser(ParserRuleContext tree) {
        this.tree = tree;
    }

    public void buildPipeline() {
        ParseTree compilationUnit = tree.getChild(0);
        ParseTree programUnit = compilationUnit.getChild(0);
        ParseTree procedureDivision = programUnit.getChild(3);
        ParserRuleContext procedureDivisionBody = (ParserRuleContext) procedureDivision.getChild(3);
        FlowUnit top = new FlowUnit(procedureDivisionBody);
        CobolEntityNavigatorFactory.procedureDivisionBody = procedureDivisionBody;
        CobolEntityNavigatorFactory.procedureDivisionFlowUnit = top;
        top.buildChildren();
        Stack<CobolFrame> stack = new Stack<>();
        FlowNavigator flowNavigator = new FlowNavigator(top.units());
        CobolFrame frame = new CobolFrame(flowNavigator, null, top);
        CobolVirtualMachine vm2 = new CobolVirtualMachine(frame, flowNavigator);
        CobolVmInstruction runResult = vm2.run();
    }

    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfElseContext ifElse) {
        if (ifElse == null) return new ArrayList<>();
        return ifElse.conditionalStatementCall().stream().map(conditionalCallContext -> conditionalCallContext.statement()).collect(Collectors.toList());
    }

    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfThenContext ifThen) {
        return ifThen.conditionalStatementCall().stream().map(conditionalCallContext -> conditionalCallContext.statement()).collect(Collectors.toList());
    }
}
