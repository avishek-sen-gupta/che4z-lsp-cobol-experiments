package org.eclipse.lsp.cobol.cli;

public class ExitScope implements CobolVmInstruction {
    private boolean hasExited = false;

    @Override
    public CobolVmInstruction execute(NextExecution e) {
        return null;
    }

    @Override
    public boolean apply(FlowControl flow) {
        ProgramScope scope = flow.getFrame().getScope().scope();
        if (scope != ProgramScope.UNKNOWN && scope != ProgramScope.SENTENCE && !hasExited) {
            flow.setContinueExecution(false);
            hasExited = true;
            System.out.println("Breaking out of " + scope + "[" + flow.getFrame().getScope() + "]");
            return true;
        }
        if (scope == ProgramScope.UNKNOWN || scope == ProgramScope.SENTENCE && !hasExited) {
            flow.setContinueExecution(false);
            System.out.println("Breaking out of " + scope + "[" + flow.getFrame().getScope() + "]");
        }
        return false;
    }
}
