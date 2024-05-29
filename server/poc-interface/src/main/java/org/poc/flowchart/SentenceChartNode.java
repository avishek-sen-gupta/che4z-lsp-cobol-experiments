package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

public class SentenceChartNode extends CompositeCobolNode {
    public SentenceChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.SENTENCE;
    }

    @Override
    public boolean isMergeable() {
        CobolParser.SentenceContext e = (CobolParser.SentenceContext) executionContext;
        return e.statement().size() == 1 && nodeService.node(e.statement(0)).isMergeable();
    }
}
