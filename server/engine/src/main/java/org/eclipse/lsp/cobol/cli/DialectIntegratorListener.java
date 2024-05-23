package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.core.CobolParserBaseListener;

public class DialectIntegratorListener extends CobolParserBaseListener {
    @Override
    public void enterDialectStatement(CobolParser.DialectStatementContext ctx) {
        super.enterDialectStatement(ctx);
    }
}
