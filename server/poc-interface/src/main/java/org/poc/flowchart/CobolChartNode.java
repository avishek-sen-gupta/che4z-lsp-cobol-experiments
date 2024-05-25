package org.poc.flowchart;

import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.eclipse.lsp.cobol.dialects.idms.IdmsParser;
import org.flowchart.ChartNode;
import org.flowchart.ChartNodeService;
import org.flowchart.ChartNodeVisitor;
import org.eclipse.lsp.cobol.cli.IdmsContainerNode;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.*;

public class CobolChartNode implements ChartNode {
    static int counter = 0;
    private String uuid;
    protected boolean composite = false;
    @Getter protected List<ChartNode> outgoingNodes;
    protected List<ChartNode> incomingNodes;
    private ChartNode internalTreeRoot;
    @Getter protected final ParseTree executionContext;
    protected ChartNodeService nodeService;

    public CobolChartNode(ParseTree executionContext, ChartNodeService nodeService) {
        this.uuid = String.valueOf(counter);
        counter ++;
        this.nodeService = nodeService;
        composite = isCompositeNode(executionContext);
        this.executionContext = executionContext;
        outgoingNodes = new ArrayList<>();
        incomingNodes = new ArrayList<>();
//        this.nodeService.register(this);
    }

    protected boolean isCompositeNode(ParseTree executionContext) {
        return executionContext.getClass() == CobolParser.ProcedureSectionContext.class ||
                executionContext.getClass() == CobolParser.ParagraphsContext.class ||
                executionContext.getClass() == CobolParser.ParagraphContext.class ||
                executionContext.getClass() == CobolParser.SentenceContext.class ||
                executionContext.getClass() == CobolParser.ProcedureDivisionBodyContext.class ||
                executionContext.getClass() == CobolParser.IfThenContext.class ||
                executionContext.getClass() == CobolParser.IfElseContext.class ||
                executionContext.getClass() == CobolParser.ConditionalStatementCallContext.class ||
                executionContext.getClass() == CobolParser.DialectStatementContext.class ||
                executionContext.getClass() == CobolParser.DialectSectionContext.class ||
                executionContext.getClass() == IdmsParser.IdmsStatementsContext.class
                ;
    }

    @Override
    public void buildFlow() {
        System.out.println("Building flow for " + executionContextName());
        buildInternalFlow();
        buildOutgoingFlow();
//        outgoingNodes.forEach(ChartNode::buildFlow);
    }

    @Override
    public void buildOutgoingFlow() {
        for (int i = 0; i < outgoingNodes.size(); i++) {
            outgoingNodes.get(i).buildFlow();
        }
    }

    @Override
    public void buildInternalFlow() {
        System.out.println("Building internal flow for " + executionContextName());
        if (composite) {
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
        if (executionContext instanceof ParserRuleContext) {
            return executionContextName() + "/" + ((ParserRuleContext) executionContext).getStart().getLine();
        }
        return executionContextName() + "." + uuid;
//        return getClass().getSimpleName() + "{" +
//                "executionContext=" + executionContext.getClass().getSimpleName() + "} = " + executionContextName();
    }

    public String executionContextName() {
        if (executionContext.getClass() == CobolParser.ProcedureSectionContext.class)
            return ((CobolParser.ProcedureSectionContext) executionContext).procedureSectionHeader().sectionName().getText();
        if (executionContext.getClass() == CobolParser.ParagraphContext.class)
            return ((CobolParser.ParagraphContext) executionContext).paragraphDefinitionName().getText();
        if (executionContext.getClass() == CobolParser.StatementContext.class)
            return "Stmt:" + truncated(executionContext, 15);
        if (executionContext.getClass() == CobolParser.SentenceContext.class)
            return "Sentence: " + truncated(executionContext, 10);
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
        return executionContext.getClass().getSimpleName() + "/" + uuid;
    }

    private String truncated(ParseTree e, int truncationLimit) {
        return e.getText().length() > truncationLimit ? e.getText().substring(0, truncationLimit) : e.getText();
    }

    public void accept(ChartNodeVisitor visitor, int level) {
        accept(visitor, level, -1);
    }
    public void accept(ChartNodeVisitor visitor, int level, int maxLevel) {
        System.out.println("Current level: " + level);
        if (composite) {
            visitor.visit(this, nodeService);
            this.outgoingNodes.forEach(c -> c.accept(visitor, level, maxLevel));
            if (maxLevel != -1 && level > maxLevel) return;
            if (internalTreeRoot == null) return;

            // Make an explicit connection between higher organisational unit and root of internal tree
            visitor.visitParentChildLink(this, internalTreeRoot, nodeService);
            ChartNode current = internalTreeRoot;
            current.accept(visitor, level + 1, maxLevel);
        } else {
            visitor.visit(this, nodeService);
            outgoingNodes.forEach(c -> c.accept(visitor, level, maxLevel));
        }
    }

    @Override
    public void addIncomingNode(ChartNode chartNode) {
        incomingNodes.add(chartNode);
    }

}
