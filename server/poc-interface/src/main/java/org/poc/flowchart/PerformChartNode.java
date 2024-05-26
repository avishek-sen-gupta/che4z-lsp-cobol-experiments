package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;
import poc.common.flowchart.ChartNodeVisitor;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class PerformChartNode extends CobolChartNode {

    private ChartNode inlineStatementContext;
    private ChartNode targetNode;

    public PerformChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.PerformStatementContext performStatement = new StatementIdentity<CobolParser.PerformStatementContext>(getExecutionContext()).get();
        CobolParser.PerformProcedureStatementContext performProcedureStatementContext = performStatement.performProcedureStatement();
        if (performProcedureStatementContext != null) return;
        inlineStatementContext = nodeService.node(performStatement.performInlineStatement());
        inlineStatementContext.buildFlow();
    }

    private boolean isVarying() {
        CobolParser.PerformStatementContext performStatement = new StatementIdentity<CobolParser.PerformStatementContext>(getExecutionContext()).get();
        CobolParser.PerformProcedureStatementContext performProcedureStatementContext = performStatement.performProcedureStatement();
        return performProcedureStatementContext == null;
    }

    @Override
    public void buildOutgoingFlow() {
        // Call super here because this is still a normal statement which will continue its normal flow
        super.buildOutgoingFlow();
        if (isVarying()) return;
        CobolParser.PerformStatementContext performStatement = new StatementIdentity<CobolParser.PerformStatementContext>(getExecutionContext()).get();
        CobolParser.PerformProcedureStatementContext performProcedureStatementContext = performStatement.performProcedureStatement();
        CobolParser.ProcedureNameContext procedureNameContext = performProcedureStatementContext.procedureName();
        String procedureName = procedureNameContext.getText();
        System.out.println("Found a PERFORM, routing to " + procedureName);
        targetNode = nodeService.sectionOrParaWithName(procedureName);
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.acceptUnvisited(visitor, level, maxLevel);
        if (inlineStatementContext != null) {
            visitor.visitParentChildLink(this, inlineStatementContext, nodeService);
            inlineStatementContext.accept(visitor, level, maxLevel);
            return;
        }
        visitor.visitControlTransfer(this, targetNode);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.PERFORM;
    }
}
