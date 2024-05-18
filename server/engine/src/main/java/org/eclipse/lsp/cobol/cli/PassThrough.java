package org.eclipse.lsp.cobol.cli;

public class PassThrough implements CobolVmInstruction {
    @Override
    public CobolVmInstruction execute(NextExecution e) {
        return e.apply();
    }
}
