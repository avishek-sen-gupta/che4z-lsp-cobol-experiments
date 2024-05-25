package org.poc.flowchart;

import com.google.common.collect.ImmutableList;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;
import org.flowchart.ChartNodeVisitor;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

import static guru.nidi.graphviz.model.Factory.*;

public class ChartNodeVisitorImpl implements ChartNodeVisitor {
    private MutableGraph g;

    public ChartNodeVisitorImpl(MutableGraph g) {
        this.g = g;
    }

    public void visit(ChartNode node, List<ChartNode> outgoingNodes, ChartNodeService nodeService) {
        System.out.println("Visiting : " + node);
        outgoingNodes.forEach(o -> {
            System.out.println("Linking " + node + " to " + o);
            ImmutableList<Color> colorScheme = overallColor(node);
            Color fontColor = colorScheme.get(0);
            Color backgroundColor = colorScheme.get(1);
            g.add(mutNode(node.toString()).add("style", "filled").add("fontcolor", fontColor.value).add("fillcolor", backgroundColor.value).addLink(mutNode(o.toString())));
        });
    }

    private ImmutableList<Color> overallColor(ChartNode node) {
        ImmutableList<Color> childColorScheme = childColor(node);
        ImmutableList<Color> selfColorScheme = selfColor(node);
        if (selfColorScheme.get(1) == Color.WHITE) return childColorScheme;
        return selfColorScheme;
    }

    private ImmutableList<Color> childColor(ChartNode node) {
        if (node.getExecutionContext().getClass() == CobolParser.StatementContext.class) {
            ParseTree typedStatement = node.getExecutionContext().getChild(0);
            System.out.println(typedStatement.getClass());
            if (typedStatement.getClass() == CobolParser.IfStatementContext.class) return ImmutableList.of(Color.WHITE, Color.PURPLE);
            else if (typedStatement.getClass() == CobolParser.GoToStatementContext.class) return ImmutableList.of(Color.WHITE, Color.DARKORANGE2);
        }
        return ImmutableList.of(Color.BLACK, Color.WHITE);
    }

    private ImmutableList<Color> selfColor(ChartNode node) {
        if (node.getExecutionContext().getClass() == CobolParser.ProcedureSectionContext.class) return ImmutableList.of(Color.WHITE, Color.RED);
        else if (node.getExecutionContext().getClass() == CobolParser.ParagraphContext.class) return ImmutableList.of(Color.BLACK, Color.GREEN);
        return ImmutableList.of(Color.BLACK, Color.WHITE);
    }

    public void visitCluster(ChartNode compositeNode, ChartNodeService nodeService) {
    }

    @Override
    public void visitParentChildLink(ChartNode parent, ChartNode internalTreeRoot, ChartNodeService nodeService) {
        MutableNode o = mutNode(parent.toString());
        MutableNode child = mutNode(internalTreeRoot.toString());
        g.add(o.add(Color.RED).addLink(o.linkTo(child).with("style", "dashed")));
    }

    @Override
    public void visitControlTransfer(ChartNode from, ChartNode to) {
        MutableNode origin = mutNode(from.toString()).add(Color.RED);
        MutableNode destination = mutNode(to.toString());
        g.add(origin.addLink(origin.linkTo(destination).with("style", "bold").with("color", "blueviolet")));
    }
}
