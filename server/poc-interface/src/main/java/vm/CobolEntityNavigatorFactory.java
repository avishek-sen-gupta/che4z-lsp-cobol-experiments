package vm;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

public class CobolEntityNavigatorFactory {
    public static CobolParser.ProcedureDivisionBodyContext procedureDivisionBody;
    public static FlowUnit procedureDivisionFlowUnit;

    public static GlobalFlowUnitNavigator flowUnitnavigator() {
        return new GlobalFlowUnitNavigator(procedureDivisionFlowUnit);
    }
    public static CobolEntityNavigatorImpl procedureDivisionEntityNavigator(CobolParser.ProcedureDivisionBodyContext procedureDivisionBody) {
        return new CobolEntityNavigatorImpl(procedureDivisionBody);
    }

    public static CobolParser.ProcedureDivisionBodyContext procedureDivisionBody(ParseTree tree) {
        if (tree instanceof CobolParser.ProcedureDivisionBodyContext) return (CobolParser.ProcedureDivisionBodyContext) tree;
        for (int i = 0; i < tree.getChildCount(); i++) {
            CobolParser.ProcedureDivisionBodyContext result = procedureDivisionBody(tree.getChild(i));
            if (result != null) return result;
        }
        return null;
    }
}
