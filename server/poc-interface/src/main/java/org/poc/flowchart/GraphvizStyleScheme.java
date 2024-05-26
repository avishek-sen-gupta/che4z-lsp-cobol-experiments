package org.poc.flowchart;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.MutableNode;
import poc.common.flowchart.DomainDocument;

public class GraphvizStyleScheme {
    private final Color background;
    private final Color fontColor;
    private final Shape shape;
    private final DomainDocument notes;

    public GraphvizStyleScheme(Color backgroundFillColor, Color fontColor, Shape shape) {
        this(backgroundFillColor, fontColor, shape, new DomainDocument());
    }
    public GraphvizStyleScheme(Color backgroundFillColor, Color fontColor, Shape shape, DomainDocument notes) {
        this.background = backgroundFillColor;
        this.fontColor = fontColor;
        this.shape = shape;
        this.notes = notes;
    }

    public GraphvizStyleScheme(Color backgroundFillColor, Color fontColor) {
        this(backgroundFillColor, fontColor, Shape.BOX);
    }

    public MutableNode apply(MutableNode node) {
        return node.add("style", "filled").add("fontcolor", fontColor.value).add("fillcolor", background.value).add("shape", shape.value).add("label", notes.getText());
    }
}
