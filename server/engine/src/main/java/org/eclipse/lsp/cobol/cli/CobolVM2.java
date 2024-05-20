package org.eclipse.lsp.cobol.cli;

import static org.eclipse.lsp.cobol.cli.FlowUnit.TERMINAL;

public class CobolVM2 {
    private final CobolFrame frame;
    private final FlowNavigator navigator;

    public CobolVM2(CobolFrame frame, FlowNavigator navigator) {
        this.frame = frame;
        this.navigator = navigator;
        System.out.println("Entering new context " + frame.getScope());
    }

    public CobolVmInstruction run() {
        FlowControl flow = new FlowControl(frame);
        FlowUnit unit = null;
        CobolVmInstruction instruction = new PassThrough();
        while (flow.continueExecution && unit != TERMINAL) {
            unit = frame.getInstruction();
            if (unit == TERMINAL) {
                if (frame.getScope().scope() == ProgramScope.GOTO) {
                    System.out.println("Terminating program...");
                    return new ProgramTermination();
                }
                System.out.println("Exiting context " + frame.getScope());
                break;
            }

            instruction = interpret(unit);
            if (instruction.apply(flow)) {
                // For a successful EXIT. We do not wish to propagate the EXIT behaviour any further.
                instruction = new PassThrough();
            }
            frame.advance();
        }

        // instruction will stay Passthrough in case of an empty section/paragraph/division/sentence.
        return instruction;

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
            System.out.println("Interpreting atomic: " + unit);
            return unit.instruction(frame);
        }

        return new ScopeExecutionBuilder(unit, CobolEntityNavigatorFactory.flowUnitnavigator(), this.frame).run();
    }
}
