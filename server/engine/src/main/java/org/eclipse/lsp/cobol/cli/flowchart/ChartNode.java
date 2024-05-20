package org.eclipse.lsp.cobol.cli.flowchart;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.*;

public class ChartNode {
    static int counter = 0;
    private String uuid = "";
    private final Random random;
    @Expose
    private boolean composite = false;
    @Getter
    @Expose
    private List<ChartNode> outgoingNodes;
    private List<ChartNode> incomingNodes;
    @Expose
    private ChartNode internalTreeRoot;
    private ParseTree executionContext;
    private ChartNodeService nodeService;

    public ChartNode(ParseTree executionContext, ChartNodeService nodeService) {
        random = new Random();
        if (executionContext.getClass() == TerminalNodeImpl.class || executionContext.getClass() == CobolParser.ParagraphsContext.class) {
            this.uuid = counter + "";
            counter ++;
        }
//        this.uuid = random.nextInt(100) + "";
        this.nodeService = nodeService;
        if (executionContext.getClass() == CobolParser.ProcedureSectionContext.class ||
                executionContext.getClass() == CobolParser.ParagraphsContext.class ||
                executionContext.getClass() == CobolParser.ParagraphContext.class ||
                executionContext.getClass() == CobolParser.SentenceContext.class ||
                executionContext.getClass() == CobolParser.ProcedureDivisionBodyContext.class ||
                executionContext.getClass() == CobolParser.IfThenContext.class ||
                executionContext.getClass() == CobolParser.IfElseContext.class) {
            composite = true;
        }
        this.executionContext = executionContext;
        outgoingNodes = new ArrayList<>();
        incomingNodes = new ArrayList<>();
        this.nodeService.register(this);
    }

    public void buildFlow() {
        buildInternalFlow();
        outgoingNodes.forEach(n -> n.buildFlow());
    }

    private void buildInternalFlow() {
        if (composite) {
            List<ParseTree> children = ((ParserRuleContext) executionContext).children;
            if (children == null) return;
            internalTreeRoot = nodeService.node(children.get(0));
            ChartNode current = internalTreeRoot;
            for (int i = 0; i <= children.size() - 2; i++) {
                ChartNode successor = nodeService.node(children.get(i + 1));
                current.goesTo(successor);
                current = successor;
            }
            internalTreeRoot.buildFlow();
        } else {
            if (executionContext.getClass() == CobolParser.StatementContext.class) {
                ParseTree typedStatement = executionContext.getChild(0);
                if (typedStatement.getClass() == CobolParser.IfStatementContext.class) {
                    CobolParser.IfStatementContext ifStateement = (CobolParser.IfStatementContext) typedStatement;
                    ChartNode ifThenBlock = nodeService.node(ifStateement.ifThen());
                    this.goesTo(ifThenBlock);
                    ifThenBlock.buildFlow();
                    CobolParser.IfElseContext ifElseCtx = ifStateement.ifElse();
                    if (ifElseCtx != null) {
                        ChartNode ifElseBlock = nodeService.node(ifElseCtx);
                        this.goesTo(ifElseBlock);
                        ifElseBlock.buildFlow();
                    }
                }
            }
            // Nothing for now
        }
    }

    private void goesTo(ChartNode successor) {
        outgoingNodes.add(successor);
        successor.incomingNodes.add(this);
    }

    public void smushNonEssentialNodes() {
        // Get rid of terminal symbols, etc.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChartNode chartNode = (ChartNode) o;
        return executionContext == chartNode.executionContext;
    }

    @Override
    public int hashCode() {
        return Objects.hash(executionContext);
    }

    @Override
    public String toString() {
        return executionContextName();
//        return getClass().getSimpleName() + "{" +
//                "executionContext=" + executionContext.getClass().getSimpleName() + "} = " + executionContextName();
    }

    public String executionContextName() {
        if (executionContext.getClass() == CobolParser.ProcedureSectionContext.class)
            return ((CobolParser.ProcedureSectionContext) executionContext).procedureSectionHeader().sectionName().getText();
        if (executionContext.getClass() == CobolParser.ParagraphContext.class)
            return ((CobolParser.ParagraphContext) executionContext).paragraphDefinitionName().getText();
        if (executionContext.getClass() == CobolParser.StatementContext.class)
            return ((CobolParser.StatementContext) executionContext).getText();
        if (executionContext.getClass() == CobolParser.SentenceContext.class)
            return "" + ((CobolParser.SentenceContext) executionContext).start.getLine();
        if (executionContext.getClass() == TerminalNodeImpl.class)
            return executionContext.getText() + uuid;
        if (executionContext.getClass() == CobolParser.ParagraphDefinitionNameContext.class)
            return "NAME: " + executionContext.getText();
        if (executionContext.getClass() == CobolParser.ProcedureSectionHeaderContext.class)
            return executionContext.getText();
        if (executionContext.getClass() == CobolParser.ParagraphsContext.class)
            return "para-group:" + uuid;
        return executionContext.getClass().getSimpleName() + "/" + uuid;
    }

    public void accept(ChartNodeVisitor visitor, int level) {
        if (composite) {
            visitor.visit(this);
            this.outgoingNodes.forEach(c -> c.accept(visitor, level));
            if (level > 4) return;
            if (internalTreeRoot == null) return;
            visitor.visitSpecific(this, internalTreeRoot);
            ChartNode current = internalTreeRoot;
            current.accept(visitor, level + 1);
        } else {
            visitor.visit(this);
            outgoingNodes.forEach(c -> c.accept(visitor, level));
        }
//        visitor.visit(this);
//        outgoingNodes.forEach(c -> c.accept(visitor, level + 1));
    }
}
