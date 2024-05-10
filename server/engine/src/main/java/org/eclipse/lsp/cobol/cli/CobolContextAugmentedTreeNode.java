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
import org.antlr.v4.runtime.tree.ParseTree;
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
        String text = astNode.getText().length() > 50 ? astNode.getText().substring(0, 50) + " ... (truncated)" : astNode.getText();
        return astNode.getClass().getSimpleName() + " / " + text;
    }
}
