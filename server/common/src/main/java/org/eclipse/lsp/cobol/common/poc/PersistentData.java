package org.eclipse.lsp.cobol.common.poc;

public class PersistentData {
    private static AnnotatedParserRuleContext tree;

    public static void setResult(AnnotatedParserRuleContext tree) {
        PersistentData.tree = tree;
    }
}
