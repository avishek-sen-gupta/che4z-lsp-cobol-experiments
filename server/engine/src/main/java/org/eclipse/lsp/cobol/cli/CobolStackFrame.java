package org.eclipse.lsp.cobol.cli;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.lsp.cobol.core.CobolParser;

public class CobolStackFrame {
    private final ParserRuleContext callSite;
    private final ParserRuleContext entryPoint;
    @Getter @Setter
    private int instructionPointer;
    private CobolEntityNavigator navigator;

    public CobolStackFrame(CobolEntityNavigator navigator, ParserRuleContext callSite, ParserRuleContext entryPoint, int iptr) {
        this.callSite = callSite;
        this.entryPoint = entryPoint;
        this.instructionPointer = iptr;
        this.navigator = navigator;
    }

    public CobolStackFrame(CobolEntityNavigator navigator, ParserRuleContext callSite, ParserRuleContext entryPoint) {
        this(navigator, callSite, entryPoint, 0);
    }
}
