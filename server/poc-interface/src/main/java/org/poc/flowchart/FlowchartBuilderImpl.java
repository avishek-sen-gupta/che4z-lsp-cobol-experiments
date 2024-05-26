package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.FlowchartBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class FlowchartBuilderImpl implements FlowchartBuilder {
    private final ChartNodeService chartNodeService;
    private ParseTree root;
    private CobolEntityNavigator cobolEntityNavigator;
    private MutableGraph graph;

    public FlowchartBuilderImpl(CobolEntityNavigator cobolEntityNavigator) {
        this.cobolEntityNavigator = cobolEntityNavigator;
        chartNodeService = new ChartNodeServiceImpl(cobolEntityNavigator);
        graph = Factory.mutGraph("example1").setDirected(true);
        Graphviz.useEngine(new GraphvizCmdLineEngine().timeout(5, java.util.concurrent.TimeUnit.HOURS));
    }

    @Override
    public FlowchartBuilder draw(List<ParseTree> roots, int maxLevel) {
        roots.forEach(r -> draw(r, maxLevel));
        return this;
    }

    @Override
    public FlowchartBuilder draw(ParseTree root, int maxLevel) {
        return buildChart(root, maxLevel);
    }

    @Override
    public FlowchartBuilder draw(List<ParseTree> roots) {
        return draw(roots, -1);
    }

    @Override
    public FlowchartBuilder draw(ParseTree root) {
        System.out.println("Assembling : " + root.getText());
        return draw(root, -1);
    }

    private FlowchartBuilder buildChart(ParseTree node, int maxLevel) {
        ChartNode rootChartNode = chartNodeService.node(node);
        rootChartNode.buildFlow();

//        MutableGraph g = mutGraph("example1").setDirected(true).graphAttrs().add("rankdir", "TB");
        ChartNodeGraphvizVisitor chartVisitor = new ChartNodeGraphvizVisitor(graph);
        rootChartNode.accept(chartVisitor, 1, maxLevel);
        return this;
    }

    @Override
    public FlowchartBuilder write(String dotFilePath) {
        try {
            Graphviz.fromGraph(graph).engine(Engine.DOT)
                    .render(Format.DOT)
                    .toFile(new File(dotFilePath));
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FlowchartBuilder outline(ParseTree groupRoot, String clusterLabel) {
        NodeCollector collectorVisitor = new NodeCollector(chartNodeService);
        groupRoot.accept(collectorVisitor);
        MutableGraph outliningCluster = mutGraph(clusterLabel).setCluster(true).graphAttrs().add("bgcolor", Color.LIGHTGREY.value);
        collectorVisitor.getCollectedNodes().forEach(n -> outliningCluster.add(mutNode(n.toString())));
        graph.add(outliningCluster);
        return this;
    }

    @Override
    public FlowchartBuilder connectToComment(String explanation, ParseTree symbol) {
        ChartNode explainedNode = chartNodeService.node(symbol);
        System.out.println(String.format("Linking EXPLANATION : %s to %s", explanation, explainedNode));
        graph.add(commentStyle(mutNode(formatted(explanation, 30))).addLink(mutNode(explainedNode.toString())));
        return this;
    }

    private String formatted(String s, int lineLength) {
        StringBuilder builder = new StringBuilder(s);
        int length = s.length();
        for (int i = lineLength; i < length; i += lineLength) {
            builder.insert(i, "\n");
        }
        return builder.toString();
    }

    private MutableNode commentStyle(MutableNode commentNode) {
        return commentNode.add("shape", Shape.BOX)
                .add("height", 4)
                .add("style", "filled")
                .add("fillcolor", Color.YELLOW.value);
    }

    public static FlowchartBuilder build(CobolEntityNavigator navigator) {
        return new FlowchartBuilderImpl(navigator);
    }
}
