package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.ArrayList;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class PerformProcedureChartNode extends CobolChartNode {

    private ChartNode inlineStatementContext;
    private List<ChartNode> procedures = new ArrayList<>();
    private ChartNode condition;

    public PerformProcedureChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public void buildInternalFlow() {
        CobolParser.PerformStatementContext performStatement = new SyntaxIdentity<CobolParser.PerformStatementContext>(getExecutionContext()).get();
        CobolParser.PerformProcedureStatementContext performProcedureStatementContext = performStatement.performProcedureStatement();
        if (isVarying()) {
            condition = nodeService.node(performProcedureStatementContext.performType(), this, new StackFrames());
        }

        if (performProcedureStatementContext != null) return;
        inlineStatementContext = nodeService.node(performStatement.performInlineStatement(), this, new StackFrames());
        inlineStatementContext.buildFlow();
    }

    private boolean isVarying() {
        CobolParser.PerformStatementContext performStatement = new SyntaxIdentity<CobolParser.PerformStatementContext>(getExecutionContext()).get();
        CobolParser.PerformProcedureStatementContext performProcedureStatementContext = performStatement.performProcedureStatement();
        return performProcedureStatementContext.performType() != null && performProcedureStatementContext.performType().performVarying() != null;
    }

    @Override
    public void buildOutgoingFlow() {
        // Call super here because this is still a normal statement which will continue its normal flow, after PERFORM returns
        super.buildOutgoingFlow();
    }

    @Override
    public void buildControlFlow() {
        CobolParser.PerformStatementContext performStatement = new SyntaxIdentity<CobolParser.PerformStatementContext>(getExecutionContext()).get();
        CobolParser.PerformProcedureStatementContext performProcedureStatementContext = performStatement.performProcedureStatement();
        CobolParser.ProcedureNameContext procedureNameContext = performProcedureStatementContext.procedureName();
        String procedureName = procedureNameContext.getText();
        System.out.println("Found a PERFORM, routing to " + procedureName);
        ChartNode startNode = nodeService.sectionOrParaWithName(procedureName);
        if (performStatement.performProcedureStatement().through() == null) {
            procedures.add(startNode);
        } else {
            CobolParser.ProcedureNameContext endProcedureNameContext = performStatement.performProcedureStatement().through().procedureName();
            ChartNode endNode = nodeService.sectionOrParaWithName(endProcedureNameContext.getText());
            procedures.addAll(allProcedures(startNode, endNode));
        }
    }

    private List<ChartNode> allProcedures(ChartNode startProcedure, ChartNode endProcedure) {
        ChartNode current = startProcedure;
        List<ChartNode> allInclusiveProcedures = new ArrayList<>();
        while (current != endProcedure && !current.getOutgoingNodes().isEmpty()) {
            allInclusiveProcedures.add(current);
            current = current.getOutgoingNodes().getFirst();
        }
        allInclusiveProcedures.add(endProcedure);
        return allInclusiveProcedures;
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {
        super.acceptUnvisited(visitor, level);
        if (inlineStatementContext != null) {
            visitor.visitParentChildLink(this, inlineStatementContext, new VisitContext(level), nodeService);
            inlineStatementContext.accept(visitor, level);
            return;
        }

        procedures.forEach(p -> visitor.visitControlTransfer(this, p, new VisitContext(level)));

        if (isVarying()) {
            procedures.forEach(p -> visitor.visitControlTransfer(p, condition, new VisitContext(level)));
            visitor.visitControlTransfer(condition, this, new VisitContext(level));
        }
//        visitor.visitControlTransfer(this, targetNode, new VisitContext(level));
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
