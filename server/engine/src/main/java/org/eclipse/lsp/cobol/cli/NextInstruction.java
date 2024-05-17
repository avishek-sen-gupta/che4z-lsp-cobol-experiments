package org.eclipse.lsp.cobol.cli;

import org.eclipse.lsp.cobol.core.CobolParser;

public class NextInstruction implements InstructionPointerOperation {

    private final InstructionContext current;
    private final InstructionContext next;

    public NextInstruction(CobolParser.StatementContext current, CobolEntityNavigator navigator) {
        this.current = navigator.context(current);
        this.next = navigator.next(this.current);
    }

    @Override
    public InstructionContext next() {
        return next;
    }
}
