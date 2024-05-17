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
    private final ParseTree procedureDivisionBody;
    private final List<CobolParser.StatementContext> statements;

    public CobolEntityNavigator(ParserRuleContext tree) {
        ParseTree compilationUnit = tree.getChild(0);
        ParseTree programUnit = compilationUnit.getChild(0);
        ParseTree procedureDivision = programUnit.getChild(3);
        procedureDivisionBody = procedureDivision.getChild(3);
        sentences = new ArrayList<>();
        sections = new ArrayList<>();
        paragraphs = new ArrayList<>();
        statements = new ArrayList<>();
        new CobolEntityFinder<>(CobolParser.StatementContext.class).recurse(procedureDivisionBody, statements);
//        new CobolEntityFinder<CobolParser.SentenceContext>().recurse(procedureDivisionBody, sentences);
//        new CobolEntityFinder<CobolParser.ProcedureSectionContext>().recurse(procedureDivisionBody, sections);
//        new CobolEntityFinder<CobolParser.ParagraphContext>().recurse(procedureDivisionBody, paragraphs);
    }

    public ParseTree transferSite(CobolParser.ProcedureNameContext procedureNameContext) {
        return recursivelySearch(procedureDivisionBody, procedureNameContext);
    }

    private ParseTree recursivelySearch(ParseTree parent, CobolParser.ProcedureNameContext procedureNameContext) {
        if (parent instanceof CobolParser.ParagraphContext) {
            if (((CobolParser.ParagraphContext) parent).paragraphDefinitionName().getText().equals(procedureNameContext.getText()))
                return parent;
        } else if (parent instanceof CobolParser.ProcedureSectionContext) {
            if (((CobolParser.ProcedureSectionContext) parent).procedureSectionHeader().sectionName().getText().equals(procedureNameContext.getText()))
                return parent;
        }

        for (int i = 0; i <= parent.getChildCount() - 1; i++) {
            ParseTree searchResult = recursivelySearch(parent.getChild(i), procedureNameContext);
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
        new CobolEntityFinder<CobolParser.SentenceContext>(CobolParser.SentenceContext.class).recurse(site, statements);
        return statements.get(0);
    }

    public InstructionContext context(CobolParser.StatementContext statementContext) {
        int statementPointer = statements.indexOf(statementContext);
        LocationContext context = new CobolEntityFinder<CobolParser.StatementContext>(CobolParser.StatementContext.class).context(statementContext);
        return new InstructionContext(statementPointer, context);
    }

    public InstructionContext next(InstructionContext currentContext) {
        return context(statements.get(currentContext.getStatementPointer() + 1));
    }

    public CobolParser.StatementContext instruction(int instructionPointer) {
        return statements.get(instructionPointer);
    }
}
