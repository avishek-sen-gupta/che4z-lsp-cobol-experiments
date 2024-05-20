package org.eclipse.lsp.cobol.cli.flowchart;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChartNode {
    private boolean composite = false;
    private List<ChartNode> outgoingNodes;
    private List<ChartNode> incomingNodes;
    private ChartNode internalTreeRoot;
    private ParseTree executionContent;
    private ChartNodeService nodeService;

    public ChartNode(ParseTree executionContent, ChartNodeService nodeService)
    {
        this.nodeService = nodeService;
        if (executionContent.getClass() == CobolParser.ProcedureSectionContext.class ||
            executionContent.getClass() == CobolParser.ParagraphsContext.class ||
            executionContent.getClass() == CobolParser.ParagraphContext.class ||
            executionContent.getClass() == CobolParser.SentenceContext.class ||
            executionContent.getClass() == CobolParser.ProcedureDivisionBodyContext.class ||
            executionContent.getClass() == CobolParser.IfThenContext.class ||
            executionContent.getClass() == CobolParser.IfElseContext.class) {
            composite = true;
        }
        this.executionContent = executionContent;
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
            List<ParseTree> children = ((ParserRuleContext) executionContent).children;
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
        return executionContent == chartNode.executionContent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(executionContent);
    }
}
