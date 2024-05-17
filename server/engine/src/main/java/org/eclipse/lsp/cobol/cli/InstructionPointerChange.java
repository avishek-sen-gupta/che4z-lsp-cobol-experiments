package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.lsp.cobol.core.CobolParser;

public class InstructionPointerChange implements InstructionPointerOperation {
    private final InstructionContext current;
    private final InstructionContext next;

    public InstructionPointerChange(CobolParser.StatementContext current, CobolParser.StatementContext next, CobolEntityNavigator navigator) {
        this.current = navigator.context(current);
        this.next = navigator.context(next);
    }

    @Override
    public InstructionContext next() {
        return next;
    }
}
