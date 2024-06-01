package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;
import poc.common.flowchart.ChartNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class AtEndPhraseChartNode extends CompositeCobolNode {
    private List<ChartNode> conditionalStatements = new ArrayList<>();
    public AtEndPhraseChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public List<? extends ParseTree> getChildren() {
        CobolParser.AtEndPhraseContext atEndPhrase = (CobolParser.AtEndPhraseContext) executionContext;
        return atEndPhrase.conditionalStatementCall();
    }

    @Override
    public String label() {
        return "";
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.AT_END_PHRASE;
    }
}
