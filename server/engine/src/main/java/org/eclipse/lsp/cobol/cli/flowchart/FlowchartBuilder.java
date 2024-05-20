package org.eclipse.lsp.cobol.cli.flowchart;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.engine.GraphvizV8Engine;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.model.Factory.*;

public class FlowchartBuilder {
    private ParserRuleContext root;

    public FlowchartBuilder(ParserRuleContext root) {
        this.root = root;
    }

    public ChartNode run() {
        ParseTree compilationUnit = root.getChild(0);
        ParseTree programUnit = compilationUnit.getChild(0);
        ParseTree procedureDivision = programUnit.getChild(3);
        ParserRuleContext procedureDivisionBody = (ParserRuleContext) procedureDivision.getChild(3);
        return buildChart(procedureDivisionBody);

    }

    private ChartNode buildChart(ParserRuleContext node) {
        ChartNodeService chartNodeService = new ChartNodeService();
        ChartNode chartNode = new ChartNode(node, chartNodeService);
        chartNode.buildFlow();
        chartNode.smushNonEssentialNodes();

        Graphviz.useEngine(new GraphvizCmdLineEngine());
        MutableGraph g = mutGraph("example1").setDirected(true).graphAttrs().add("rankdir", "TB");
        GraphMaker graphMaker = new GraphMaker(g);
        chartNode.accept(graphMaker, 1);
        try {
            Graphviz.fromGraph(g).height(10000).render(Format.PNG).toFile(new File("/Users/asgupta/Downloads/mbrdi-poc/flowchart.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return chartNode;
    }
}
