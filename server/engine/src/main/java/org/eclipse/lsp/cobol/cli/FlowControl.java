package org.eclipse.lsp.cobol.cli;

import lombok.Data;

@Data
public class FlowControl {
    boolean continueExecution = true;
    private CobolFrame frame;

    public FlowControl(CobolFrame frame) {
        this.frame = frame;
    }
}
