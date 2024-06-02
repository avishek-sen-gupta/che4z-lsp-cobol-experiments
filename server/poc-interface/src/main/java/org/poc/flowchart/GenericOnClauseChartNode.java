package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.List;

public class GenericOnClauseChartNode extends CompositeCobolNode {

    private ChartNode condition;

    public GenericOnClauseChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.GenericOnClauseStatementContext onClause = new SyntaxIdentity<CobolParser.GenericOnClauseStatementContext>(executionContext).get();
        condition = nodeService.node(onClause.generalIdentifier(), this);
        super.buildInternalFlow();
    }

    @Override
    public List<? extends ParseTree> getChildren() {
        CobolParser.GenericOnClauseStatementContext onClause = new SyntaxIdentity<CobolParser.GenericOnClauseStatementContext>(executionContext).get();
        return onClause.statement();
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.ON_CLAUSE;
    }

    @Override
    public String label() {
        return NodeText.originalText(condition.getExecutionContext(), NodeText::PASSTHROUGH);
    }

    @Override
    public void linkParentToChild(ChartNodeVisitor visitor) {
        visitor.visitParentChildLink(this, internalTreeRoot, nodeService, CHILD_IS_CONDITIONAL_STATEMENT);
    }

    @Override
    public void reset() {
        super.reset();
        condition.reset();
    }

}
