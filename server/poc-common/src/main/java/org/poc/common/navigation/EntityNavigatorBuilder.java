package org.poc.common.navigation;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

public interface EntityNavigatorBuilder {
    CobolEntityNavigator procedureDivisionEntityNavigator(CobolParser.ProcedureDivisionBodyContext procedureDivisionBody, ParserRuleContext fullProgramTree);
    CobolParser.ProcedureDivisionBodyContext procedureDivisionBody(ParseTree tree);
}
