package org.eclipse.lsp.cobol.cli.flowchart;

import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.vm.CobolEntityNavigator;
import org.eclipse.lsp.cobol.cli.vm.CobolEntityNavigatorFactory;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.model.Factory.*;

public class FlowchartBuilder {
    private final CobolParser.ProcedureDivisionBodyContext procedureDivisionBody;
    private ParserRuleContext root;
    private CobolEntityNavigator cobolEntityNavigator;

    public FlowchartBuilder(ParserRuleContext root) {
        this.root = root;
        ParseTree compilationUnit = root.getChild(0);
        ParseTree programUnit = compilationUnit.getChild(0);
        ParseTree procedureDivision = programUnit.getChild(3);
        procedureDivisionBody = (CobolParser.ProcedureDivisionBodyContext) procedureDivision.getChild(3);
        this.cobolEntityNavigator = CobolEntityNavigatorFactory.entityNavigator(procedureDivisionBody);
    }

    public ChartNode run() {
        return buildChart(procedureDivisionBody);
    }

    private ChartNode buildChart(ParserRuleContext node) {
        ChartNodeService chartNodeService = new ChartNodeService(cobolEntityNavigator);
        ChartNode chartNode = new ChartNode(node, chartNodeService);
        chartNode.buildFlow();
        chartNode.smushNonEssentialNodes();

        Graphviz.useEngine(new GraphvizCmdLineEngine());
        MutableGraph g = mutGraph("example1").setDirected(true).graphAttrs().add("rankdir", "TB");
        ChartVisitor chartVisitor = new ChartVisitor(g);
        chartNode.accept(chartVisitor, 1);
        try {
            Graphviz.fromGraph(g).engine(Engine.NEATO).height(10000).render(Format.PNG).toFile(new File("/Users/asgupta/Downloads/mbrdi-poc/flowchart.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return chartNode;
    }
}
