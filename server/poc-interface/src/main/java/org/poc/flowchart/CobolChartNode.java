package org.poc.flowchart;

import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import poc.common.flowchart.*;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.*;

public class CobolChartNode implements ChartNode {
    static int counter = 0;
    private final String uuid;
    protected List<ChartNode> outgoingNodes = new ArrayList<>();
    protected List<ChartNode> incomingNodes = new ArrayList<>();
    @Getter protected final ParseTree executionContext;
    protected ChartNodeService nodeService;
    private DomainDocument document = new DomainDocument();
    protected boolean initialised = false;
    protected boolean visited = false;
    private boolean databaseAccess;

    public CobolChartNode(ParseTree executionContext, ChartNodeService nodeService) {
        this.uuid = String.valueOf(counter);
        counter ++;
        this.nodeService = nodeService;
        this.executionContext = executionContext;
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
    public void goesTo(ChartNode successor) {
        outgoingNodes.add(successor);
        successor.addIncomingNode(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CobolChartNode chartNode = (CobolChartNode) o;
        return executionContext == chartNode.executionContext;
    }

    @Override
    public int hashCode() {
        return Objects.hash(executionContext);
    }

    @Override
    public String toString() {
//        if (!getNotes().getText().isEmpty()) return getNotes().getText();
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
            return "Stmt:" + truncated(executionContext, 15);
        if (executionContext.getClass() == CobolParser.SentenceContext.class)
            return "SE: " + truncated(executionContext, 15);
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
    public ChartNodeType type() {
        return ChartNodeType.ATOMIC;
    }

    protected String defaultName() {
        return executionContext.getClass().getSimpleName() + "/" + uuid;
    }

    protected String truncated(ParseTree e, int truncationLimit) {
        return e.getText().length() > truncationLimit ? e.getText().substring(0, truncationLimit) : e.getText();
    }

    public void accept(ChartNodeVisitor visitor, int level) {
        accept(visitor, level, -1);
    }

    @Override
    public void accept(ChartNodeVisitor visitor, int level, int maxLevel) {
        if (visited) return;
        visited = true;
        acceptUnvisited(visitor, level, maxLevel);
    }
    public void acceptUnvisited(ChartNodeVisitor visitor, int level, int maxLevel) {
//        System.out.println("Current level: " + level);
        visitor.visit(this, outgoingNodes, incomingNodes, nodeService);
        outgoingNodes.forEach(c -> c.accept(visitor, level, maxLevel));
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
        visited = false;
        outgoingNodes.forEach(ChartNode::reset);
    }

    @Override
    public void remove() {
        incomingNodes.forEach(n -> n.removeOutgoingNode(this));
        incomingNodes.clear();
        outgoingNodes.forEach(n -> n.removeIncomingNode(this));
        outgoingNodes.clear();
    }

    @Override
    public void removeOutgoingNode(ChartNode chartNode) {
        int before = outgoingNodes.size();
        outgoingNodes.remove(chartNode);
        int after = outgoingNodes.size();
        if (before != after) {
            System.out.println("Warning: NOTHING was removed");
        }
        else {
            System.out.println("Removed successfully");
        }
    }

    @Override
    public void removeIncomingNode(ChartNode chartNode) {
        incomingNodes.remove(chartNode);
    }

    @Override
    public boolean accessesDatabase() {
        return databaseAccess;
    }
}
