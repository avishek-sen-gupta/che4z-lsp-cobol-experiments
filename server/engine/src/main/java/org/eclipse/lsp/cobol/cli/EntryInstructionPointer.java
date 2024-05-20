package org.eclipse.lsp.cobol.cli;

public interface EntryInstructionPointer {
    EntryInstructionPointer ZERO = unit -> 0;

    int instructionPointer(FlowUnit unit);
}
