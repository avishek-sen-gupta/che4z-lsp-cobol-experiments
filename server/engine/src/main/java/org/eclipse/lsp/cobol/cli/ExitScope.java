package org.eclipse.lsp.cobol.cli;

public class ExitScope implements CobolVmInstruction {
    @Override
    public CobolVmInstruction execute(NextExecution e) {
        return null;
    }
}
