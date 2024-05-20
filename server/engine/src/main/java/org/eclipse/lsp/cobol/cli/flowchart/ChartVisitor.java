package org.eclipse.lsp.cobol.cli.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.model.MutableGraph;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.vm.CobolEntityNavigator;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.*;

public class ChartVisitor implements ChartNodeVisitor {
    private MutableGraph g;

    public ChartVisitor(MutableGraph g) {
        this.g = g;
    }

    @Override
    public void visit(ChartNode node, ChartNodeService nodeService) {
        System.out.println("Visiting : " + node);
        processControlStatement(node, nodeService);
        List<ChartNode> outgoingNodes = node.getOutgoingNodes();
        outgoingNodes.forEach(o -> {
            System.out.println("Linking " + node + " to " + o);
            g.add(mutNode(node.toString()).add(Color.RED).addLink(mutNode(o.toString())));
        });
    }

    private void processControlStatement(ChartNode node, ChartNodeService nodeService) {
        if (node.getExecutionContext().getClass() == CobolParser.StatementContext.class) {
            ParseTree typedStatement = node.getExecutionContext().getChild(0);
            if (typedStatement.getClass() == CobolParser.GoToStatementContext.class) {
                CobolParser.GoToStatementContext goToStatement = (CobolParser.GoToStatementContext) typedStatement;
                List<CobolParser.ProcedureNameContext> procedureNames = goToStatement.procedureName();
                System.out.println("Found a GO TO, routing to " + procedureNames);
                List<ChartNode> destinationNodes = procedureNames.stream().map(p -> nodeService.sectionOrParaWithName(p.paragraphName().getText())).collect(Collectors.toList());
                destinationNodes.forEach(destinationNode -> g.add(mutNode(node.toString()).add(Color.RED).addLink(mutNode(destinationNode.toString()))));
            }
            if (typedStatement.getClass() == CobolParser.PerformStatementContext.class) {
                CobolParser.PerformStatementContext performStatement = (CobolParser.PerformStatementContext) typedStatement;
                CobolParser.PerformProcedureStatementContext performProcedureStatementContext = performStatement.performProcedureStatement();
                if (performProcedureStatementContext == null) {
                    g.add(mutNode("PERFORM VARYING").add(Color.RED));
                    return;
                }
                CobolParser.ProcedureNameContext procedureName = performProcedureStatementContext.procedureName();
                System.out.println("Found a PERFORM, routing to " + procedureName);

                ChartNode targetNode = nodeService.sectionOrParaWithName(procedureName.getText());
                g.add(mutNode(node.toString()).add(Color.RED).addLink(mutNode(targetNode.toString())));
            }
        }
    }

    @Override
    public void visitSpecific(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService) {
        g.add(mutNode(parent.toString()).add(Color.RED).addLink(mutNode(internalTreeRoot.toString())));
    }
}
