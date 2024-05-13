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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.CobolAugmentedTreeNode;
import org.eclipse.lsp.cobol.cli.CobolContextAugmentedTreeNode;
import org.eclipse.lsp.cobol.common.model.tree.Node;

/**
 * Draws Cobol AST
 */
public class CobolTreeVisualiser {
    /**
     * Draws the tree using the wrapping Node structure
     * @param rootNode
     */
    public void visualiseCobolAST(Node rootNode) {
        SimpleTreeNode graphRoot = new CobolAugmentedTreeNode(rootNode);
        buildGraph(rootNode, graphRoot);
        new ListingTreePrinter().print(graphRoot);
    }

    private void buildGraph(Node astParentNode, SimpleTreeNode graphParentNode) {
        for (Node astChildNode : astParentNode.getChildren()) {
            SimpleTreeNode graphChildNode = new CobolAugmentedTreeNode(astChildNode);
            graphParentNode.addChild(graphChildNode);
            buildGraph(astChildNode, graphChildNode);
        }
    }

    /**
     * Draws the tree using the canonical Context structure
     * @param tree
     */
    public void visualiseCobolAST(ParseTree tree) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        CobolContextAugmentedTreeNode graphRoot = new CobolContextAugmentedTreeNode(tree);
        buildContextGraph(tree, graphRoot);
        new ListingTreePrinter().print(graphRoot);
        String s = gson.toJson(graphRoot);
    }

    private void buildContextGraph(ParseTree astParentNode, CobolContextAugmentedTreeNode graphParentNode) {
        for (int i = 0; i <= astParentNode.getChildCount() - 1; ++i) {
            ParseTree astChildNode = astParentNode.getChild(i);
            CobolContextAugmentedTreeNode graphChildNode = new CobolContextAugmentedTreeNode(astChildNode);
            graphParentNode.addChild(graphChildNode);
            buildContextGraph(astChildNode, graphChildNode);
        }
        graphParentNode.freeze();
    }

}
