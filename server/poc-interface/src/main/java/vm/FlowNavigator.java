package vm;

import java.util.List;

public class FlowNavigator {
    private List<FlowUnit> units;

    public FlowNavigator(List<FlowUnit> units) {
        this.units = units;
    }

    public FlowUnit instruction(int instructionPointer) {
        if (instructionPointer >= units.size()) return FlowUnit.TERMINAL;
        return units.get(instructionPointer);
    }
}
