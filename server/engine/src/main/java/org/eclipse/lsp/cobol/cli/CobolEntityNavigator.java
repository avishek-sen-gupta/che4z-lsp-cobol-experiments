package org.eclipse.lsp.cobol.cli;

import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;

public class CobolEntityNavigator {

    @Getter
    private final List<CobolParser.SentenceContext> sentences;
    private final List<CobolParser.ProcedureSectionContext> sections;
    private final List<CobolParser.ParagraphContext> paragraphs;
    private final List<CobolParser.StatementContext> statements;
    private ParserRuleContext tree;

    public CobolEntityNavigator(ParserRuleContext tree) {
        this.tree = tree;
        sentences = new ArrayList<>();
        sections = new ArrayList<>();
        paragraphs = new ArrayList<>();
        statements = new ArrayList<>();
        new CobolEntityFinder<>(CobolParser.StatementContext.class).recurse(tree, statements);
//        new CobolEntityFinder<CobolParser.SentenceContext>().recurse(procedureDivisionBody, sentences);
//        new CobolEntityFinder<CobolParser.ProcedureSectionContext>().recurse(procedureDivisionBody, sections);
//        new CobolEntityFinder<CobolParser.ParagraphContext>().recurse(procedureDivisionBody, paragraphs);
    }

    public InstructionContext transferSiteContext(ParseTree procedure) {
        CobolParser.StatementContext firstStatement = recursivelyFindTransferSite(procedure);
        return context(firstStatement);

    }

    private CobolParser.StatementContext recursivelyFindTransferSite(ParseTree currentNode) {
        if (currentNode.getClass() == CobolParser.StatementContext.class) return (CobolParser.StatementContext) currentNode;
        for (int i = 0; i <= currentNode.getChildCount() - 1; i++) {
            ParseTree maybeStatement = recursivelyFindTransferSite(currentNode.getChild(i));
            if (maybeStatement != null) return (CobolParser.StatementContext) maybeStatement;
        }
        return null;
    }

    public ParserRuleContext findTargetRecursive(CobolParser.ProcedureNameContext procedureNameContext, ParseTree currentNode) {
        if (currentNode instanceof CobolParser.ParagraphContext) {
            if (((CobolParser.ParagraphContext) currentNode).paragraphDefinitionName().getText().equals(procedureNameContext.getText()))
                return (CobolParser.ParagraphContext) currentNode;
        } else if (currentNode instanceof CobolParser.ProcedureSectionContext) {
            if (((CobolParser.ProcedureSectionContext) currentNode).procedureSectionHeader().sectionName().getText().equals(procedureNameContext.getText()))
                return (CobolParser.ProcedureSectionContext) currentNode;
        }

        for (int i = 0; i <= currentNode.getChildCount() - 1; i++) {
            ParserRuleContext searchResult = findTargetRecursive(procedureNameContext, currentNode.getChild(i));
            if (searchResult != null) return searchResult;
        }

        return null;
    }

    public List<CobolParser.SentenceContext> allSentencesFrom(ParseTree site) {
        CobolParser.SentenceContext firstSentence =  firstSentenceIn(site);
        int indexOfFirstSentence = sentences.indexOf(firstSentence);
        return sentences.subList(indexOfFirstSentence, sentences.size());
    }

    private CobolParser.SentenceContext firstSentenceIn(ParseTree site) {
        List<CobolParser.SentenceContext> statements = new ArrayList<>();
        new CobolEntityFinder<>(CobolParser.SentenceContext.class).recurse(site, statements);
        return statements.get(0);
    }

    public InstructionContext context(CobolParser.StatementContext statementContext) {
        if (statementContext == null) {
            System.out.println("WHY AGAIN?");
        }
        int statementPointer = statements.indexOf(statementContext);
        LocationContext context = new CobolEntityFinder<>(CobolParser.StatementContext.class).context(statementContext);
        return new InstructionContext(statementPointer, context);
    }

    public InstructionContext next(InstructionContext currentContext) {
        if (currentContext.getStatementPointer() + 1 >= statements.size()) return InstructionContext.TERM;
        return context(statements.get(currentContext.getStatementPointer() + 1));
    }

    public CobolParser.StatementContext instruction(int instructionPointer) {
        if (statements.isEmpty()) return null;
        return statements.get(instructionPointer);
    }

    public ParserRuleContext findTarget(CobolParser.ProcedureNameContext gotoTargetName) {
        return findTargetRecursive(gotoTargetName, tree);
    }
}
