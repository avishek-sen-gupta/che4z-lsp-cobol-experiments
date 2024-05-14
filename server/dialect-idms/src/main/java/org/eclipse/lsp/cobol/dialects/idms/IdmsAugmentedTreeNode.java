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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.TreeNode;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.text.MessageFormat;
import java.util.List;

/**
 *  Visualisation Tree Node that encapsulates the actual AST node
 */
public class IdmsAugmentedTreeNode extends SimpleTreeNode {
    private final ParseTree astNode;

    @Expose
    @SerializedName("nodeType")
    private final String nodeType;
    @Expose
    @SerializedName("text")
    private final String originalText;

    @Expose
    @SerializedName("children")
    private List<TreeNode> childrenRef;

    @Expose
    @SerializedName("span")
    private TextSpan span;

    public IdmsAugmentedTreeNode(ParseTree astNode) {
        super(astNode.getClass().getSimpleName());
        this.astNode = astNode;
        this.nodeType = astNode.getClass().getSimpleName();
        this.originalText = withType(astNode, false);
        this.span = createSpan(astNode);
    }

    private TextSpan createSpan(ParseTree astNode) {
        if (!(astNode instanceof ParserRuleContext)) return new TextSpan(-1, -1, -1, -1);
        ParserRuleContext context = (ParserRuleContext) astNode;
        return new TextSpan(context.start.getLine(), context.stop.getLine(), context.start.getCharPositionInLine(), context.stop.getCharPositionInLine());
    }

    @Override
    public String content() {
        String formattedExtent = MessageFormat.format("({0}])", span.content());
        return astNode.getClass().getSimpleName() + " / " + withType(astNode, true) + " " + formattedExtent;
    }

    private String withType(ParseTree astNode, boolean truncate) {
        String originalText = originalText(astNode);
        if (astNode instanceof ParserRuleContext) {
            ParserRuleContext context = (ParserRuleContext) astNode;
            return truncate ? truncated(originalText) : originalText;
        }
        return astNode.getText();
    }

    private String truncated(String text) {
        return text.length() > 50 ? text.substring(0, 50) + " ... (truncated)" : text;
    }

    private String originalText(ParseTree astNode) {
        Token startToken = (astNode instanceof TerminalNode) ? ((TerminalNode) astNode).getSymbol() : ((ParserRuleContext) astNode).start;
        Token stopToken = (astNode instanceof TerminalNode) ? ((TerminalNode) astNode).getSymbol() : ((ParserRuleContext) astNode).stop;

        CharStream cs = startToken.getInputStream();
        int stopIndex = stopToken != null ? stopToken.getStopIndex() : -1;
        return stopIndex >= startToken.getStartIndex() ? cs.getText(new Interval(startToken.getStartIndex(), stopIndex)) : "<NULL>";
    }

    /**
     * Creates a new reference to the children that will be used for serialisation to JSON
     */
    public void freeze() {
        this.childrenRef = super.children();
    }
}

