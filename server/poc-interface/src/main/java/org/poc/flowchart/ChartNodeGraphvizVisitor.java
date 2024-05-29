package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeVisitor;
import poc.common.flowchart.ChartOverlay;

import java.util.List;

import static guru.nidi.graphviz.model.Factory.*;

public class ChartNodeGraphvizVisitor implements ChartNodeVisitor {
    private MutableGraph g;
    private final ChartOverlay overlay;

    public ChartNodeGraphvizVisitor(MutableGraph g, ChartOverlay overlay) {
        this.g = g;
        this.overlay = overlay;
    }

    public void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, ChartNodeService nodeService) {
        System.out.println("Visiting : " + node);
        outgoingNodes.forEach(o -> {
            System.out.println("Linking " + node + " to " + o);
            g.add(styled(node, mutNode(node.toString())).addLink(mutNode(o.toString())));
        });

        if (node.accessesDatabase()) {
            g.add(mutNode(node.toString()).addLink(mutNode("IDMS Database").add("shape", "cylinder")));
        }
    }

    public void visitCluster(ChartNode compositeNode, ChartNodeService nodeService) {
    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService) {
        MutableNode o = styled(parent, mutNode(parent.toString()));
        MutableNode child = styled(internalTreeRoot, mutNode(internalTreeRoot.toString()));
        g.add(o.add(Color.RED).addLink(o.linkTo(child).with("style", "dashed")));
    }

    @Override
    public void visitControlTransfer(ChartNode from, ChartNode to) {
        MutableNode origin = styled(from, mutNode(from.toString()));
        MutableNode destination = styled(to, mutNode(to.toString()));
        g.add(origin.addLink(origin.linkTo(destination).with("style", "bold").with("color", "blueviolet")));
    }

    @Override
    public ChartNodeVisitor newScope(ChartNode enclosingScope) {
        return this;
    }

    private MutableNode styled(ChartNode chartNode, MutableNode node) {
        return FlowchartStylePreferences.scheme(chartNode).apply(node);
    }
}
