package org.eclipse.lsp.cobol.cli;

import lombok.Data;
import lombok.Getter;

public class InstructionContext {
    public static InstructionContext TERM = new InstructionContext(-1, null);
    @Getter
    private final int statementPointer;
    private final LocationContext context;

    public InstructionContext(int statementPointer, LocationContext context) {
        this.statementPointer = statementPointer;
        this.context = context;
    }
}
