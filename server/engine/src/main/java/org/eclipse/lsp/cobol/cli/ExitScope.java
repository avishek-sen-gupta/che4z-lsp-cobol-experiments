package org.eclipse.lsp.cobol.cli;

import static java.lang.System.exit;

public class ExitScope implements CobolVmInstruction {
    private boolean hasExited = false;

    @Override
    public CobolVmInstruction execute(NextExecution e) {
        return null;
    }

    @Override
    public boolean apply(FlowControl flow) {
        ProgramScope scope = flow.getFrame().getScope().scope();
        if (scope == ProgramScope.PARAGRAPH || scope != ProgramScope.SECTION && !hasExited) {
            flow.setContinueExecution(false);
            hasExited = true;
            System.out.println("Breaking out of " + scope + "[" + flow.getFrame().getScope() + "]");
            return true;
        }
//        if (scope == ProgramScope.GOTO && !hasExited) {
//            System.out.println("Cannot unwind GO TO stack. Program has ended.");
//            exit(0);
//        }
        else if (!hasExited) {
            flow.setContinueExecution(false);
            System.out.println("Breaking out of " + scope + "[" + flow.getFrame().getScope() + "]");
            return false;
        }
        return false;
    }
}
