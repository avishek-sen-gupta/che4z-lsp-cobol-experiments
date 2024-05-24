package vm;

import lombok.Data;

@Data
public class FlowControl {
    boolean continueExecution = true;
    private CobolFrame frame;

    public FlowControl(CobolFrame frame) {
        this.frame = frame;
    }
}
