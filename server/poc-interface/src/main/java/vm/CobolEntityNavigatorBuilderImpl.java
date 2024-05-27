package vm;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.common.navigation.CobolEntityNavigator;
import org.poc.common.navigation.EntityNavigatorBuilder;

public class CobolEntityNavigatorBuilderImpl implements EntityNavigatorBuilder {
    public static CobolParser.ProcedureDivisionBodyContext procedureDivisionBody;

    @Override
    public CobolEntityNavigator procedureDivisionEntityNavigator(CobolParser.ProcedureDivisionBodyContext procedureDivisionBody, ParserRuleContext fullProgramTree) {
        return new CobolEntityNavigatorImpl(procedureDivisionBody, fullProgramTree);
    }

    @Override
    public CobolParser.ProcedureDivisionBodyContext procedureDivisionBody(ParseTree tree) {
        if (tree instanceof CobolParser.ProcedureDivisionBodyContext) return (CobolParser.ProcedureDivisionBodyContext) tree;
        for (int i = 0; i < tree.getChildCount(); i++) {
            CobolParser.ProcedureDivisionBodyContext result = procedureDivisionBody(tree.getChild(i));
            if (result != null) return result;
        }
        return null;
    }
}
