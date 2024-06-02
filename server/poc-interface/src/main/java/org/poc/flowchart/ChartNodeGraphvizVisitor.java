package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import poc.common.flowchart.*;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static guru.nidi.graphviz.model.Factory.*;

public class ChartNodeGraphvizVisitor implements ChartNodeVisitor {
    int maxLevel = -1;
    private final Function<VisitContext, Boolean> visitCondition;
    private final MutableGraph g;
    private final ChartOverlay overlay;

    public ChartNodeGraphvizVisitor(MutableGraph g, ChartOverlay overlay, Function<VisitContext, Boolean> stopCondition) {
        this.g = g;
        this.overlay = overlay;
        this.visitCondition = stopCondition;
    }

    public void visit(ChartNode node, List<ChartNode> outgoingNodes, List<ChartNode> incomingNodes, VisitContext visitContext, ChartNodeService nodeService) {
        if (!visitCondition.apply(visitContext)) return;
        if (node.isPassthrough()) return;
        System.out.println("Visiting : " + node);
        ChartNode source = overlay.block(node);
        List<ChartNode> targets = outgoingNodes.stream().map(ChartNode::passthrough).map(overlay::block).toList();

        targets.forEach(t -> {
            System.out.println("Linking " + node + " to " + t);
            if (source == t) return;
            MutableNode graphSource = styled(source, mutNode(source.id()).add("label", source.label()));
            MutableNode graphTarget = styled(t, mutNode(t.id()).add("label", t.label()));
            g.add(graphSource.addLink(graphSource.linkTo(graphTarget).with("penwidth", "3")));
        });

        if (node.accessesDatabase()) {
            g.add(mutNode(source.id()).addLink(mutNode("IDMS Database")
                    .add("shape", "cylinder")
                    .add("height", "4")
                    .add("width", "4")
                    .add("style", "filled")
                    .add("fillcolor", Color.LIGHTBLUE2.value)
                    .add("penwidth", "4")
            ));
        }
    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, VisitContext visitContext, ChartNodeService nodeService) {
        if (!visitCondition.apply(visitContext.oneLower())) return;
        visitParentChildLink(parent, internalTreeRoot, visitContext, nodeService, ChartNodeCondition.ALWAYS_SHOW);
    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, VisitContext visitContext, ChartNodeService nodeService, ChartNodeCondition hideStrategy) {
        if (!visitCondition.apply(visitContext)) return;
        ChartNode overlayParent = overlay.block(parent);
        if (overlayParent.getClass() == GenericProcessingChartNode.class) return;
        ChartNode passthroughTarget = internalTreeRoot.passthrough();
        ChartNode overlayInternalTreeRoot = overlay.block(passthroughTarget);
        MutableNode graphParent = styled(overlayParent, mutNode(overlayParent.id())).add("label", overlayParent.label());
        MutableNode graphChild = mutNode(overlayInternalTreeRoot.id()).add("label", overlayInternalTreeRoot.label());
        MutableNode child = styled(overlayInternalTreeRoot, graphChild);
//        if (overlayInternalTreeRoot.getExecutionContext() != null && overlayInternalTreeRoot.getExecutionContext().getClass() == CobolParser.ConditionalStatementCallContext.class)
        String arrowStyle = hideStrategy.apply(overlayInternalTreeRoot) ? "none" : "normal";
        g.add(graphParent.addLink(graphParent.linkTo(child).with("style", "dashed").with("arrowhead", arrowStyle)));
    }

    @Override
    public void visitControlTransfer(ChartNode from, ChartNode to, VisitContext visitContext) {
        if (!visitCondition.apply(visitContext)) return;
        ChartNode overlayFrom = overlay.block(from.passthrough());
        ChartNode overlayTo = overlay.block(to.passthrough());
        MutableNode origin = styled(overlayFrom, mutNode(overlayFrom.id())).add("label", overlayFrom.label());
        MutableNode destination = styled(overlayTo, mutNode(overlayTo.id())).add("label", overlayTo.label());
        g.add(origin.addLink(origin.linkTo(destination).with("style", "bold").with("color", "blueviolet")));
    }

    @Override
    public ChartNodeVisitor newScope(ChartNode enclosingScope) {
        return this;
    }

    @Override
    public void group(ChartNode root) {
        ChartNodeCollectorVisitor collector = new ChartNodeCollectorVisitor(overlay);
        root.accept(collector, -1);
        List<ChartNode> chartNodes = collector.getChartNodes();
        String clusterID = String.format("cluster_%s", UUID.randomUUID());
        MutableGraph outliningCluster = mutGraph(clusterID).setCluster(true).graphAttrs().add("bgcolor", Color.LIGHTGREY.value);
        chartNodes.forEach(n -> {
            outliningCluster.add(mutNode(n.id()));
        });
        g.add(outliningCluster);
    }

    private MutableNode styled(ChartNode chartNode, MutableNode node) {
        return FlowchartStylePreferences.scheme(chartNode).apply(node);
    }
}
