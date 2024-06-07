package org.poc.flowchart;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.*;

import java.util.List;

public class CompositeCobolNode extends CobolChartNode {
    public static ChartNodeCondition CHILD_IS_CONDITIONAL_STATEMENT = node -> SyntaxIdentity.isOfType(node.getExecutionContext(), CobolParser.ConditionalStatementCallContext.class);
    protected ChartNode internalTreeRoot;

    public CompositeCobolNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    @Override
    public void buildInternalFlow() {
        System.out.println("Building internal flow for " + name());
        List<? extends ParseTree> children = getChildren();
        if (children == null) return;
        System.out.println("Looking at " + name());
        internalTreeRoot = nodeService.node(children.getFirst(), this, staticFrameContext.add(this));
        ChartNode current = internalTreeRoot;
        for (int i = 0; i <= children.size() - 2; i++) {
            ChartNode nextNode = nodeService.node(children.get(i + 1), this, staticFrameContext.add(this));
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
    public void acceptUnvisited(ChartNodeVisitor visitor, int level) {
        // super() call needs to happen first, otherwise duplicate overlays will be created, since children will get GenericProcessingNodes earlier than parents.
        if (internalTreeRoot != null) {
            linkParentToChild(visitor, level);
            ChartNode current = internalTreeRoot;
            current.accept(visitor.newScope(this), level + 1);
        }
        super.acceptUnvisited(visitor, level);
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
        return scope != null ? scope.next(nodeCondition, startingNode, true) : new DummyChartNode(nodeService, staticFrameContext);
    }

    @Override
    public void linkParentToChild(ChartNodeVisitor visitor, int level) {
        visitor.visitParentChildLink(this, internalTreeRoot, new VisitContext(level), nodeService);
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
    public CobolVmSignal acceptInterpreter(CobolInterpreter interpreter, ChartNodeService nodeService, FlowControl flowControl) {
        CobolVmSignal signal = executeInternalRoot(interpreter, nodeService);
        return flowControl.apply((Void) -> continueOrAbort(signal, interpreter, nodeService), signal);
    }

    protected CobolVmSignal executeInternalRoot(CobolInterpreter interpreter, ChartNodeService nodeService) {
        interpreter.enter(this);
        CobolVmSignal signal = internalTreeRoot != null ? internalTreeRoot.acceptInterpreter(interpreter.scope(this), nodeService, FlowControl::CONTINUE) : CobolVmSignal.CONTINUE;
        interpreter.exit(this);
        return signal;
    }

    @Override
    public boolean contains(ChartNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
