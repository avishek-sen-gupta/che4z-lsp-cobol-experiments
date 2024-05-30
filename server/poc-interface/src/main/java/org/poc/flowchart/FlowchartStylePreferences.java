package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeType;

import java.util.HashMap;

public class FlowchartStylePreferences {
    private static final HashMap<ChartNodeType, GraphvizStyleScheme> schemes = new HashMap<>();
    static {
        schemes.put(ChartNodeType.GENERIC_PROCESSING, new GraphvizStyleScheme(Color.WHEAT, Color.BLACK));
        schemes.put(ChartNodeType.ATOMIC, new GraphvizStyleScheme(Color.WHEAT, Color.BLACK, Shape.POINT));
        schemes.put(ChartNodeType.GENERIC_STATEMENT, new GraphvizStyleScheme(Color.WHEAT, Color.BLACK, Shape.INV_HOUSE));
        schemes.put(ChartNodeType.PARAGRAPHS, new GraphvizStyleScheme(Color.WHEAT, Color.BLACK, Shape.POINT));
        schemes.put(ChartNodeType.SENTENCE, new GraphvizStyleScheme(Color.WHEAT, Color.BLACK, Shape.POINT));
        schemes.put(ChartNodeType.COMPOSITE, new GraphvizStyleScheme(Color.WHEAT, Color.BLACK));
        schemes.put(ChartNodeType.SECTION, new GraphvizStyleScheme(Color.DEEPSKYBLUE4, Color.WHITE));
        schemes.put(ChartNodeType.PARAGRAPH, new GraphvizStyleScheme(Color.DARKSEAGREEN4, Color.WHITE));
        schemes.put(ChartNodeType.DIALECT, new GraphvizStyleScheme(Color.BLUE4, Color.WHITE, Shape.FOLDER));
        schemes.put(ChartNodeType.GOTO, new GraphvizStyleScheme(Color.DARKGREEN, Color.WHITE, Shape.INV_HOUSE));
        schemes.put(ChartNodeType.CONTROL_FLOW, new GraphvizStyleScheme(Color.DARKORANGE2, Color.WHITE, Shape.R_ARROW));
        schemes.put(ChartNodeType.PERFORM, new GraphvizStyleScheme(Color.DARKVIOLET, Color.WHITE, Shape.CDS));
        schemes.put(ChartNodeType.IF_BRANCH, new GraphvizStyleScheme(Color.CHOCOLATE4, Color.WHITE, Shape.DIAMOND));
        schemes.put(ChartNodeType.CONDITIONAL_STATEMENT, new GraphvizStyleScheme(Color.CHOCOLATE4, Color.WHITE, Shape.POINT));
        schemes.put(ChartNodeType.CONDITION_CLAUSE, new GraphvizStyleScheme(Color.CHOCOLATE4, Color.WHITE, Shape.POINT));
        schemes.put(ChartNodeType.IF_YES, new GraphvizStyleScheme(Color.DARKGREEN, Color.WHITE));
        schemes.put(ChartNodeType.IF_NO, new GraphvizStyleScheme(Color.RED, Color.WHITE));
        schemes.put(ChartNodeType.DUMMY, new GraphvizStyleScheme(Color.DARKSLATEGRAY4, Color.WHITE));
    }

    public static GraphvizStyleScheme scheme(ChartNode node) {
        return schemes.get(node.type());
//        if (node.getNotes().isEmpty()) return schemes.get(node.type());
//        return new GraphvizStyleScheme(Color.AZURE4, Color.BLACK, Shape.NOTE, node.getNotes());
    }
}
