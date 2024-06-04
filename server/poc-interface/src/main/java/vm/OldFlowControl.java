package vm;

import lombok.Data;

@Data
public class OldFlowControl {
    boolean continueExecution = true;
    private CobolFrame frame;

    public OldFlowControl(CobolFrame frame) {
        this.frame = frame;
    }
}
