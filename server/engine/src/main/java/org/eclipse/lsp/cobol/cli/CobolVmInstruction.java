package org.eclipse.lsp.cobol.cli;

import java.util.function.IntBinaryOperator;

public interface CobolVmInstruction {
    CobolVmInstruction execute(NextExecution e);

    public static final CobolVmInstruction TERM = new CobolVmInstruction() {
        @Override
        public CobolVmInstruction execute(NextExecution e) {
            return this;
        }
    };
}

