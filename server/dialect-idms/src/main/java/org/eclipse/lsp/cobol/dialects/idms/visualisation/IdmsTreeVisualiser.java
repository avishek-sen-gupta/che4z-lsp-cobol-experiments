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
package org.eclipse.lsp.cobol.dialects.idms.visualisation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.dialects.idms.IdmsAugmentedTreeNode;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws IDMS AST
 */
public class IdmsTreeVisualiser {
    /**
     * Draws the tree
     *
     * @param startRuleContext
     */
    public void visualiseIdmsAST(IdmsParser.StartRuleContext startRuleContext) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        List<IdmsParser.IdmsRulesContext> idmsRulesContexts = startRuleContext.idmsRules();
        List<IdmsAugmentedTreeNode> allAstTrees = new ArrayList<>();

        for (IdmsParser.IdmsRulesContext ctx : idmsRulesContexts) {
            IdmsParser.IdmsSectionsContext idmsSectionsContext = ctx.idmsSections();
            IdmsParser.IdmsStatementsContext idmsStatementsContext = ctx.idmsStatements();
            if (idmsSectionsContext != null) {
                allAstTrees.addAll(drawParseTrees(idmsSectionsContext.children));
            }
            if (idmsStatementsContext != null) {
                allAstTrees.addAll(drawParseTrees(idmsStatementsContext.children));
            }
        }

        try {
            String s = gson.toJson(allAstTrees);
            PrintWriter out = new PrintWriter("/Users/asgupta/Downloads/mbrdi-poc/V751C931-idms-parse-trees.json");
            out.println(s);
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<IdmsAugmentedTreeNode> drawParseTrees(List<ParseTree> parseRuleContext) {
        List<ParseTree> trees = parseRuleContext;
        List<IdmsAugmentedTreeNode> astTrees = new ArrayList<>();
        for (ParseTree tree : trees) {
            IdmsAugmentedTreeNode graphRoot = new IdmsAugmentedTreeNode(tree);
            buildGraph(tree, graphRoot);
            astTrees.add(graphRoot);
            new ListingTreePrinter().print(graphRoot);
        }
        return astTrees;
    }

    private void buildGraph(ParseTree astParentNode, IdmsAugmentedTreeNode graphParentNode) {
        for (int i = 0; i <= astParentNode.getChildCount() - 1; ++i) {
            ParseTree astChildNode = astParentNode.getChild(i);
            IdmsAugmentedTreeNode graphChildNode = new IdmsAugmentedTreeNode(astChildNode);
            graphParentNode.addChild(graphChildNode);
            buildGraph(astChildNode, graphChildNode);
        }
        graphParentNode.freeze();
    }
}
