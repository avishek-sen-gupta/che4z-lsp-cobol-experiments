package org.eclipse.lsp.cobol.cli.flowchart;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

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
        return chartNode;
    }
}
