package org.eclipse.lsp.cobol.cli;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class ConditionFlowNode extends FlowNode {
    public ConditionFlowNode() {
        super(new ArrayList<>(), new ArrayList<>());
    }

    public void truePathRoot(FlowNode trueOutgoingPathRoot) {
        super.outgoingNodes.add(trueOutgoingPathRoot);
    }

    public void falsePathRoot(FlowNode falseOutgoingPathRoot) {
        super.outgoingNodes.add(falseOutgoingPathRoot);
    }
}
