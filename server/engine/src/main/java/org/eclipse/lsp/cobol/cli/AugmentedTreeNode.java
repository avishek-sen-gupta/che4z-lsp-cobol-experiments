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
import org.eclipse.lsp.cobol.common.model.tree.*;
import org.eclipse.lsp.cobol.common.model.tree.variable.VariableDefinitionNameNode;
import org.eclipse.lsp.cobol.common.model.tree.variable.VariableUsageNode;
import org.eclipse.lsp.cobol.common.model.tree.variables.ConditionDataNameNode;

/**
 *  Visualisation Tree Node that encapsulates the actual AST node
 */
public class AugmentedTreeNode extends SimpleTreeNode {
    private Node astNode;

    public AugmentedTreeNode(Node astNode) {
        super(astNode.getClass().getSimpleName());
        this.astNode = astNode;
    }

    @Override
    public String content() {
        if (astNode instanceof DivisionNode) return withType(((DivisionNode) astNode).getDivisionType().getDivName(), astNode);
        if (astNode instanceof ParagraphNameNode) return withType(((ParagraphNameNode) astNode).getName(), astNode);
        if (astNode instanceof LiteralNode)
            return withType(((LiteralNode) astNode).getText(), astNode);
        if (astNode instanceof SectionNameNode)
            return withType(((SectionNameNode) astNode).getName(), astNode);
        if (astNode instanceof VariableUsageNode)
            return withType(((VariableUsageNode) astNode).getFormattedDisplayString(), astNode);
        if (astNode instanceof VariableDefinitionNameNode)
            return withType(((VariableDefinitionNameNode) astNode).getName(), astNode);
        if (astNode instanceof PerformNode)
            return withType(((PerformNode) astNode).getTarget().getName(), astNode);
        if (astNode instanceof ConditionDataNameNode)
            return withType(((ConditionDataNameNode) astNode).getFullVariableDescription(), astNode);
        return astNode.getClass().getSimpleName();
    }

    private String withType(String nodeDescription, Node astNode) {
        return astNode.getClass().getSimpleName() + " / " + astNode.getNodeType().name() + " / " + nodeDescription;
    }
}
