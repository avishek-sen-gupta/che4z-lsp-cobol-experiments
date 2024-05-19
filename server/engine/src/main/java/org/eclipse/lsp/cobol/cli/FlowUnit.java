package org.eclipse.lsp.cobol.cli;

import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlowUnit {
    private String executionContextName() {
        if (executionContext.getClass() == CobolParser.ProcedureSectionContext.class) return ((CobolParser.ProcedureSectionContext) executionContext).procedureSectionHeader().sectionName().getText();
        if (executionContext.getClass() == CobolParser.ParagraphContext.class) return ((CobolParser.ParagraphContext) executionContext).paragraphDefinitionName().getText();
        if (executionContext.getClass() == CobolParser.StatementContext.class) return ((CobolParser.StatementContext) executionContext).getText();
        if (executionContext.getClass() == CobolParser.SentenceContext.class) return "" + ((CobolParser.SentenceContext) executionContext).start.getLine();
        return "";
    }
    @Override
    public String toString() {
        return "FlowUnit{" +
                "executionContext=" + executionContext.getClass().getSimpleName() +  "} = " + executionContextName();
    }

    public static FlowUnit TERMINAL = new FlowUnit(null);
    @Getter private ParseTree executionContext;
    private List<FlowUnit> children;

    public FlowUnit(ParseTree executionContext) {
        this.executionContext = executionContext;
        children = new ArrayList<>();
    }

    public ProgramScope scope() {
        if (executionContext.getClass() == CobolParser.ProcedureSectionContext.class) return ProgramScope.SECTION;
        if (executionContext.getClass() == CobolParser.ParagraphContext.class) return ProgramScope.PARAGRAPH;
        if (executionContext.getClass() == CobolParser.StatementContext.class) return ProgramScope.STATEMENT;
        if (executionContext.getClass() == CobolParser.SentenceContext.class) return ProgramScope.SENTENCE;
        return ProgramScope.UNKNOWN;
    }
    public void buildChildren(int level) {
        if (level > 4) return;
        if (executionContext.getClass() == CobolParser.StatementContext.class) return;
        for (int i = 0; i <= executionContext.getChildCount() - 1; i++) {
            ParseTree child = executionContext.getChild(i);
            if (child.getClass() == CobolParser.ParagraphsContext.class) {
                if (((CobolParser.ParagraphsContext) child).children == null) continue;
                List<FlowUnit> paragraphs = ((ParserRuleContext) child).children.stream().map(FlowUnit::new).collect(Collectors.toList());
                children.addAll(paragraphs);
                continue;
            }
            if (child.getClass() != CobolParser.ParagraphContext.class
                && child.getClass() != CobolParser.ProcedureSectionContext.class
                && child.getClass() != CobolParser.SentenceContext.class
                && child.getClass() != CobolParser.StatementContext.class) {
                continue;
            }
            children.add(new FlowUnit(child));
        }
        children.forEach(c -> c.buildChildren(level + 1));
    }

    public void X(int level) {
    }

    public List<FlowUnit> units() {
        return children;
    }

    public boolean isAtomic() {
        return executionContext.getClass() == CobolParser.StatementContext.class;
    }

    public CobolVmInstruction instruction() {
        CobolParser.StatementContext statement = (CobolParser.StatementContext) executionContext;
        ParseTree typedStatement = statement.getChild(0);
        if (typedStatement.getClass() == CobolParser.ExitStatementContext.class) {
            System.out.println("Excountered EXIT at " + ((CobolParser.ExitStatementContext) typedStatement).start.getLine());
            return new ExitScope();
        }
        return new PassThrough();
    }
}
