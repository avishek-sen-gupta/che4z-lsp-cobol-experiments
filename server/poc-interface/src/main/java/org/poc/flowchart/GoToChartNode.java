package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;
import poc.common.flowchart.ChartNodeVisitor;

import java.util.List;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class GoToChartNode extends CobolChartNode {

    private List<ChartNode> destinationNodes;

    public GoToChartNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public void buildOutgoingFlow() {
        // Don't call super here
        CobolParser.GoToStatementContext goToStatement = new StatementIdentity<CobolParser.GoToStatementContext>(getExecutionContext()).get();
        List<CobolParser.ProcedureNameContext> procedureNames = goToStatement.procedureName();
        System.out.println("Found a GO TO, routing to " + procedureNames);
        destinationNodes = procedureNames.stream().map(p -> nodeService.sectionOrParaWithName(p.paragraphName().getText())).collect(Collectors.toList());
        outgoingNodes.addAll(destinationNodes);
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.acceptUnvisited(visitor, level, maxLevel);
        // If you want two nodes to the destination, uncomment below. The super already does the flow work.
//        destinationNodes.forEach(destinationNode -> visitor.visitControlTransfer(this, destinationNode));
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.CONTROL_FLOW;
    }
}
