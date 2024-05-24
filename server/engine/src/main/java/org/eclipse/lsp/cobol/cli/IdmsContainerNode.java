package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolLexer;
import org.eclipse.lsp.cobol.core.CobolParser;

public class IdmsContainerNode extends ParserRuleContext {
    private ParseTree dialectNode;

    public IdmsContainerNode(ParseTree dialectNode, CobolParser.DialectNodeFillerContext parent) {
        super(parent, 1);
        this.dialectNode = dialectNode;
        addAnyChild(dialectNode);
    }

    @Override
    public int getChildCount() {
        return 1;
    }

    @Override
    public ParseTree getChild(int i) {
        return dialectNode;
    }

    @Override
    public String getText() {
        return dialectNode.getText();
    }

    @Override
    public Token getStart() {
        return new CommonToken(CobolLexer.COMPUTATIONAL, getText());
    }

    @Override
    public Token getStop() {
        return new CommonToken(CobolLexer.COMPUTATIONAL, getText());
    }
}
