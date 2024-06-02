package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.List;

public class SearchChartNode extends CobolChartNode {
    private ChartNode atEndBlock;
    private List<ChartNode> whenPhrases;

    public SearchChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.SearchStatementContext searchStatementContext = new SyntaxIdentity<CobolParser.SearchStatementContext>(executionContext).get();
        atEndBlock = nodeService.node(searchStatementContext.atEndPhrase(), this);
        List<CobolParser.SearchWhenContext> searchWhenContexts = searchStatementContext.searchWhen();
        whenPhrases = searchWhenContexts.stream().map(when -> nodeService.node(when, this)).toList();

        atEndBlock.buildFlow();
        whenPhrases.forEach(ChartNode::buildFlow);
    }

    @Override
    public void buildOutgoingFlow() {
        // Call super here because this is still a normal statement which will continue its normal flow, after PERFORM returns
        super.buildOutgoingFlow();
    }

    @Override
    public void buildControlFlow() {
        atEndBlock.buildControlFlow();
        whenPhrases.forEach(ChartNode::buildControlFlow);
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {
        super.acceptUnvisited(visitor, level);
        whenPhrases.forEach(w -> w.acceptUnvisited(visitor, level));
        whenPhrases.forEach(w -> visitor.visitParentChildLink(this, w, new VisitContext(level), nodeService));

        atEndBlock.accept(visitor, level);
        visitor.visitParentChildLink(this, atEndBlock, new VisitContext(level), nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.SEARCH;
    }

    @Override
    public String label() {
        CobolParser.SearchStatementContext searchStatementContext = new SyntaxIdentity<CobolParser.SearchStatementContext>(executionContext).get();
        return searchStatementContext.SEARCH().getText() + "\n" + searchStatementContext.qualifiedDataName().getText();
//        return CobolContextAugmentedTreeNode.originalText(executionContext, CobolEntityNavigator::PASSTHROUGH);
    }

    @Override
    public void reset() {
        super.reset();
        atEndBlock.reset();
        whenPhrases.forEach(ChartNode::reset);
    }
}
