package org.eclipse.lsp.cobol.cli;

import static org.eclipse.lsp.cobol.cli.CobolVmInstruction.TERM;
import static org.eclipse.lsp.cobol.cli.FlowUnit.TERMINAL;

public class CobolVM2 {
    private final CobolFrame frame;
    private final FlowNavigator navigator;

    public CobolVM2(CobolFrame frame, FlowNavigator navigator) {
        this.frame = frame;
        this.navigator = navigator;
        System.out.println("Entering new context " + frame.getCallSite());
    }

    public CobolVmInstruction run() {
        FlowUnit unit = null;
        do {
            unit = frame.getInstruction();
            if (unit == TERMINAL) break;
            CobolVmInstruction instruction = interpret(unit);
            frame.advance();
        } while (unit != TERMINAL);
        return null;
//        FlowUnit unit = frame.getInstruction();
//        if (unit == TERMINAL) return TERM;
//        CobolVmInstruction instruction = interpret(unit);
//
//        return instruction.execute(() -> {
//            frame.advance();
//            return run();
//        });
    }

    private CobolVmInstruction interpret(FlowUnit unit) {
        if (unit.isAtomic()) {
            return unit.instruction();
            // Interpret as statement
        }
        FlowNavigator flowNavigator = new FlowNavigator(unit.units());
        CobolFrame frame = new CobolFrame(flowNavigator, unit, this.frame);
        CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
        CobolVmInstruction result = vm2.run();
        return result;
    }
}
