package org.poc.flowchart;

import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class ChartNodeCompressionVisitor implements ChartNodeVisitor {
    private final ChartNode enclosingScope;
    private ChartNode tail;
    private final ChartNode head;
    private final ChartNodeCompressionVisitor parentVisitor;
    private List<ChartNodeVisitor> childVisitors = new ArrayList<>();

    public ChartNodeCompressionVisitor(ChartNode enclosingScope, ChartNodeCompressionVisitor parentVisitor) {
        this.enclosingScope = enclosingScope;
        tail = new GenericProcessingChartNode(null, enclosingScope);
        head = tail;
        this.parentVisitor = parentVisitor;
    }

    @Override
    public void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, ChartNodeService nodeService) {
        if (outgoingNodes.isEmpty()) return;
        if (node.getExecutionContext().getClass() == CobolParser.ParagraphContext.class ||
                node.getExecutionContext().getClass() == CobolParser.ProcedureSectionContext.class ||
                node.getExecutionContext().getClass() == CobolParser.ProcedureDivisionBodyContext.class ||
                node.getExecutionContext().getClass() == CobolParser.SentenceContext.class
        ) {
            // Simplification: There can only be one outgoing node at this point. If-Else conditions, etc. store their multiple branches in their own structures. There would be special accept() in those kinds of subclasses, since you shouldn't be merging those anyway, because they exist in their own scope.
            GenericProcessingChartNode decoratedCurrentNode = new GenericProcessingChartNode(node, enclosingScope);
            tail.goesTo(decoratedCurrentNode);
//            GenericProcessingChartNode successor = new GenericProcessingChartNode(outgoingNodes.getFirst(), enclosingScope);
//            decoratedCurrentNode.goesTo(successor);
            tail = decoratedCurrentNode;
        }
        else if (!isMergeable(node)) {
//            GenericProcessingChartNode successor = new GenericProcessingChartNode(outgoingNodes.getFirst(), enclosingScope);
            GenericProcessingChartNode decoratedCurrentNode = new GenericProcessingChartNode(node, enclosingScope);
            tail.goesTo(decoratedCurrentNode);
            tail = decoratedCurrentNode;
        }
        else {
//            tail.append(node);
            // The above will become a null operation
        }
    }

    private boolean isMergeable(ChartNode node) {
        if (node.getClass() == IfChartNode.class || node.getClass() == DialectStatementChartNode.class) {
            return false;
        }
        return true;
    }

    @Override
    public void visitCluster(ChartNode node, ChartNodeService nodeService) {

    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService) {
//        tail.append(internalTreeRoot);
    }

    @Override
    public void visitControlTransfer(ChartNode from, ChartNode to) {

    }

    @Override
    public ChartNodeVisitor newScope(ChartNode enclosingScope) {
        if (enclosingScope.getExecutionContext().getClass() == CobolParser.SentenceContext.class ||
                enclosingScope.getExecutionContext().getClass() == CobolParser.ParagraphsContext.class) return this;
        ChartNodeCompressionVisitor childVisitor = new ChartNodeCompressionVisitor(enclosingScope, this);
        childVisitors.add(childVisitor);
        return childVisitor;
    }
}
