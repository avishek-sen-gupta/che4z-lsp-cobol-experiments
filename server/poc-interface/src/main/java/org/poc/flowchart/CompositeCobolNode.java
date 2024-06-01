package org.poc.flowchart;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.common.navigation.CobolEntityNavigator;
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
        List<? extends ParseTree> children = getChildren();
        if (children == null) return;
        internalTreeRoot = nodeService.node(children.getFirst(), this);
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
    public List<? extends ParseTree> getChildren() {
        return ((ParserRuleContext) executionContext).children;
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
    public ChartNode next(ChartNodeCondition nodeCondition, ChartNode startingNode, boolean isComplete) {
        System.out.println("Moved up to " + executionContext.getClass() + executionContext.getText());
        CobolEntityNavigator navigator = nodeService.getNavigator();
//        boolean shouldSearch = navigator.findByCondition(executionContext, n -> n == startingNode.getExecutionContext()) == null;
        if (!isComplete) {
            System.out.println("ITR is " + internalTreeRoot.getClass() + " " + internalTreeRoot);
            ChartNode searchResult = internalTreeRoot.next(nodeCondition, startingNode, false);
            if (searchResult != null) return searchResult;
        }

        ChartNode chainSearch = new ChartNodes(outgoingNodes, nodeService).first().next(nodeCondition, startingNode, false);
        if (chainSearch != null) return chainSearch;
        return scope != null ? scope.next(nodeCondition, startingNode, true) : new DummyChartNode(nodeService);
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
