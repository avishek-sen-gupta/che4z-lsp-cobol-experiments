package org.eclipse.lsp.cobol.cli.vm;

import org.antlr.v4.runtime.ParserRuleContext;

public class CobolEntityNavigatorFactory {

    public static ParserRuleContext procedureDivisionBody;
    public static FlowUnit procedureDivisionFlowUnit;

    public static GlobalFlowUnitNavigator flowUnitnavigator() {
        return new GlobalFlowUnitNavigator(procedureDivisionFlowUnit);
    }
}
