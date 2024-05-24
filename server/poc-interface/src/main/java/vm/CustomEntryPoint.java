package vm;

public class CustomEntryPoint implements EntryInstructionPointer {
    private FlowUnit targetParaOrSection;
    private boolean isComplete = false;


    public CustomEntryPoint(FlowUnit targetParaOrSection) {
        this.targetParaOrSection = targetParaOrSection;
    }

    @Override
    public int instructionPointer(FlowUnit unit) {
        if (isComplete) return ZERO.instructionPointer(unit);
        int candidatePointer = unit.getChildren().indexOf(targetParaOrSection);
        if (candidatePointer == -1) return ZERO.instructionPointer(unit);
        isComplete = true;
        return candidatePointer;
    }
}
