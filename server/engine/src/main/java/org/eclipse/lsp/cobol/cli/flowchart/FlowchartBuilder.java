package org.eclipse.lsp.cobol.cli.flowchart;

import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.vm.CobolEntityNavigator;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.model.Factory.*;

public class FlowchartBuilder {
    private ParseTree root;
    private CobolEntityNavigator cobolEntityNavigator;

    public FlowchartBuilder(ParseTree root, CobolEntityNavigator cobolEntityNavigator) {
        this.root = root;
//        ParseTree compilationUnit = root.getChild(0);
//        ParseTree programUnit = compilationUnit.getChild(0);
//        ParseTree procedureDivision = programUnit.getChild(3);
//        procedureDivisionBody = (CobolParser.ProcedureDivisionBodyContext) procedureDivision.getChild(3);
        this.cobolEntityNavigator = cobolEntityNavigator;
    }

    public ChartNode run() {
        return buildChart(root);
    }

    private ChartNode buildChart(ParseTree node) {
        ChartNodeService chartNodeService = new ChartNodeService(cobolEntityNavigator);
        ChartNode chartNode = new ChartNode(node, chartNodeService);
        chartNode.buildFlow();

//        MutableGraph g = mutGraph("example1").setDirected(true).graphAttrs().add("rankdir", "TB");
        MutableGraph g = mutGraph("example1").setDirected(true);
        ChartVisitor chartVisitor = new ChartVisitor(g);
        chartNode.accept(chartVisitor, 1, -1);
        try {
            Graphviz.fromGraph(g).engine(Engine.NEATO).height(10000).render(Format.PNG).toFile(new File("/Users/asgupta/Downloads/mbrdi-poc/flowchart.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return chartNode;
    }
}
