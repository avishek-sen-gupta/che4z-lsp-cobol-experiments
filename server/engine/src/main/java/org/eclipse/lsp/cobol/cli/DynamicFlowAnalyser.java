package org.eclipse.lsp.cobol.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicFlowAnalyser {
    private final CobolEntityNavigator navigator;
    private ParserRuleContext tree;
    private CobolVM vm;

    public DynamicFlowAnalyser(ParserRuleContext tree) {
        this.tree = tree;
        navigator = new CobolEntityNavigator(tree);
        vm = new CobolVM(navigator);
    }

    public void run() {
        interpret2();
        System.out.println("One instruction");
    }

    private void interpret2() {
        int instructionPointer = 0;
        CobolParser.StatementContext s1 = vm.apply(new ZeroethInstruction(navigator));
        InstructionPointerOperation iptrOp1 = vm.log(s1);
        CobolParser.StatementContext s2 = vm.apply(iptrOp1);
        InstructionPointerOperation iptrOp2 = vm.log(s2);
    }

    private void interpret(List<CobolParser.SentenceContext> sentences) {
        FlowNode startNode = new BeginFlowNode();
        FlowNode tail = startNode;
        for (int i = 0; i <= sentences.size() - 1; i++) {
            FlowNode sentenceRoot = interpret(sentences.get(i), tail);
            tail = sentenceRoot;
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String programFlow = gson.toJson(startNode);
        try {
            PrintWriter out = new PrintWriter("/Users/asgupta/Downloads/mbrdi-poc/control-flows.json");
            out.println(programFlow);
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private FlowNode interpret(CobolParser.SentenceContext sentenceContext, FlowNode precedingNode) {
        List<CobolParser.StatementContext> collectedStatements = sentenceContext.children.stream()
                .filter(s -> s instanceof CobolParser.StatementContext)
                .map(s -> (CobolParser.StatementContext) s)
                .collect(Collectors.toList());
        FlowNode sentenceRoot = processPath(collectedStatements, precedingNode);
        return sentenceRoot;
    }

    private FlowNode process(CobolParser.StatementContext statement, FlowNode precedingNode) {
        ParseTree typedStatement = statement.getChild(0);
        if (typedStatement.getClass() == CobolParser.IfStatementContext.class) {
            CobolParser.IfStatementContext ifStatement = (CobolParser.IfStatementContext) typedStatement;
            CobolParser.IfThenContext ifThen = ifStatement.ifThen();
            ConditionFlowNode conditionFlowNode = new ConditionFlowNode(ifStatement);
            FlowNode trueOutgoingPathRoot = processPath(collectStatements(ifThen), conditionFlowNode.getTrueOutgoingPathRoot());
            CobolParser.IfElseContext ifElse = ifStatement.ifElse();
            FlowNode falseOutgoingPathRoot = processPath(collectStatements(ifElse), conditionFlowNode.getFalseOutgoingPathRoot());
            return conditionFlowNode;
        }

        if (typedStatement.getClass() == CobolParser.GoToStatementContext.class) {
            CobolParser.GoToStatementContext gotoStatement = (CobolParser.GoToStatementContext) typedStatement;
            CobolParser.ProcedureNameContext procedureNameContext = gotoStatement.procedureName().get(0);
            ParseTree site = navigator.transferSite(procedureNameContext);
            List<CobolParser.SentenceContext> transferredToSentenceSequence = navigator.allSentencesFrom(site);
            System.out.println("It's a GO TO to: " + transferredToSentenceSequence.get(0).getText());
        }
        FlowNode tail = new FlowNode(statement.getText());
        return tail;
    }


    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfElseContext ifElse) {
        if (ifElse == null) return new ArrayList<>();
        return ifElse.conditionalStatementCall().stream().map(conditionalCallContext -> conditionalCallContext.statement()).collect(Collectors.toList());
    }

    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfThenContext ifThen) {
        return ifThen.conditionalStatementCall().stream().map(conditionalCallContext -> conditionalCallContext.statement()).collect(Collectors.toList());
    }

    private FlowNode processPath(List<CobolParser.StatementContext> statements, FlowNode precedingNode) {
        if (statements.isEmpty()) return new DeadEndFlowNode();
        FlowNode tail = precedingNode;
        for (int i = 0; i <= statements.size() - 1; i++) {
            FlowNode statementTreeRoot = process(statements.get(i), tail);
            tail.connectTo(statementTreeRoot);
            tail = statementTreeRoot; // This is not always going to be true, especially when GOTOs and EXITs terminate the branch
        }
        return tail;
    }

}
