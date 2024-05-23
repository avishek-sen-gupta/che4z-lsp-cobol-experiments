package org.eclipse.lsp.cobol.cli;

import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.common.poc.PersistentData;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.core.CobolParserBaseListener;

public class DialectIntegratorListener extends CobolParserBaseListener {
    @Getter
    private int restores = 0;

    @Override
    public void enterDialectNodeFiller(CobolParser.DialectNodeFillerContext ctx) {
        super.enterDialectNodeFiller(ctx);
        String guid = ctx.dialectGuid().getText();
        ParseTree dialectNode = PersistentData.getDialectNode("IDMS-" + guid);
        System.out.println(String.format("Restoring _DIALECT_ %s: %s", guid, dialectNode.getText()));
        ctx.addChild(new IdmsContainerNode(dialectNode));
        restores ++;
    }
}
