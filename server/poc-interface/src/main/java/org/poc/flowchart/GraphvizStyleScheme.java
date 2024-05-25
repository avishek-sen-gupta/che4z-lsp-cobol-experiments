package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.MutableNode;

public class GraphvizStyleScheme {
    private final Color background;
    private final Color fontColor;
    private final Shape shape;

    public GraphvizStyleScheme(Color backgroundFillColor, Color fontColor, Shape shape) {
        this.background = backgroundFillColor;
        this.fontColor = fontColor;
        this.shape = shape;
    }

    public GraphvizStyleScheme(Color backgroundFillColor, Color fontColor) {
        this(backgroundFillColor, fontColor, Shape.BOX);
    }

    public MutableNode apply(MutableNode node) {
        return node.add("style", "filled").add("fontcolor", fontColor.value).add("fillcolor", background.value).add("shape", shape.value);
    }
}
