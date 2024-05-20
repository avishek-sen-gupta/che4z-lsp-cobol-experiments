package org.eclipse.lsp.cobol.cli.vm;

import java.util.List;

import static org.eclipse.lsp.cobol.cli.vm.FlowUnit.TERMINAL;

public class FlowNavigator {
    private List<FlowUnit> units;

    public FlowNavigator(List<FlowUnit> units) {
        this.units = units;
    }

    public FlowUnit instruction(int instructionPointer) {
        if (instructionPointer >= units.size()) return TERMINAL;
        return units.get(instructionPointer);
    }
}
