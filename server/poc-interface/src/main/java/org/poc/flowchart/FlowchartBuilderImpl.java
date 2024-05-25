package org.poc.flowchart;

import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import org.antlr.v4.runtime.tree.ParseTree;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;
import org.poc.common.navigation.CobolEntityNavigator;
import org.flowchart.FlowchartBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FlowchartBuilderImpl implements FlowchartBuilder {
    private ParseTree root;
    private CobolEntityNavigator cobolEntityNavigator;
    private MutableGraph graph;

    public FlowchartBuilderImpl(CobolEntityNavigator cobolEntityNavigator) {
        this.cobolEntityNavigator = cobolEntityNavigator;
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
        return draw(root, -1);
    }

    private FlowchartBuilder buildChart(ParseTree node, int maxLevel) {
        ChartNodeService chartNodeService = new ChartNodeServiceImpl(cobolEntityNavigator);
        ChartNode rootChartNode = chartNodeService.node(node);
        rootChartNode.buildFlow();

//        MutableGraph g = mutGraph("example1").setDirected(true).graphAttrs().add("rankdir", "TB");
        ChartNodeVisitorImpl chartVisitor = new ChartNodeVisitorImpl(graph);
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

    public static FlowchartBuilder build(CobolEntityNavigator navigator) {
        return new FlowchartBuilderImpl(navigator);
    }
}
