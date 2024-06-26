package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.List;

public class GenericOnClauseChartNode extends CompositeCobolNode {

    private ChartNode condition;

    public GenericOnClauseChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.GenericOnClauseStatementContext onClause = new SyntaxIdentity<CobolParser.GenericOnClauseStatementContext>(executionContext).get();
        condition = nodeService.node(onClause.generalIdentifier(), this, staticFrameContext);
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
        return "ON\n" + NodeText.originalText(condition.getExecutionContext(), NodeText::PASSTHROUGH);
    }

    @Override
    public void linkParentToChild(ChartNodeVisitor visitor, int level) {
        visitor.visitParentChildLink(this, internalTreeRoot, new VisitContext(level), nodeService, CHILD_IS_CONDITIONAL_STATEMENT);
    }

}
