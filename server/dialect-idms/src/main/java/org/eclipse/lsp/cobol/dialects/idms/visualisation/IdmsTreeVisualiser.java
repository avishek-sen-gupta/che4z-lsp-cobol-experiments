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

import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.dialects.idms.IdmsAugmentedTreeNode;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;

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
        List<IdmsParser.IdmsRulesContext> idmsRulesContexts = startRuleContext.idmsRules();
        for (IdmsParser.IdmsRulesContext ctx : idmsRulesContexts) {
            IdmsParser.IdmsSectionsContext idmsSectionsContext = ctx.idmsSections();
            IdmsParser.IdmsStatementsContext idmsStatementsContext = ctx.idmsStatements();
            if (idmsSectionsContext != null) {
                drawParseTrees(idmsSectionsContext.children);
            }
            if (idmsStatementsContext != null) {
                drawParseTrees(idmsStatementsContext.children);
            }
        }
    }

    private void drawParseTrees(List<ParseTree> parseRuleContext) {
        List<ParseTree> trees = parseRuleContext;
        for (ParseTree tree : trees) {
            SimpleTreeNode graphRoot = new IdmsAugmentedTreeNode(tree);
            buildGraph(tree, graphRoot);
            new ListingTreePrinter().print(graphRoot);
        }
    }

    private void buildGraph(ParseTree astParentNode, SimpleTreeNode graphParentNode) {
        for (int i = 0; i <= astParentNode.getChildCount() - 1; ++i) {
            ParseTree astChildNode = astParentNode.getChild(i);
            SimpleTreeNode graphChildNode = new IdmsAugmentedTreeNode(astChildNode);
            graphParentNode.addChild(graphChildNode);
            buildGraph(astChildNode, graphChildNode);
        }
    }
}
