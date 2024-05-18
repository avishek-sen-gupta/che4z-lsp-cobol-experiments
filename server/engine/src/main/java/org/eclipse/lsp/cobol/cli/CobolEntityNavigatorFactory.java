package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class CobolEntityNavigatorFactory {

    public static ParserRuleContext procedureDivisionBody;

    public static CobolEntityNavigator navigator() {
        return new CobolEntityNavigator(procedureDivisionBody);
    }
}
