package org.eclipse.lsp.cobol.cli.vm;

public interface CobolVmInstruction {
    CobolVmInstruction execute(NextExecution e);

    public static final CobolVmInstruction TERM = new CobolVmInstruction() {
        @Override
        public CobolVmInstruction execute(NextExecution e) {
            return this;
        }

        @Override
        public boolean apply(FlowControl flow) {
            return true;
        }
    };

    boolean apply(FlowControl flow);
}

