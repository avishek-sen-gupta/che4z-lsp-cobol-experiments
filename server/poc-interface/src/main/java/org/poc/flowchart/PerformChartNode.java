package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;
import org.flowchart.ChartNodeVisitor;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class PerformChartNode extends CobolChartNode {
    public PerformChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public void accept(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.accept(visitor, level, maxLevel);
        CobolParser.PerformStatementContext performStatement = new StatementIdentity<CobolParser.PerformStatementContext>(getExecutionContext()).get();
        CobolParser.PerformProcedureStatementContext performProcedureStatementContext = performStatement.performProcedureStatement();
        if (performProcedureStatementContext == null) {
//            g.add(Factory.mutNode("PERFORM VARYING").add(Color.RED));
            return;
        }
        CobolParser.ProcedureNameContext procedureNameContext = performProcedureStatementContext.procedureName();
        String procedureName = procedureNameContext.getText();
        System.out.println("Found a PERFORM, routing to " + procedureName);
        ChartNode targetNode = nodeService.sectionOrParaWithName(procedureName);

        visitor.visitControlTransfer(this, targetNode);
    }
}
