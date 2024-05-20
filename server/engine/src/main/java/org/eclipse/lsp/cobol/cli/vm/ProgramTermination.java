package org.eclipse.lsp.cobol.cli.vm;

import org.eclipse.lsp.cobol.cli.vm.CobolVmInstruction;
import org.eclipse.lsp.cobol.cli.vm.FlowControl;
import org.eclipse.lsp.cobol.cli.vm.NextExecution;

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
