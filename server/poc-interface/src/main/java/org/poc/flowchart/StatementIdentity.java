package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

public class StatementIdentity<T> {
    private final ParseTree executionContext;

    public StatementIdentity(ParseTree executionContext) {
        this.executionContext = executionContext;
    }

    public static boolean is(ParseTree parseTree, Class clazz) {
        if (parseTree.getClass() != CobolParser.StatementContext.class) return false;
        CobolParser.StatementContext statement = (CobolParser.StatementContext) parseTree;
        return statement.getChild(0).getClass() == clazz;
    }

    public T get() {
        return (T) executionContext.getChild(0);
    }
}
