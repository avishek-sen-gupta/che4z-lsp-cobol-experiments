package org.eclipse.lsp.cobol.cli.vm;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

public class CobolEntityNavigator {
    private CobolParser.ProcedureDivisionBodyContext root;

    public CobolEntityNavigator(CobolParser.ProcedureDivisionBodyContext procedureDivisionBody) {
        this.root = procedureDivisionBody;
    }

    public ParseTree findTarget(String procedureName) {
        return findTargetRecursive(procedureName, root);
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
