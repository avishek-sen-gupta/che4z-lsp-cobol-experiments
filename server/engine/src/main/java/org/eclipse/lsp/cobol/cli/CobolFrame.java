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

    public CobolFrame(FlowNavigator navigator, FlowUnit scope, CobolFrame parent, int iptr) {
        this.scope = scope;
        this.parent = parent;
        this.instructionPointer = iptr;
        this.navigator = navigator;
    }

    public CobolFrame(FlowNavigator navigator, FlowUnit scope, CobolFrame parent) {
        this(navigator, scope, parent,0);
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
