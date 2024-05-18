package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlowUnit {
    @Override
    public String toString() {
        return "FlowUnit{" +
                "executionContext=" + executionContext.getClass().getSimpleName() +
                '}';
    }

    public static FlowUnit TERMINAL = new FlowUnit(null);
    private ParseTree executionContext;
    private List<FlowUnit> children;

    public FlowUnit(ParseTree executionContext) {
        this.executionContext = executionContext;
        children = new ArrayList<>();
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
        return new PassThrough();
//        CobolParser.StatementContext statement = (CobolParser.StatementContext) executionContext;
//        ParseTree typedStatement = statement.getChild(0);
//        if (typedStatement.getClass() == CobolParser.ExitStatementContext.class) {
//            return new ExitScope();
//        }
//
//        return new GenericInstruction();
    }
}
