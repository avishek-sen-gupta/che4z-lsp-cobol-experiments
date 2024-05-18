package org.eclipse.lsp.cobol.cli;

import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

public class IfBranchInstruction implements InstructionPointerOperation {
    private List<InstructionPointerOperation> log;

    public IfBranchInstruction(List<InstructionPointerOperation> log) {
        this.log = log;
    }

    @Override
    public InstructionContext nextContext() {
        return null;
    }
}
