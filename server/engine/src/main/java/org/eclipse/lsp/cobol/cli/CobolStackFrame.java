package org.eclipse.lsp.cobol.cli;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class CobolStackFrame {
    @Getter @Setter
    private int instructionPointer;
    private CobolEntityNavigator navigator;

    public CobolStackFrame(CobolEntityNavigator navigator) {
        this.instructionPointer = 0;
        this.navigator = navigator;
    }
}
