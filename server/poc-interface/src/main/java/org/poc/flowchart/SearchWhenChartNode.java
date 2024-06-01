package org.poc.flowchart;

import com.google.common.collect.ImmutableList;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.*;

import java.util.List;

public class SearchWhenChartNode extends CompositeCobolNode {
    private ChartNode condition;
//    private List<ChartNode> conditionalStatements;

    public SearchWhenChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.SearchWhenContext searchWhenStatementContext = (CobolParser.SearchWhenContext) executionContext;
        condition = nodeService.node(searchWhenStatementContext.condition(), this);
        condition.buildFlow();
        super.buildInternalFlow();
    }

    @Override
    public List<? extends ParseTree> getChildren() {
        CobolParser.SearchWhenContext searchWhenStatementContext = (CobolParser.SearchWhenContext) executionContext;
        if (searchWhenStatementContext.nextSentenceWrapperStatement() != null) {
            return ImmutableList.of(searchWhenStatementContext.nextSentenceWrapperStatement());
        }
        return searchWhenStatementContext.conditionalStatementCall();
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.acceptUnvisited(visitor, level, maxLevel);
        condition.acceptUnvisited(visitor, level, maxLevel);
        visitor.visitParentChildLink(this, condition, nodeService);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.SEARCH_WHEN;
    }

    @Override
    public void reset() {
        super.reset();
        condition.reset();
    }

    @Override
    public String name() {
        CobolParser.SearchWhenContext searchWhenStatementContext = (CobolParser.SearchWhenContext) executionContext;
        return "When\n" + CobolContextAugmentedTreeNode.originalText(searchWhenStatementContext.condition(), CobolEntityNavigator::PASSTHROUGH);
    }
}
