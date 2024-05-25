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
package org.poc.analysis.visualisation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.flowchart.CobolContextAugmentedTreeNode;
import org.flowchart.CobolTreeVisualiser;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Draws Cobol AST
 */
public class CobolTreeVisualiserImpl implements CobolTreeVisualiser {
    @Override
    public void visualiseCobolAST(ParserRuleContext tree, String cobolParseTreeOutputPath) {
        visualiseCobolAST(tree, cobolParseTreeOutputPath, true);
    }

    /**
     * Draws the tree using the canonical Context structure
     *
     * @param tree
     * @param cobolParseTreeOutputPath
     */
    @Override
    public void visualiseCobolAST(ParseTree tree, String cobolParseTreeOutputPath, boolean outputTree) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        CobolContextAugmentedTreeNode graphRoot = new CobolContextAugmentedTreeNode(tree);
        buildContextGraph(tree, graphRoot);
        if (outputTree) new ListingTreePrinter().print(graphRoot);
        try {
            String s = gson.toJson(graphRoot);
//            PrintWriter out = new PrintWriter("/Users/asgupta/Downloads/mbrdi-poc/V7523438-compiled-parse-tree.json");
            PrintWriter out = new PrintWriter(cobolParseTreeOutputPath);
            out.println(s);
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void buildContextGraph(ParseTree astParentNode, CobolContextAugmentedTreeNode graphParentNode) {
        for (int i = 0; i <= astParentNode.getChildCount() - 1; ++i) {
            ParseTree astChildNode = astParentNode.getChild(i);
            CobolContextAugmentedTreeNode graphChildNode = new CobolContextAugmentedTreeNode(astChildNode);
            if (! shouldSkip(astChildNode, astParentNode))
                graphParentNode.addChild(graphChildNode);
            buildContextGraph(astChildNode, graphChildNode);
        }
        graphParentNode.freeze();
    }

    private static boolean shouldSkip(ParseTree astChildNode, ParseTree astParentNode) {
//        if (astChildNode.getClass() == CobolParser.EaterContext.class) return true;
//        if (astParentNode.getClass() == CobolParser.DialectNodeFillerContext.class) {
//            return astChildNode.getClass() != IdmsContainerNode.class;
//        }
        return false;
    }
}
