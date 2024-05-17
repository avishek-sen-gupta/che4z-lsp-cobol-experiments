package org.eclipse.lsp.cobol.cli;

import org.eclipse.lsp.cobol.core.CobolParser;

public class LocationContext {
    private CobolParser.ProcedureSectionContext section;
    private CobolParser.ParagraphContext paragraph;

    public LocationContext(CobolParser.ProcedureSectionContext section, CobolParser.ParagraphContext paragraph) {
        this.section = section;
        this.paragraph = paragraph;
    }
}
