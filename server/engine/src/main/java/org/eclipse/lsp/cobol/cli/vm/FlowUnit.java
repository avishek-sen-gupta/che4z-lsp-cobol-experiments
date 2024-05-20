package org.eclipse.lsp.cobol.cli.vm;

import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO: Break this up
public class FlowUnit {
    public static FlowUnit TERMINAL = new FlowUnit(null);
    @Getter
    private ParseTree executionContext;
    @Getter
    private List<FlowUnit> children;
    @Getter
    private FlowUnit parent;

    public String executionContextName() {
        if (executionContext.getClass() == CobolParser.ProcedureSectionContext.class)
            return ((CobolParser.ProcedureSectionContext) executionContext).procedureSectionHeader().sectionName().getText();
        if (executionContext.getClass() == CobolParser.ParagraphContext.class)
            return ((CobolParser.ParagraphContext) executionContext).paragraphDefinitionName().getText();
        if (executionContext.getClass() == CobolParser.StatementContext.class)
            return ((CobolParser.StatementContext) executionContext).getText();
        if (executionContext.getClass() == CobolParser.SentenceContext.class)
            return "" + ((CobolParser.SentenceContext) executionContext).start.getLine();
        return "";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "executionContext=" + executionContext.getClass().getSimpleName() + "} = " + executionContextName();
    }

    public FlowUnit(ParseTree executionContext) {
        this.executionContext = executionContext;
        children = new ArrayList<>();
//        if (executionContext != null && executionContext.getClass() == CobolParser.StatementContext.class
//                && ((CobolParser.StatementContext) executionContext).children.get(0).getClass() == CobolParser.CallStatementContext.class) {
//            System.out.println("Hello Call");
//        }
    }

    public ProgramScope scope() {
        if (executionContext.getClass() == CobolParser.ProcedureSectionContext.class) return ProgramScope.SECTION;
        if (executionContext.getClass() == CobolParser.ParagraphContext.class) return ProgramScope.PARAGRAPH;
        if (executionContext.getClass() == CobolParser.StatementContext.class)
            return statementType((CobolParser.StatementContext) executionContext);
        if (executionContext.getClass() == CobolParser.SentenceContext.class) return ProgramScope.SENTENCE;
        return ProgramScope.UNKNOWN;
    }

    private ProgramScope statementType(CobolParser.StatementContext statement) {
        if (statement.children.get(0).getClass() == CobolParser.PerformStatementContext.class) {
            return ProgramScope.PERFORM;
        }
        return ProgramScope.ATOMIC_STATEMENT;
    }

    public static FlowUnit statementFlowUnit(CobolParser.StatementContext s) {
        ParseTree typedStatement = s.children.get(0);
        if (typedStatement.getClass() == CobolParser.IfStatementContext.class) {
            FlowUnit ifFlowUnit = new IfFlowUnit(s);
            ifFlowUnit.buildChildren();
            return ifFlowUnit;
        } else if (typedStatement.getClass() == CobolParser.GoToStatementContext.class) {
            return new GoToFlowUnit(s);
        }
        return new FlowUnit(s);
    }

    public void buildChildren() {
        if (executionContext.getClass() == CobolParser.StatementContext.class) {
            CobolParser.StatementContext statement = (CobolParser.StatementContext) executionContext;
            FlowUnit e = statementFlowUnit(statement);
            addChild(e);
            return;
        }
        for (int i = 0; i <= executionContext.getChildCount() - 1; i++) {
            ParseTree child = executionContext.getChild(i);
            // Unwrap ParagraphsContext to get at ParagraphContext objects
            if (child.getClass() == CobolParser.ParagraphsContext.class) {
                // Some paragraphs can be empty
                if (((CobolParser.ParagraphsContext) child).children == null) continue;
                List<FlowUnit> paragraphs = ((ParserRuleContext) child).children.stream().map(FlowUnit::new).collect(Collectors.toList());
                addChildren(paragraphs);
            } else if (child.getClass() == CobolParser.StatementContext.class) {
                CobolParser.StatementContext statement = (CobolParser.StatementContext) child;
                FlowUnit e = statementFlowUnit(statement);
                addChild(e);
            } else if (child.getClass() == CobolParser.ParagraphContext.class
                    || child.getClass() == CobolParser.ProcedureSectionContext.class
                    || child.getClass() == CobolParser.SentenceContext.class) {
                FlowUnit e = new FlowUnit(child);
                addChild(e);
            }
        }
        children.forEach(c -> c.buildChildren());
    }

    private void addChildren(List<FlowUnit> children) {
        children.forEach(this::addChild);
    }

    private void addChild(FlowUnit f) {
        children.add(f);
        f.setParent(this);
    }

    private void setParent(FlowUnit parent) {
        this.parent = parent;
    }

    public List<FlowUnit> units() {
        return children;
    }

    public boolean isAtomic() {
        return executionContext.getClass() == CobolParser.StatementContext.class
                && statementType((CobolParser.StatementContext) executionContext) == ProgramScope.ATOMIC_STATEMENT;
    }

    public CobolVmInstruction instruction(CobolFrame frame) {
        CobolParser.StatementContext statement = (CobolParser.StatementContext) executionContext;
        ParseTree typedStatement = statement.getChild(0);
        if (typedStatement.getClass() == CobolParser.ExitStatementContext.class) {
            if (frame.isProcedure()) {
                System.out.println("Encountered PERFORM EXIT at " + ((CobolParser.ExitStatementContext) typedStatement).start.getLine());
                return new ExitProcedure();
            } else {
                System.out.println("Encountered EXIT at " + ((CobolParser.ExitStatementContext) typedStatement).start.getLine());
                return new ExitScope();
            }
        }
        return new PassThrough();
    }
}
