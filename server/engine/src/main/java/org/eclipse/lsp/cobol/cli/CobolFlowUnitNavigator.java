package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

public class CobolFlowUnitNavigator {
    private FlowUnit procedureDivisionFlowUnit;

    public CobolFlowUnitNavigator(FlowUnit procedureDivisionFlowUnit) {
        this.procedureDivisionFlowUnit = procedureDivisionFlowUnit;
    }

    public FlowUnit findTarget(String procedureName) {
        return findTargetRecursive(procedureName, procedureDivisionFlowUnit);
    }

    public FlowUnit findTargetRecursive(String procedureNameContext, FlowUnit currentNode) {
        if (procedureNameContext.equals(currentNode.executionContextName())) return currentNode;
        List<FlowUnit> children = currentNode.getChildren();
        for (int i = 0; i <= children.size() - 1; i++) {
            FlowUnit searchResult = findTargetRecursive(procedureNameContext, children.get(i));
            if (searchResult != null) return searchResult;
        }

        return null;
    }
}
