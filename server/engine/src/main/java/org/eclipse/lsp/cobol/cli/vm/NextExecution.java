package org.eclipse.lsp.cobol.cli.vm;

import org.eclipse.lsp.cobol.cli.vm.CobolVmInstruction;

public interface NextExecution {
    CobolVmInstruction apply();
}
