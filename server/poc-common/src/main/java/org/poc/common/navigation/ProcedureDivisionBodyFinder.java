package org.poc.common.navigation;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

public interface ProcedureDivisionBodyFinder {
    CobolParser.ProcedureDivisionBodyContext apply(ParseTree tree);
}
