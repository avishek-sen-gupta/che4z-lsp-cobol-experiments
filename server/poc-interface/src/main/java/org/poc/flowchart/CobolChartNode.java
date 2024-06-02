package org.poc.flowchart;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import poc.common.flowchart.*;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.*;

public class CobolChartNode implements ChartNode {
    protected final String uuid;
    protected List<ChartNode> outgoingNodes = new ArrayList<>();
    protected List<ChartNode> incomingNodes = new ArrayList<>();
    @Getter
    protected final ParseTree executionContext;
    protected ChartNodeService nodeService;
    private DomainDocument document = new DomainDocument();
    protected boolean initialised = false;
    protected boolean visited = false;
    private boolean databaseAccess;
    protected ChartNode scope;

    public CobolChartNode(ParseTree executionContext, ChartNode scope, ChartNodeService nodeService) {
        this.uuid = String.valueOf(nodeService.counter());
        this.nodeService = nodeService;
        this.executionContext = executionContext;
        this.scope = scope;
    }

    @Override
    public void buildFlow() {
        if (initialised) return;
        initialised = true;
        System.out.println("Building flow for " + name());
        buildInternalFlow();
        buildOutgoingFlow();
    }

    @Override
    public void buildOutgoingFlow() {
        outgoingNodes.forEach(ChartNode::buildFlow);
    }

    @Override
    public void buildInternalFlow() {
    }

    @Override
    public void buildControlFlow() {
    }

    @Override
    public void goesTo(ChartNode successor) {
        outgoingNodes.add(successor);
        successor.addIncomingNode(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CobolChartNode chartNode = (CobolChartNode) o;
        return executionContext == chartNode.executionContext && scope == chartNode.scope;
    }

    @Override
    public int hashCode() {
        return Objects.hash(executionContext);
    }

    // Use only for debugging
    @Override
    public String toString() {
        if (executionContext instanceof ParserRuleContext) {
            return name() + "/" + ((ParserRuleContext) executionContext).getStart().getLine() + "/" + uuid;
        }
        return name() + "." + uuid;
    }

    @Override
    public String name() {
        if (executionContext.getClass() == CobolParser.ProcedureSectionContext.class)
            return ((CobolParser.ProcedureSectionContext) executionContext).procedureSectionHeader().sectionName().getText();
        if (executionContext.getClass() == CobolParser.ParagraphContext.class)
            return ((CobolParser.ParagraphContext) executionContext).paragraphDefinitionName().getText();
        if (executionContext.getClass() == CobolParser.StatementContext.class)
            return truncated(executionContext, 15);
        if (executionContext.getClass() == CobolParser.SentenceContext.class)
            return truncated(executionContext, 15);
//            return "SE:" + truncated(executionContext);
        if (executionContext.getClass() == TerminalNodeImpl.class)
            return executionContext.getText();
        if (executionContext.getClass() == CobolParser.ParagraphDefinitionNameContext.class)
            return "NAME: " + executionContext.getText();
        if (executionContext.getClass() == CobolParser.ProcedureSectionHeaderContext.class)
            return executionContext.getText();
        if (executionContext.getClass() == CobolParser.ParagraphsContext.class)
            return "para-group:";
        if (executionContext.getClass() == IdmsParser.IdmsStatementsContext.class)
            return truncated(executionContext, 15);
        return defaultName();
    }

    @Override
    public String originalText() {
        return NodeText.originalText(executionContext, NodeText::PASSTHROUGH);
    }

    @Override
    public ChartNodeType type() {
        return ChartNodeType.ATOMIC;
    }

    protected String defaultName() {
        return executionContext.getClass().getSimpleName() + "/" + uuid;
    }

    protected String truncated(ParseTree e, int truncationLimit) {
        return truncated(e.getText(), truncationLimit);
//        return e.getText().length() > truncationLimit ? e.getText().substring(0, truncationLimit) : e.getText();
    }

    protected String truncated(String s, int truncationLimit) {
        return s.length() > truncationLimit ? s.substring(0, truncationLimit) : s;
    }

    @Override
    public void accept(ChartNodeVisitor visitor, int level) {
        if (visited) return;
        visited = true;
        acceptUnvisited(visitor, level);
    }

    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {
//        System.out.println("Current level: " + level);
        visitor.visit(this, outgoingNodes, incomingNodes, new VisitContext(level), nodeService);
        outgoingNodes.forEach(c -> c.accept(visitor, level));
    }

    @Override
    public void addIncomingNode(ChartNode chartNode) {
        incomingNodes.add(chartNode);
    }

    @Override
    public DomainDocument getNotes() {
        return document;
    }

    @Override
    public void reset() {
        // If visited is already false, it means I must have already set it to false and now I am looping back on myself
        if (!visited) return;
        visited = false;
        outgoingNodes.forEach(ChartNode::reset);
    }

    @Override
    public boolean accessesDatabase() {
        return databaseAccess;
    }

    @Override
    public boolean isMergeable() {
        return false;
    }

    @Override
    public boolean contains(ChartNode node) {
        return false;
    }

    @Override
    public List<ChartNode> getOutgoingNodes() {
        return outgoingNodes;
    }

    @Override
    public String label() {
        return name();
    }

    @Override
    public ChartNode passthrough() {
        return this;
    }

    @Override
    public boolean isPassthrough() {
        return false;
    }

    @Override
    public String id() {
        return name() + "." + uuid;
    }

    @Override
    public ChartNode next(ChartNodeCondition nodeCondition, ChartNode startingNode, boolean isComplete) {
        if (this != startingNode && nodeCondition.apply(this)) return this;
        System.out.println("Num outgoing nodes: " + outgoingNodes.size());
        if (outgoingNodes.isEmpty()) return scope.next(nodeCondition, startingNode, true);
        for (ChartNode o : outgoingNodes) {
            ChartNode next = o.next(nodeCondition, startingNode, true);
            if (next != null) return next;
        }
        return null;
    }

    @Override
    public void linkParentToChild(ChartNodeVisitor visitor, int level) {
    }

    @Override
    public ChartNode find(ChartNodeCondition nodeCondition, ChartNode startingNode) {
        if (nodeCondition.apply(this)) return this;
        return scope.find(nodeCondition, startingNode);
    }

    @Override
    public List<? extends ParseTree> getChildren() {
        return ImmutableList.of();
    }
}
