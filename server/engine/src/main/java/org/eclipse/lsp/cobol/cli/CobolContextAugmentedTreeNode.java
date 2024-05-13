/*
 * Copyright (c) 2023 Broadcom.
 * The term "Broadcom" refers to Broadcom Inc. and/or its subsidiaries.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Broadcom, Inc. - initial API and implementation
 *
 */
package org.eclipse.lsp.cobol.cli;

import hu.webarticum.treeprinter.SimpleTreeNode;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.lsp.cobol.common.model.tree.Node;
import org.eclipse.lsp.cobol.core.CobolParser;

/**
 *  Visualisation Tree Node that encapsulates the actual AST node
 */
public class CobolContextAugmentedTreeNode extends SimpleTreeNode {
    private final ParseTree astNode;

    public CobolContextAugmentedTreeNode(ParseTree astNode) {
        super(astNode.getClass().getSimpleName());
        this.astNode = astNode;
    }

    @Override
    public String content() {
        return withType(astNode);
    }

    private String withType(ParseTree astNode) {
//        ParserRuleContext context = (ParserRuleContext) astNode;
//        Token start = context.start;
//        Token stop = context.stop;
        Token startToken = (astNode instanceof TerminalNode) ? ((TerminalNode)(astNode)).getSymbol() : ((ParserRuleContext)(astNode)).start;
        Token stopToken = (astNode instanceof TerminalNode) ? ((TerminalNode)(astNode)).getSymbol() : ((ParserRuleContext)(astNode)).stop;

        CharStream cs = startToken.getInputStream();
        int stopIndex = stopToken != null ? stopToken.getStopIndex() : -1;
        String originalText = stopIndex >= startToken.getStartIndex() ? cs.getText(new Interval(startToken.getStartIndex(), stopIndex)) : "<NULL>";
//        String text = astNode.getText().length() > 50 ? astNode.getText().substring(0, 50) + " ... (truncated)" : astNode.getText();
        String text = originalText.length() > 50 ? originalText.substring(0, 50) + " ... (truncated)" : originalText;
        return astNode.getClass().getSimpleName() + " / " + text;
    }
}
