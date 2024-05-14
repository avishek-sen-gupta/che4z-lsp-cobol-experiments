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
import org.eclipse.lsp.cobol.common.model.TextSpan;

import java.util.List;

/**
 *  Visualisation Tree Node that encapsulates the actual AST node
 */
public class CobolContextAugmentedTreeNode extends SimpleTreeNode {
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

    public CobolContextAugmentedTreeNode(ParseTree astNode) {
        super(astNode.getClass().getSimpleName());
        this.astNode = astNode;
        this.nodeType = astNode.getClass().getSimpleName();
        this.originalText = withType(astNode, false);
        this.span = createSpan(astNode);
    }

    private TextSpan createSpan(ParseTree astNode) {
        if (!(astNode instanceof ParserRuleContext)) {
            TerminalNode terminalNode = (TerminalNode) astNode;
            return new TextSpan(terminalNode.getSymbol().getLine(), terminalNode.getSymbol().getLine(), terminalNode.getSymbol().getCharPositionInLine(), -1, terminalNode.getSymbol().getStartIndex(), terminalNode.getSymbol().getStopIndex());
        }
        ParserRuleContext context = (ParserRuleContext) astNode;
        return new TextSpan(context.start.getLine(), context.stop.getLine(), context.start.getCharPositionInLine(), context.stop.getCharPositionInLine(), context.start.getStartIndex(), context.stop.getStopIndex());
    }
    @Override
    public String content() {
        return astNode.getClass().getSimpleName() + " / " + withType(astNode, true);
    }

    private String withType(ParseTree astNode, boolean truncate) {
        String originalText = originalText(astNode);
//        String text = astNode.getText().length() > 50 ? astNode.getText().substring(0, 50) + " ... (truncated)" : astNode.getText();
        return truncate ? truncated(originalText) : originalText;
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
