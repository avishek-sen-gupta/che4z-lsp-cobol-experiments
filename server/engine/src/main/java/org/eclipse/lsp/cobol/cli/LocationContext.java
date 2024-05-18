package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.lsp.cobol.core.CobolParser;

public class LocationContext {
    private ParserRuleContext node;
    private CobolParser.ProcedureSectionContext section;
    private CobolParser.ParagraphContext paragraph;

    public LocationContext(ParserRuleContext node, CobolParser.ProcedureSectionContext section, CobolParser.ParagraphContext paragraph) {
        this.node = node;
        this.section = section;
        this.paragraph = paragraph;
    }
}
