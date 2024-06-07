package vm;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.common.navigation.CobolEntityNavigator;
import org.poc.common.navigation.EntityNavigatorBuilder;
import org.poc.common.navigation.ParseTreeSearchCondition;

public class CobolEntityNavigatorBuilderImpl implements EntityNavigatorBuilder {
    public static CobolParser.ProcedureDivisionBodyContext procedureDivisionBody;

    @Override
    public CobolEntityNavigator procedureDivisionEntityNavigator(CobolParser.ProcedureDivisionBodyContext procedureDivisionBody, CobolParser.DataDivisionContext dataDivisionBody, ParserRuleContext fullProgramTree) {
        return new CobolEntityNavigatorImpl(procedureDivisionBody, fullProgramTree);
    }

    @Override
    public CobolParser.ProcedureDivisionBodyContext procedureDivisionBody(ParseTree tree) {
        return (CobolParser.ProcedureDivisionBodyContext) findByConditionRecursive(tree, n -> n instanceof CobolParser.ProcedureDivisionBodyContext);
    }

    @Override
    public CobolParser.DataDivisionContext dataDivisionBody(ParseTree tree) {
        return (CobolParser.DataDivisionContext) findByConditionRecursive(tree, n -> n instanceof CobolParser.DataDivisionContext);
    }

    private ParseTree findByConditionRecursive(ParseTree currentNode, ParseTreeSearchCondition c) {
        if (c.apply(currentNode)) return currentNode;
        for (int i = 0; i <= currentNode.getChildCount() - 1; i++) {
            ParseTree searchResult = findByConditionRecursive(currentNode.getChild(i), c);
            if (searchResult != null) return searchResult;
        }

        return null;
    }

}
