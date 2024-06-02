package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import org.antlr.v4.runtime.tree.ParseTree;
import poc.common.flowchart.*;
import org.poc.common.navigation.CobolEntityNavigator;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class FlowchartBuilderImpl implements FlowchartBuilder {
    private final ChartNodeService chartNodeService;
    private ChartNode graphRoot;
    private CobolEntityNavigator cobolEntityNavigator;
    private MutableGraph graph;
    private ChartOverlay overlay;

    public FlowchartBuilderImpl(CobolEntityNavigator cobolEntityNavigator) {
        this.cobolEntityNavigator = cobolEntityNavigator;
        chartNodeService = new ChartNodeServiceImpl(cobolEntityNavigator);
        graph = Factory.mutGraph("example1").setDirected(true).setCluster(true);
        Graphviz.useEngine(new GraphvizCmdLineEngine().timeout(5, java.util.concurrent.TimeUnit.HOURS));
    }

    @Override
    public FlowchartBuilder buildDotStructure(Function<VisitContext, Boolean> stopCondition) {
        return buildChartGraphic(stopCondition);
    }

    @Override
    public FlowchartBuilder buildDotStructure() {
        return buildDotStructure(VisitContext::ALWAYS_VISIT);
    }

    @Override
    public FlowchartBuilder buildChartAST(ParseTree node) {
        ChartNode rootChartNode = chartNodeService.node(node, null);
        rootChartNode.buildFlow();
        graphRoot = rootChartNode;
        return this;
    }

    private FlowchartBuilder buildChartGraphic(Function<VisitContext, Boolean> stopCondition) {
        ChartNode rootChartNode = graphRoot;
        rootChartNode.reset();
        ChartNodeVisitor chartVisitor = new ChartNodeGraphvizVisitor(graph, overlay, stopCondition);
        rootChartNode.accept(chartVisitor, 1);
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
        ChartNode explainedNode = chartNodeService.existingNode(symbol);
        if (explainedNode == null) return this;
        System.out.println(String.format("Linking EXPLANATION : %s to %s", explanation, explainedNode));
        MutableNode explanationNode = mutNode(formatted(explanation, 30));
        MutableNode explainedTarget = mutNode(explainedNode.toString());
        graph.add(explanationNode.addLink(explanationNode.linkTo(explainedTarget).with("color", Color.LIGHTGREY.value)));
        return this;
    }

    @Override
    public FlowchartBuilder createComment(String comment) {
        graph.add(commentStyle(mutNode(formatted(comment, 30))));
        return this;
    }

    @Override
    public FlowchartBuilder buildOverlay() {
//        ChartNode rootChartNode = chartNodeService.existingNode(node);
        ChartNode rootChartNode = graphRoot;
        rootChartNode.reset();
//        ChartNodeRuleVisitor compressionVisitor = new ChartNodeRuleVisitor(rootChartNode, rules);
        ChartNodeOverlayVisitor compressionVisitor = new ChartNodeOverlayVisitor(rootChartNode);
        rootChartNode.accept(compressionVisitor, 1);
        compressionVisitor.report();
//        compressionVisitor.applyRules();
        overlay = compressionVisitor.overlay();
        return this;
    }

    @Override
    public FlowchartBuilder buildControlFlow() {
        ChartNode rootChartNode = graphRoot;
        rootChartNode.reset();
        rootChartNode.accept(new ChartNodeControlFlowVisitor(), 1);
        return this;
    }

    @Override
    public void generateFlowchart(ParseTree procedure, String dotFilePath, String imageOutputPath, String splineStyle) throws IOException, InterruptedException {
        buildChartAST(procedure)
        .buildControlFlow()
        .buildOverlay()
//        .buildDotStructure(VisitContext.VISIT_UPTO_LEVEL(4)) // Level 4 for only sections and paragraphs
        .buildDotStructure()
        .write(dotFilePath);
        new GraphGenerator(splineStyle).generateImage(dotFilePath, imageOutputPath);

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
