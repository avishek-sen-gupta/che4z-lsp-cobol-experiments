package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.eclipse.lsp.cobol.core.CobolLexer;
import org.eclipse.lsp.cobol.core.CobolParser;

public class IdmsContainerNode extends TerminalNodeImpl {
    private ParseTree dialectNode;

    public IdmsContainerNode(ParseTree dialectNode) {
        super(new CommonToken(CobolLexer.COMPUTATIONAL, "IDMS Container"));
        this.dialectNode = dialectNode;
    }

    @Override
    public int getChildCount() {
        return 1;
    }

    @Override
    public ParseTree getChild(int i) {
        return dialectNode;
    }
}
