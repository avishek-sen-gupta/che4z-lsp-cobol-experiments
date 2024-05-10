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
package org.eclipse.lsp.cobol.dialects.idms;

import hu.webarticum.treeprinter.SimpleTreeNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.text.MessageFormat;

/**
 *  Visualisation Tree Node that encapsulates the actual AST node
 */
public class IdmsAugmentedTreeNode extends SimpleTreeNode {
    private final ParseTree astNode;

    public IdmsAugmentedTreeNode(ParseTree astNode) {
        super(astNode.getClass().getSimpleName());
        this.astNode = astNode;
    }

    @Override
    public String content() {
        return withType("", astNode);
    }

    private String withType(String nodeDescription, ParseTree astNode) {
        if (astNode instanceof ParserRuleContext) {
            ParserRuleContext context = (ParserRuleContext) astNode;
            int startLine = context.start.getLine();
            int stopLine = context.stop.getLine();
            int startColumn = context.start.getCharPositionInLine();
            int stopColumn = context.stop.getCharPositionInLine();
            String formattedExtent = MessageFormat.format("([{0}, {1}] - [{2}, {3}])", startLine, startColumn, stopLine, stopColumn);
            return astNode.getClass().getSimpleName() + " / " + astNode.getText() + " " + formattedExtent;
//            return astNode.getClass().getSimpleName() + " / " + astNode.getText() + "([" + startLine + ", " + startColumn + "] - [" + stopLine + ", " + stopColumn + "])";
        }
        return astNode.getClass().getSimpleName() + " / " + astNode.getText();
    }
}
