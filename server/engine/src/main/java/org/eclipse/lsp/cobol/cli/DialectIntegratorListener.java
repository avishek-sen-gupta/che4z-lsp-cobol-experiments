package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.common.poc.PersistentData;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.core.CobolParserBaseListener;

public class DialectIntegratorListener extends CobolParserBaseListener {
    @Override
    public void enterDialectNodeFiller(CobolParser.DialectNodeFillerContext ctx) {
        super.enterDialectNodeFiller(ctx);
        String guid = ctx.dialectGuid().getText();
        ParseTree dialectNode = PersistentData.getDialectNode("IDMS-" + guid);
        ctx.addChild(new IdmsContainerNode(dialectNode));
    }
}
