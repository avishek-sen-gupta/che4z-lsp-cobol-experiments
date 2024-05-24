package org.poc.common.navigation;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

public interface EntityNavigatorBuilder {
    CobolEntityNavigator procedureDivisionEntityNavigator(CobolParser.ProcedureDivisionBodyContext procedureDivisionBody);
    CobolParser.ProcedureDivisionBodyContext procedureDivisionBody(ParseTree tree);
}
