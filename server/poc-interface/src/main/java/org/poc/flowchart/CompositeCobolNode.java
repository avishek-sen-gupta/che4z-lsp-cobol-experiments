package org.poc.flowchart;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.List;

public class CompositeCobolNode extends CobolChartNode {
    public static ChartNodeCondition CHILD_IS_CONDITIONAL_STATEMENT = node -> StatementIdentity.isOfType(node.getExecutionContext(), CobolParser.ConditionalStatementCallContext.class);
    protected ChartNode internalTreeRoot;

    public CompositeCobolNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService) {
        super(parseTree, scope, nodeService);
    }

    @Override
    public void buildInternalFlow() {
        System.out.println("Building internal flow for " + name());
        List<ParseTree> children = ((ParserRuleContext) executionContext).children;
        if (children == null) return;
        internalTreeRoot = nodeService.node(children.get(0), this);
        ChartNode current = internalTreeRoot;
        for (int i = 0; i <= children.size() - 2; i++) {
            ChartNode nextNode = nodeService.node(children.get(i + 1), this);
            if (".".equals(nextNode.getExecutionContext().getText())) continue;
            ChartNode successor = nextNode;
            current.goesTo(successor);
            current = successor;
        }
        internalTreeRoot.buildFlow();
    }

    @Override
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
        if (internalTreeRoot != null && (maxLevel == -1 || (maxLevel != -1 && level <= maxLevel))) {
            linkParentToChild(visitor);
            ChartNode current = internalTreeRoot;
            current.accept(visitor.newScope(this), level + 1, maxLevel);
        }
        super.acceptUnvisited(visitor, level, maxLevel);
    }

    @Override
    public void linkParentToChild(ChartNodeVisitor visitor) {
        visitor.visitParentChildLink(this, internalTreeRoot, nodeService);
    }

    @Override
    public void reset() {
        if (internalTreeRoot != null) internalTreeRoot.reset();
        super.reset();
    }

    @Override
    public ChartNodeType type() {
        if (executionContext.getClass() == CobolParser.ProcedureSectionContext.class) return ChartNodeType.SECTION;
        if (executionContext.getClass() == CobolParser.ParagraphContext.class) return ChartNodeType.PARAGRAPH;
//        if (executionContext.getClass() == CobolParser.ConditionalStatementCallContext.class)
//            return ChartNodeType.CONDITION_CLAUSE;
        return ChartNodeType.COMPOSITE;
    }

    @Override
    public boolean contains(ChartNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
