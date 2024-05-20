package org.eclipse.lsp.cobol.cli.vm;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.lsp.cobol.core.CobolParser;

public class CobolEntityNavigatorFactory {

    public static CobolParser.ProcedureDivisionBodyContext procedureDivisionBody;
    public static FlowUnit procedureDivisionFlowUnit;

    public static GlobalFlowUnitNavigator flowUnitnavigator() {
        return new GlobalFlowUnitNavigator(procedureDivisionFlowUnit);
    }
    public static CobolEntityNavigator entityNavigator(CobolParser.ProcedureDivisionBodyContext procedureDivisionBody) {
        return new CobolEntityNavigator(procedureDivisionBody);
    }
}
