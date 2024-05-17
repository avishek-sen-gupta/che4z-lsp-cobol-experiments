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
    private ParserRuleContext tree;

    public DynamicFlowAnalyser(ParserRuleContext tree) {
        this.tree = tree;
    }

    public void run() {
        ParseTree compilationUnit = tree.getChild(0);
        ParseTree programUnit = compilationUnit.getChild(0);
        ParseTree procedureDivision = programUnit.getChild(3);
        ParseTree procedureDivisionBody = procedureDivision.getChild(3);
        List<CobolParser.SentenceContext> sentences = new ArrayList<>();
        recurse(procedureDivisionBody, sentences);
        System.out.println("Got all Sentences in order");
        interpret(sentences);
    }

    private void interpret(List<CobolParser.SentenceContext> sentences) {
        FlowNode startNode = new BeginFlowNode();
        FlowNode tail = startNode;
        for (int i = 0; i <= sentences.size() - 1; i++) {
            FlowNode sentenceRoot = interpret(sentences.get(i), tail);
//            tail.setOutgoingNode(sentenceRoot); // This will not always be true, will need connect logic
//            sentenceRoot.addIncomingNode(tail);
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
//            conditionFlowNode.truePathRoot(trueOutgoingPathRoot);
            CobolParser.IfElseContext ifElse = ifStatement.ifElse();
            FlowNode falseOutgoingPathRoot = processPath(collectStatements(ifElse), conditionFlowNode.getFalseOutgoingPathRoot());
//            conditionFlowNode.falsePathRoot(falseOutgoingPathRoot);
//            precedingNode.setOutgoingNode(conditionFlowNode);
//            conditionFlowNode.addIncomingNode(precedingNode);
            return conditionFlowNode;
        }

        FlowNode tail = new FlowNode(statement.getText());
//        precedingNode.setOutgoingNode(tail);
//        tail.addIncomingNode(precedingNode);
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
//            if (statementTreeRoot.canReturn()) {
//                tail = statementTreeRoot; // TODO : Connect correctly
//            } else {
//                tail = new DeadEndFlowNode();
//            }
            // Revisit tail connection for IF-THEN, GOTOs, etc.
//            tail.setOutgoingNode(statementTreeRoot); // This is not always going to be true, especially when GOTOs and EXITs terminate the branch
            tail = statementTreeRoot; // This is not always going to be true, especially when GOTOs and EXITs terminate the branch
        }
        return tail;
    }

    private static void recurse(ParseTree parentNode, List<CobolParser.SentenceContext> sentences) {
        for (int i = 0; i <= parentNode.getChildCount() - 1; i++) {
            ParseTree child = parentNode.getChild(i);
            if (child.getClass() != CobolParser.SentenceContext.class) {
                recurse(child, sentences);
                continue;
            }
            sentences.add((CobolParser.SentenceContext) child);
        }
    }
}
