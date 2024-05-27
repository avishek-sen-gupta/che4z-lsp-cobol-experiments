package vm;

import com.google.common.collect.ImmutableList;
import org.antlr.v4.runtime.tree.ParseTree;
import org.poc.common.navigation.CobolEntityNavigator;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.common.navigation.ParseTreeSearchCondition;
import org.poc.flowchart.StatementIdentity;

import java.util.ArrayList;
import java.util.List;

public class CobolEntityNavigatorImpl implements CobolEntityNavigator {
    private CobolParser.ProcedureDivisionBodyContext root;

    public CobolEntityNavigatorImpl(CobolParser.ProcedureDivisionBodyContext procedureDivisionBody) {
        this.root = procedureDivisionBody;
    }

    @Override
    public ParseTree target(String procedureName) {
        return findTargetRecursive(procedureName, root);
    }

    @Override
    public ParseTree root() {
        return root;
    }

    @Override
    public ParseTree findByCondition(ParseTree searchRoot, ParseTreeSearchCondition c, int maxLevel) {
        return findByConditionRecursive(searchRoot, c, 1, maxLevel);
    }

    @Override
    public ParseTree findByCondition(ParseTree searchRoot, ParseTreeSearchCondition c) {
        return findByConditionRecursive(searchRoot, c, 1, -1);
    }

    @Override
    public List<ParseTree> statementsContaining(String symbol, ParseTree scope) {
        List<ParseTree> trees = new ArrayList<>();
        findAllByConditionRecursive(scope, trees, n ->
//                !StatementIdentity.isOneOfTypes(n, ImmutableList.of(CobolParser.ParagraphContext.class, CobolParser.ProcedureSectionContext.class)) &&
                StatementIdentity.isOfType(n, CobolParser.StatementContext.class) &&
                        n.getText().contains(symbol), 1, -1);
        return trees;
    }

    private ParseTree findByConditionRecursive(ParseTree currentNode, ParseTreeSearchCondition c, int level, int maxLevel) {
        if (c.apply(currentNode)) {
            if (c.apply(currentNode)) return currentNode;
        }
        if (maxLevel != -1 && level > maxLevel) return null;
        for (int i = 0; i <= currentNode.getChildCount() - 1; i++) {
            ParseTree searchResult = findByConditionRecursive(currentNode.getChild(i), c, level + 1, maxLevel);
            if (searchResult != null) return searchResult;
        }

        return null;
    }

    private void findAllByConditionRecursive(ParseTree currentNode, List<ParseTree> matchedTrees, ParseTreeSearchCondition c, int level, int maxLevel) {
        if (c.apply(currentNode)) {
            matchedTrees.add(currentNode);
        }
        if (maxLevel != -1 && level > maxLevel) return;
        for (int i = 0; i <= currentNode.getChildCount() - 1; i++) {
            findAllByConditionRecursive(currentNode.getChild(i), matchedTrees, c, level + 1, maxLevel);
        }
    }

    public ParseTree findTargetRecursive(String procedureName, ParseTree currentNode) {
        if (currentNode.getClass() == CobolParser.ParagraphContext.class) {
            String name = ((CobolParser.ParagraphContext) currentNode).paragraphDefinitionName().getText();
            if (name.equals(procedureName)) return currentNode;
        } else if (currentNode.getClass() == CobolParser.ProcedureSectionContext.class) {
            String name = ((CobolParser.ProcedureSectionContext) currentNode).procedureSectionHeader().sectionName().getText();
            if (name.equals(procedureName)) return currentNode;
        }
        for (int i = 0; i <= currentNode.getChildCount() - 1; i++) {
            ParseTree searchResult = findTargetRecursive(procedureName, currentNode.getChild(i));
            if (searchResult != null) return searchResult;
        }

        return null;
    }
}
