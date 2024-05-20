package org.eclipse.lsp.cobol.cli.vm;

import org.eclipse.lsp.cobol.cli.vm.CobolVmInstruction;
import org.eclipse.lsp.cobol.cli.vm.FlowControl;
import org.eclipse.lsp.cobol.cli.vm.NextExecution;

public class PassThrough implements CobolVmInstruction {
    @Override
    public CobolVmInstruction execute(NextExecution e) {
        return e.apply();
    }

    @Override
    public boolean apply(FlowControl flow) {
        return true;
    }
}
