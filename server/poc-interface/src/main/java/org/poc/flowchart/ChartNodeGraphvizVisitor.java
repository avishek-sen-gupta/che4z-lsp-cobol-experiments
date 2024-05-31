package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import poc.common.flowchart.*;

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
        if (node.isPassthrough()) return;
        System.out.println("Visiting : " + node);
        ChartNode source = overlay.block(node);
        List<ChartNode> targets = outgoingNodes.stream().map(overlay::block).toList();

        targets.forEach(t -> {
            System.out.println("Linking " + node + " to " + t);
            if (source == t) return;
            MutableNode graphSource = mutNode(source.toString()).add("label", source.shortLabel());
            ChartNode passthroughTarget = t.passthrough();
            MutableNode graphTarget = mutNode(passthroughTarget.toString()).add("label", passthroughTarget.shortLabel());
            g.add(styled(source, graphSource).addLink(styled(passthroughTarget, graphTarget)));
        });

        if (node.accessesDatabase()) {
            g.add(mutNode(source.toString()).addLink(mutNode("IDMS Database").add("shape", "cylinder")));
        }
    }

    public void visitCluster(ChartNode compositeNode, ChartNodeService nodeService) {
    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService) {
        ChartNode overlayParent = overlay.block(parent);
        if (overlayParent.getClass() == GenericProcessingChartNode.class) return;
        ChartNode overlayInternalTreeRoot = overlay.block(internalTreeRoot);
        MutableNode graphParent = styled(overlayParent, mutNode(overlayParent.toString())).add("label", overlayParent.shortLabel());
        ChartNode passthroughTarget = overlayInternalTreeRoot.passthrough();
//        ChartNode passthroughTarget = overlayInternalTreeRoot;
        MutableNode graphChild = mutNode(passthroughTarget.toString()).add("label", passthroughTarget.shortLabel());
        MutableNode child = styled(passthroughTarget, graphChild);
        g.add(graphParent.addLink(graphParent.linkTo(child).with("style", "dashed")));
    }

    @Override
    public void visitControlTransfer(ChartNode from, ChartNode to) {
        ChartNode overlayFrom = overlay.block(from);
        ChartNode overlayTo = overlay.block(to);
        MutableNode origin = styled(overlayFrom, mutNode(overlayFrom.toString()));
        MutableNode destination = styled(overlayTo, mutNode(overlayTo.toString()));
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
