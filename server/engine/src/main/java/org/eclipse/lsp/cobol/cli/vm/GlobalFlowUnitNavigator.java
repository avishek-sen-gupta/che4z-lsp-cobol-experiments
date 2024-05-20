package org.eclipse.lsp.cobol.cli.vm;

import org.eclipse.lsp.cobol.cli.vm.FlowUnit;

import java.util.List;

public class GlobalFlowUnitNavigator {
    private FlowUnit procedureDivisionFlowUnit;

    public GlobalFlowUnitNavigator(FlowUnit procedureDivisionFlowUnit) {
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

    public List<FlowUnit> allFlowUnitsFrom(FlowUnit f) {
        List<FlowUnit> procedureLevelGroups = procedureDivisionFlowUnit.getChildren();
        List<FlowUnit> remainingUnits = procedureLevelGroups.subList(procedureLevelGroups.indexOf(f), procedureLevelGroups.size());
        return remainingUnits;
    }
}
