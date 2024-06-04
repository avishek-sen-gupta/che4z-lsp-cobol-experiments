package vm;

public class ExitScope implements CobolVmInstruction {
    private boolean hasExited = false;

    @Override
    public CobolVmInstruction execute(NextExecution e) {
        return null;
    }

    @Override
    public boolean apply(OldFlowControl flow) {
        ProgramScope scope = flow.getFrame().getScope().scope();
        if (scope == ProgramScope.PARAGRAPH || scope != ProgramScope.SECTION && !hasExited) {
            flow.setContinueExecution(false);
            hasExited = true;
            System.out.println("Breaking out of " + scope + "[" + flow.getFrame().getScope() + "]");
            return true;
        }
        else if (!hasExited) {
            flow.setContinueExecution(false);
            System.out.println("Breaking out of " + scope + "[" + flow.getFrame().getScope() + "]");
            return false;
        }
        return false;
    }
}
