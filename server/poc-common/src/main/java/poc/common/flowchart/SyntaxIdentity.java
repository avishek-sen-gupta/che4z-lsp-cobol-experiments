package poc.common.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

public class SyntaxIdentity<T> {
    private final ParseTree executionContext;

    public SyntaxIdentity(ParseTree executionContext) {
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

    public static boolean isClutter(ParseTree node) {
        if (node.getClass() == CobolParser.SentenceContext.class) {
            if (node.getChildCount() == 2) {
                ParseTree child = node.getChild(0);
                return !isStatementOfType(child, CobolParser.IfStatementContext.class) &&
                        !isStatementOfType(child, CobolParser.PerformStatementContext.class) &&
                        !isStatementOfType(child, CobolParser.DialectStatementContext.class) &&
                        !isStatementOfType(child, CobolParser.GoToStatementContext.class);
            }
        }
        return false;
    }

    public T get() {
        return (T) executionContext.getChild(0);
    }
}
