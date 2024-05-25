package vm;

import org.antlr.v4.runtime.tree.ParseTree;
import org.poc.common.navigation.CobolEntityNavigator;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.common.navigation.ParseTreeSearchCondition;

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
