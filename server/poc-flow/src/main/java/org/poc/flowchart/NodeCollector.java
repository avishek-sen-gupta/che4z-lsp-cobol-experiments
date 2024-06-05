package org.poc.flowchart;

import lombok.Getter;
import org.antlr.v4.runtime.tree.*;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;

import java.util.ArrayList;
import java.util.List;

public class NodeCollector implements ParseTreeVisitor<List<ChartNode>> {
    private final ChartNodeService nodeService;
    @Getter private final List<ChartNode> collectedNodes = new ArrayList<>();

    public NodeCollector(ChartNodeService nodeService) {
        this.nodeService = nodeService;
    }

    @Override
    public List<ChartNode> visit(ParseTree parseTree) {
        if (parseTree.getClass() == CobolParser.EndClauseContext.class || parseTree.getClass() == IdmsParser.EndClauseContext.class)
            return collectedNodes;
        ChartNode node = nodeService.existingNode(parseTree);
        if (node == null) return collectedNodes;
        collectedNodes.add(node);
        return collectedNodes;
    }

    @Override
    public List<ChartNode> visitChildren(RuleNode ruleNode) {
        if (ruleNode.getClass() == CobolParser.EndClauseContext.class || ruleNode.getClass() == IdmsParser.EndClauseContext.class)
            return collectedNodes;
        ChartNode node = nodeService.existingNode(ruleNode);
        if (node != null) collectedNodes.add(node);
        for (int i = 0; i < ruleNode.getChildCount(); i++) {
            ruleNode.getChild(i).accept(this);
        }
        return collectedNodes;
    }

    @Override
    public List<ChartNode> visitTerminal(TerminalNode terminalNode) {
        return collectedNodes;
    }

    @Override
    public List<ChartNode> visitErrorNode(ErrorNode errorNode) {
        return collectedNodes;
    }
}
