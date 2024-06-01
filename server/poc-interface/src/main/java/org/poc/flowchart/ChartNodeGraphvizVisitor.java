package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import org.eclipse.lsp.cobol.core.CobolParser;
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
        List<ChartNode> targets = outgoingNodes.stream().map(ChartNode::passthrough).map(overlay::block).toList();

        targets.forEach(t -> {
            System.out.println("Linking " + node + " to " + t);
            if (source == t) return;
            MutableNode graphSource = styled(source, mutNode(source.toString()).add("label", source.shortLabel()));
            MutableNode graphTarget = styled(t, mutNode(t.toString()).add("label", t.shortLabel()));
            g.add(graphSource.addLink(graphSource.linkTo(graphTarget).with("penwidth", "3")));
        });

        if (node.accessesDatabase()) {
            g.add(mutNode(source.toString()).addLink(mutNode("IDMS Database")
                    .add("shape", "cylinder")
                    .add("height", "4")
                    .add("width", "4")
                    .add("style", "filled")
                    .add("fillcolor", Color.LIGHTBLUE2.value)
                    .add("penwidth", "4")
            ));
        }
    }

    public void visitCluster(ChartNode compositeNode, ChartNodeService nodeService) {
    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService) {
        visitParentChildLink(parent, internalTreeRoot, nodeService, ChartNodeCondition.ALWAYS_SHOW);
    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService, ChartNodeCondition hideStrategy) {
        ChartNode overlayParent = overlay.block(parent);
        if (overlayParent.getClass() == GenericProcessingChartNode.class) return;
        ChartNode passthroughTarget = internalTreeRoot.passthrough();
        ChartNode overlayInternalTreeRoot = overlay.block(passthroughTarget);
        MutableNode graphParent = styled(overlayParent, mutNode(overlayParent.toString())).add("label", overlayParent.shortLabel());
        MutableNode graphChild = mutNode(overlayInternalTreeRoot.toString()).add("label", overlayInternalTreeRoot.shortLabel());
        MutableNode child = styled(overlayInternalTreeRoot, graphChild);
//        if (overlayInternalTreeRoot.getExecutionContext() != null && overlayInternalTreeRoot.getExecutionContext().getClass() == CobolParser.ConditionalStatementCallContext.class)
        String arrowStyle = hideStrategy.apply(overlayInternalTreeRoot) ? "none" : "normal";
        g.add(graphParent.addLink(graphParent.linkTo(child).with("style", "dashed").with("arrowhead", arrowStyle)));
    }

    @Override
    public void visitControlTransfer(ChartNode from, ChartNode to) {
        ChartNode overlayFrom = overlay.block(from.passthrough());
        ChartNode overlayTo = overlay.block(to.passthrough());
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
