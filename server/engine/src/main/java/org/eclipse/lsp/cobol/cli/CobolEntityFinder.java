package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

public class CobolEntityFinder<T> {
    private Class<T> clazz;

    public CobolEntityFinder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void recurse(ParseTree parentNode, List<T> entities) {
        for (int i = 0; i <= parentNode.getChildCount() - 1; i++) {
            ParseTree child = parentNode.getChild(i);
            if (child.getClass() != clazz) {
                recurse(child, entities);
                continue;
            }
            entities.add((T) child);
        }
    }

    public LocationContext context(ParserRuleContext node) {
        ParserRuleContext parent = node.getParent();
        while (parent != null && parent.getClass() != CobolParser.ParagraphContext.class) {
            parent = parent.getParent();
        }

        CobolParser.ParagraphContext paragraph = (CobolParser.ParagraphContext) parent;
        parent = node.getParent();
        while (parent != null && parent.getClass() != CobolParser.ProcedureSectionContext.class) {
            parent = parent.getParent();
        }
        CobolParser.ProcedureSectionContext section = (CobolParser.ProcedureSectionContext) parent;
        return new LocationContext(node, section, paragraph);
    }
}
