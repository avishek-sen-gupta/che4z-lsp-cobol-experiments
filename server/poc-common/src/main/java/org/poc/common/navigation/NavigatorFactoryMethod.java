package org.poc.common.navigation;

import org.eclipse.lsp.cobol.core.CobolParser;

public interface NavigatorFactoryMethod {
    CobolEntityNavigator apply(CobolParser.ProcedureDivisionBodyContext tree);
}
