package org.poc.flowchart;

import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import org.antlr.v4.runtime.tree.ParseTree;
import org.poc.common.navigation.CobolEntityNavigator;
import org.flowchart.FlowchartBuilder;

import java.io.File;
import java.io.IOException;

public class FlowchartBuilderImpl implements FlowchartBuilder {
    private ParseTree root;
    private CobolEntityNavigator cobolEntityNavigator;

    public FlowchartBuilderImpl(ParseTree root, CobolEntityNavigator cobolEntityNavigator) {
        this.root = root;
        this.cobolEntityNavigator = cobolEntityNavigator;
    }

    @Override
    public CobolChartNode run(String dotFilePath, int maxLevel) {
        return buildChart(root, dotFilePath, maxLevel);
    }

    private CobolChartNode buildChart(ParseTree node, String dotFilePath, int maxLevel) {
        Graphviz.useEngine(new GraphvizCmdLineEngine().timeout(5, java.util.concurrent.TimeUnit.HOURS));
        ChartNodeServiceImpl chartNodeService = new ChartNodeServiceImpl(cobolEntityNavigator);
        CobolChartNode chartNode = new CobolChartNode(node, chartNodeService);
        chartNode.buildFlow();

//        MutableGraph g = mutGraph("example1").setDirected(true).graphAttrs().add("rankdir", "TB");
        MutableGraph g = Factory.mutGraph("example1").setDirected(true);
        ChartNodeVisitorImpl chartVisitor = new ChartNodeVisitorImpl(g);
        chartNode.accept(chartVisitor, 1, maxLevel);
        try {
            Graphviz.fromGraph(g).engine(Engine.DOT)
                    .render(Format.DOT)
                    .toFile(new File(dotFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return chartNode;
    }

    public static FlowchartBuilder build(ParseTree tree, CobolEntityNavigator navigator) {
        return new FlowchartBuilderImpl(tree, navigator);
    }
}
