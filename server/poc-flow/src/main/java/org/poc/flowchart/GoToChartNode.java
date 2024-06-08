package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.List;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class GoToChartNode extends CobolChartNode {

    private List<ChartNode> destinationNodes;

    public GoToChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public void buildOutgoingFlow() {
        super.buildOutgoingFlow();
    }

    @Override
    public String label() {
        return originalText();
    }

    @Override
    public void buildControlFlow() {
        CobolParser.GoToStatementContext goToStatement = new SyntaxIdentity<CobolParser.GoToStatementContext>(getExecutionContext()).get();
        List<CobolParser.ProcedureNameContext> procedureNames = goToStatement.procedureName();
        System.out.println("Found a GO TO, routing to " + procedureNames);
        destinationNodes = procedureNames.stream().map(p -> nodeService.sectionOrParaWithName(p.paragraphName().getText())).collect(Collectors.toList());
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {
        super.acceptUnvisited(visitor, level);
        // If you want two nodes to the destination, uncomment below. The super already does the flow work.
        destinationNodes.forEach(destinationNode -> visitor.visitControlTransfer(this, destinationNode, new VisitContext(level)));
    }

    @Override
    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl) {
        return interpreter.scope(this).executeGoto(destinationNodes, nodeService, this);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.CONTROL_FLOW;
    }
}
