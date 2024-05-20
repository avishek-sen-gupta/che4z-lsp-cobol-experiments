package org.eclipse.lsp.cobol.cli.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;

import java.util.List;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.attribute.Color.*;
import static guru.nidi.graphviz.model.Factory.*;

public class GraphMaker implements ChartNodeVisitor {
    private MutableGraph g;

    public GraphMaker(MutableGraph g) {
        this.g = g;
    }

    @Override
    public void visit(ChartNode node) {
        System.out.println("Visiting : " + node);
        List<ChartNode> outgoingNodes = node.getOutgoingNodes();
        outgoingNodes.forEach(o -> {
            System.out.println("Linking " + node + " to " + o);
            g.add(mutNode(node.toString()).add(Color.RED).addLink(mutNode(o.toString())));
        });
    }

    @Override
    public void visitSpecific(ChartNode parent, ChartNode internalTreeRoot) {
        g.add(mutNode(parent.toString()).add(Color.RED).addLink(mutNode(internalTreeRoot.toString())));
    }
}
