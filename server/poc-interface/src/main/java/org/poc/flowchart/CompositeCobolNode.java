package org.poc.flowchart;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import poc.common.flowchart.ChartNodeType;
import poc.common.flowchart.ChartNodeVisitor;

import java.util.List;

public class CompositeCobolNode extends CobolChartNode {
    private ChartNode internalTreeRoot;

    public CompositeCobolNode(ParseTree parseTree, ChartNodeService nodeService) {
        super(parseTree, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        System.out.println("Building internal flow for " + name());
        List<ParseTree> children = ((ParserRuleContext) executionContext).children;
        if (children == null) return;
        internalTreeRoot = nodeService.node(children.get(0));
        ChartNode current = internalTreeRoot;
        for (int i = 0; i <= children.size() - 2; i++) {
            ChartNode nextNode = nodeService.node(children.get(i + 1));
            if (".".equals(nextNode.getExecutionContext().getText())) continue;
            ChartNode successor = nextNode;
            current.goesTo(successor);
            current = successor;
        }
        internalTreeRoot.buildFlow();
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        super.acceptUnvisited(visitor, level, maxLevel);
        if (maxLevel != -1 && level > maxLevel) return;
        if (internalTreeRoot == null) return;

        // Make an explicit connection between higher organisational unit and root of internal tree
        visitor.visitParentChildLink(this, internalTreeRoot, nodeService);
        ChartNode current = internalTreeRoot;
        current.accept(visitor, level + 1, maxLevel);
    }

    @Override
    public ChartNodeType type() {
        if (executionContext.getClass() == CobolParser.ProcedureSectionContext.class) return ChartNodeType.SECTION;
        if (executionContext.getClass() == CobolParser.ParagraphContext.class) return ChartNodeType.PARAGRAPH;
        return ChartNodeType.COMPOSITE;
    }
}
