package org.eclipse.lsp.cobol.cli;

public class CustomEntryPoint implements EntryInstructionPointer {
    private FlowUnit targetParaOrSection;
    private boolean isComplete = false;


    public CustomEntryPoint(FlowUnit targetParaOrSection) {
        this.targetParaOrSection = targetParaOrSection;
    }

    @Override
    public int instructionPointer(FlowUnit unit) {
        if (isComplete) return EntryInstructionPointer.ZERO.instructionPointer(unit);
        int candidatePointer = unit.getChildren().indexOf(targetParaOrSection);
        if (candidatePointer == -1) return EntryInstructionPointer.ZERO.instructionPointer(unit);
        isComplete = true;
        return candidatePointer;
    }
}
