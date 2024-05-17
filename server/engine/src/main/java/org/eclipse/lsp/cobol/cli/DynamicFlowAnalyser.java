package org.eclipse.lsp.cobol.cli;

import com.google.common.collect.ImmutableList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

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
        FlowNode flowNode = new BeginFlowNode();
        sentences.forEach(sentenceContext -> interpret(sentenceContext, flowNode));
    }

    private void interpret(CobolParser.SentenceContext sentenceContext, FlowNode precedingNode) {
        List<CobolParser.StatementContext> collectedStatements = sentenceContext.children.stream()
                .filter(s -> s instanceof CobolParser.StatementContext)
                .map(s -> (CobolParser.StatementContext) s)
                .collect(Collectors.toList());
        FlowNode sentenceRoot = processPath(collectedStatements, precedingNode);
//        for (int i = 0; i <= sentenceContext.getChildCount() - 1; i++) {
//            ParseTree child = sentenceContext.getChild(i);
//            if (child instanceof CobolParser.EndClauseContext) return;
//            CobolParser.StatementContext statement = (CobolParser.StatementContext) child;
//            ParseTree typedStatement = statement.getChild(0);
//            System.out.println(typedStatement.getClass().getSimpleName());
//            process(statement, precedingNode);
//        }
    }

    private FlowNode process(CobolParser.StatementContext statement, FlowNode precedingNode) {
        ParseTree typedStatement = statement.getChild(0);
        if (typedStatement.getClass() == CobolParser.IfStatementContext.class) {
            CobolParser.IfStatementContext ifStatement = (CobolParser.IfStatementContext) typedStatement;
            CobolParser.IfThenContext ifThen = ifStatement.ifThen();
            ConditionFlowNode conditionFlowNode = new ConditionFlowNode();
            FlowNode trueOutgoingPathRoot = processPath(collectStatements(ifThen), conditionFlowNode);
            conditionFlowNode.truePathRoot(trueOutgoingPathRoot);
            CobolParser.IfElseContext ifElse = ifStatement.ifElse();
            if (ifElse != null) {
                FlowNode falseOutgoingPathRoot = processPath(collectStatements(ifElse), conditionFlowNode);
                conditionFlowNode.falsePathRoot(falseOutgoingPathRoot);
            }
            conditionFlowNode.addIncomingNode(precedingNode);
            return conditionFlowNode;
        }

        FlowNode tail = new FlowNode();
        tail.addIncomingNode(precedingNode);
        return tail;
    }

    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfElseContext ifElse) {
        return ifElse.children.stream().map(conditionalCallContext -> (CobolParser.StatementContext) conditionalCallContext.getChild(0)).collect(Collectors.toList());
    }

    private List<CobolParser.StatementContext> collectStatements(CobolParser.IfThenContext ifThen) {
        return ifThen.children.stream().map(conditionalCallContext -> (CobolParser.StatementContext) conditionalCallContext.getChild(0)).collect(Collectors.toList());
    }

    private FlowNode processPath(List<CobolParser.StatementContext> statements, FlowNode precedingNode) {
        FlowNode tail = precedingNode;
        for (int i = 0; i <= statements.size() - 1; i++) {
            FlowNode statementTreeRoot = process(statements.get(i), tail);
            if (tail.canReturn()) {
                tail.addOutgoingNode(statementTreeRoot); // TODO : Connect correctly
            }
            if (statementTreeRoot.canReturn()) {
                tail = statementTreeRoot; // TODO : Connect correctly
            } else {
                tail = new DeadEndFlowNode();
            }
            // Revisit tail connection for IF-THEN, GOTOs, etc.
            tail.addOutgoingNode(statementTreeRoot); // This is not always going to be true, especially when GOTOs and EXITs terminate the branch
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
