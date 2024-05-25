package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;
import org.flowchart.ChartNodeVisitor;

import java.util.List;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class GoToChartNode extends CobolChartNode {
    public GoToChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public void accept(ChartNodeVisitor visitor, int level, int maxLevel) {
        CobolParser.GoToStatementContext goToStatement = new StatementIdentity<CobolParser.GoToStatementContext>(getExecutionContext()).get();
        List<CobolParser.ProcedureNameContext> procedureNames = goToStatement.procedureName();
        System.out.println("Found a GO TO, routing to " + procedureNames);
        List<ChartNode> destinationNodes = procedureNames.stream().map(p -> nodeService.sectionOrParaWithName(p.paragraphName().getText())).collect(Collectors.toList());

        destinationNodes.forEach(destinationNode -> visitor.visitControlTransfer(this, destinationNode));

    }
}
