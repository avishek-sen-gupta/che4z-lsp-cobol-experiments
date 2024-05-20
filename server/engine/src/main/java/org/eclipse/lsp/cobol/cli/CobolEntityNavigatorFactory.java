package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.ParserRuleContext;

public class CobolEntityNavigatorFactory {

    public static ParserRuleContext procedureDivisionBody;
    public static FlowUnit procedureDivisionFlowUnit;

    public static CobolEntityNavigator navigator() {
        return new CobolEntityNavigator(procedureDivisionBody);
    }

    public static GlobalFlowUnitNavigator flowUnitnavigator() {
        return new GlobalFlowUnitNavigator(procedureDivisionFlowUnit);
    }
}
