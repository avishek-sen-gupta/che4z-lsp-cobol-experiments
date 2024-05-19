package org.eclipse.lsp.cobol.cli;

public class ExitProcedure implements CobolVmInstruction {
    private boolean hasExited = false;
    @Override
    public CobolVmInstruction execute(NextExecution e) {
        return null;
    }

    @Override
    public boolean apply(FlowControl flow) {
        ProgramScope scope = flow.getFrame().getScope().scope();
        if (scope != ProgramScope.PERFORM && !hasExited) {
            flow.setContinueExecution(false);
            System.out.println("Breaking out of " + scope + "[" + flow.getFrame().getScope() + "]");
            return false;
        }
        hasExited = true;
        flow.setContinueExecution(true);
        System.out.println("Reached procedure call origin [" + flow.getFrame().getScope() + "]");
        return true;
    }
}
