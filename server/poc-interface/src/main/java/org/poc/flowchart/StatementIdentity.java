package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

public class StatementIdentity<T> {
    private final ParseTree executionContext;

    public StatementIdentity(ParseTree executionContext) {
        this.executionContext = executionContext;
    }

    public static boolean isStatementOfType(ParseTree parseTree, Class clazz) {
        if (parseTree.getClass() != CobolParser.StatementContext.class) return false;
        CobolParser.StatementContext statement = (CobolParser.StatementContext) parseTree;
        return statement.getChild(0).getClass() == clazz;
    }

    public static boolean isOfType(ParseTree parseTree, Class clazz) {
        return parseTree.getClass() == clazz;
    }

    public static boolean isOneOfTypes(ParseTree parseTree, List<Class> clazzes) {
        return clazzes.contains(parseTree.getClass());
    }

    public T get() {
        return (T) executionContext.getChild(0);
    }
}
