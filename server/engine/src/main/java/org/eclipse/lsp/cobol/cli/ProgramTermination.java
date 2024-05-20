package org.eclipse.lsp.cobol.cli;

public class ProgramTermination implements CobolVmInstruction {
    @Override
    public CobolVmInstruction execute(NextExecution e) {
        return null;
    }

    @Override
    public boolean apply(FlowControl flow) {
        flow.continueExecution = false;
        return false;
    }
}
