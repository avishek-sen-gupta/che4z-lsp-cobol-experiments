package org.eclipse.lsp.cobol.common.poc;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PersistentData {
    public static int counter = 0;

    public static String next() {
        counter ++;
        return String.valueOf(counter);
    }
    private static AnnotatedParserRuleContext tree;

    public static void setResult(AnnotatedParserRuleContext tree) {
        PersistentData.tree = tree;
    }

    public static ParseTree getDialectNode(String displayOperand) {
        return getDialectNode(displayOperand, tree);
    }

    public static ParseTree getDialectNode(String displayOperand, AnnotatedParserRuleContext node) {
        if (node.customData.get(displayOperand) != null)
            return node;

        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChild(i) instanceof TerminalNode) continue;
            ParseTree result = getDialectNode(displayOperand, (AnnotatedParserRuleContext) node.getChild(i));
            if (result != null) return result;
        }

        return null;
    }
}
