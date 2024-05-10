/*
 * Copyright (c) 2022 Broadcom.
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
package org.eclipse.lsp.cobol.visualisation;

import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import org.eclipse.lsp.cobol.cli.CobolAugmentedTreeNode;
import org.eclipse.lsp.cobol.common.model.tree.Node;

/**
 * Draws Cobol AST
 */
public class CobolTreeVisualiser {
    /**
     * Draws the tree
     * @param rootNode
     */
    public void visualiseCobolAST(Node rootNode) {
        SimpleTreeNode graphRoot = new CobolAugmentedTreeNode(rootNode);
        new CobolTreeVisualiser().buildGraph(rootNode, graphRoot);
        new ListingTreePrinter().print(graphRoot);
    }

    private void buildGraph(Node astParentNode, SimpleTreeNode graphParentNode) {
        for (Node astChildNode : astParentNode.getChildren()) {
            SimpleTreeNode graphChildNode = new CobolAugmentedTreeNode(astChildNode);
            graphParentNode.addChild(graphChildNode);
            buildGraph(astChildNode, graphChildNode);
        }
    }
}
