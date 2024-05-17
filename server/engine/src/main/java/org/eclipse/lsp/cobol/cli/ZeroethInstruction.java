package org.eclipse.lsp.cobol.cli;

import org.eclipse.lsp.cobol.core.CobolParser;

public class ZeroethInstruction implements InstructionPointerOperation {

    private final InstructionContext current;
    private final InstructionContext next;

    public ZeroethInstruction(CobolEntityNavigator navigator) {
        this.next = navigator.context(navigator.instruction(0));
        this.current = null;
    }

    @Override
    public InstructionContext next() {
        return next;
    }
}
