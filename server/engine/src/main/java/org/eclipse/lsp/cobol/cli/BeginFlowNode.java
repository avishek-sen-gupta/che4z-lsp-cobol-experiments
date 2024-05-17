package org.eclipse.lsp.cobol.cli;

import java.util.ArrayList;

public class BeginFlowNode extends FlowNode {
    public BeginFlowNode() {
        super("BEGIN", new ArrayList<>(), new DeadEndFlowNode());
    }
}
