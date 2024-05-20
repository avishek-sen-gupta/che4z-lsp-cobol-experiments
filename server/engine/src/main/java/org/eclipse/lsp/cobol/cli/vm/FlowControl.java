package org.eclipse.lsp.cobol.cli.vm;

import lombok.Data;
import org.eclipse.lsp.cobol.cli.vm.CobolFrame;

@Data
public class FlowControl {
    boolean continueExecution = true;
    private CobolFrame frame;

    public FlowControl(CobolFrame frame) {
        this.frame = frame;
    }
}
