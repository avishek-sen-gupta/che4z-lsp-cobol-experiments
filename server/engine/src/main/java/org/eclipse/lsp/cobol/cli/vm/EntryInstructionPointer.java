package org.eclipse.lsp.cobol.cli.vm;

public interface EntryInstructionPointer {
    EntryInstructionPointer ZERO = unit -> 0;

    int instructionPointer(FlowUnit unit);
}
