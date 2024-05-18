package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.tree.ParseTree;

public class GoToJmpInstruction implements InstructionPointerOperation {
    private InstructionContext next;
    private final CobolEntityNavigator navigator;

    public GoToJmpInstruction(InstructionContext next, CobolEntityNavigator navigator) {
        this.next = next;
        this.navigator = navigator;
    }

    @Override
    public InstructionContext nextContext() {
        return next;
    }
}
