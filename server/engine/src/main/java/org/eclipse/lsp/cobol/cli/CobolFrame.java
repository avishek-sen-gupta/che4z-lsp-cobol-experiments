package org.eclipse.lsp.cobol.cli;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.ParserRuleContext;

public class CobolFrame {
    @Getter private final FlowUnit callSite;
    private CobolFrame parent;
    //    private final ParserRuleContext entryPoint;
    @Getter @Setter
    private int instructionPointer;
    private FlowNavigator navigator;

    public CobolFrame(FlowNavigator navigator, FlowUnit callSite, CobolFrame parent, int iptr) {
        this.callSite = callSite;
        this.parent = parent;
        this.instructionPointer = iptr;
        this.navigator = navigator;
    }

    public CobolFrame(FlowNavigator navigator, FlowUnit callSite, CobolFrame parent) {
        this(navigator, callSite, parent,0);
    }

    public FlowUnit getInstruction() {
        return navigator.instruction(instructionPointer);
    }

    public void advance() {
        instructionPointer ++;
    }
}
