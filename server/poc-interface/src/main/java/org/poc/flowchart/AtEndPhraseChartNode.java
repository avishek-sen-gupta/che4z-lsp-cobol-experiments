package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.ArrayList;
import java.util.List;

public class AtEndPhraseChartNode extends CompositeCobolNode {
    private List<ChartNode> conditionalStatements = new ArrayList<>();
    public AtEndPhraseChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
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
