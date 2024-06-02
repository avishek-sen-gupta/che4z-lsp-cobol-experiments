package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.model.MutableGraph;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.List;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class PerformInlineChartNode extends CompositeCobolNode {
    private ChartNode condition;

    public PerformInlineChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.PerformStatementContext performStatement = new SyntaxIdentity<CobolParser.PerformStatementContext>(getExecutionContext()).get();
        CobolParser.PerformInlineStatementContext x = performStatement.performInlineStatement();
        if (isVarying(x)) {
            condition = nodeService.node(x.performType(), this);
        }
        super.buildInternalFlow();
    }

    private boolean isVarying(CobolParser.PerformInlineStatementContext performStatement) {
        return performStatement.performType() != null;
    }

    @Override
    public void buildOutgoingFlow() {
        // Call super here because this is still a normal statement which will continue its normal flow, after PERFORM returns
        super.buildOutgoingFlow();
    }

    @Override
    public List<? extends ParseTree> getChildren() {
        CobolParser.PerformStatementContext performStatement = new SyntaxIdentity<CobolParser.PerformStatementContext>(getExecutionContext()).get();
        CobolParser.PerformInlineStatementContext x = performStatement.performInlineStatement();
        return x.conditionalStatementCall();
    }

    @Override
    public void buildControlFlow() {
        CobolParser.PerformStatementContext performStatement = new SyntaxIdentity<CobolParser.PerformStatementContext>(getExecutionContext()).get();
        CobolParser.PerformInlineStatementContext x = performStatement.performInlineStatement();
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {
        super.acceptUnvisited(visitor, level);
        ChartNode tail = internalTreeRoot.tail();
        visitor.group(internalTreeRoot, this, this);
    }

    @Override
    public String label() {
        return truncated(originalText(), 30);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.PERFORM;
    }
}
