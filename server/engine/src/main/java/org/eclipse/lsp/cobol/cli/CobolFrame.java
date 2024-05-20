package org.eclipse.lsp.cobol.cli;

import lombok.Getter;
import lombok.Setter;

public class CobolFrame {
    @Getter private final FlowUnit scope;
    private CobolFrame parent;
    //    private final ParserRuleContext entryPoint;
    @Getter @Setter
    private int instructionPointer;
    private FlowNavigator navigator;
    @Getter private EntryInstructionPointer ipStrategy;

    public CobolFrame(FlowNavigator navigator, CobolFrame parent, FlowUnit scope) {
        this(navigator, parent, scope, EntryInstructionPointer.ZERO);
    }

    public CobolFrame(FlowNavigator navigator, CobolFrame parent, FlowUnit scope, EntryInstructionPointer ipStrategy) {
        this.ipStrategy = ipStrategy;
        this.scope = scope;
        this.parent = parent;
        this.instructionPointer = ipStrategy.instructionPointer(scope);
        this.navigator = navigator;
    }

    public FlowUnit getInstruction() {
        return navigator.instruction(instructionPointer);
    }

    public void advance() {
        instructionPointer ++;
    }

    public boolean isProcedure() {
        CobolFrame parent = this.parent;
        while (parent != null) {
            if (parent.scope.scope() == ProgramScope.PERFORM) return true;
            parent = parent.parent;
        }
        return false;
    }
}
