package org.eclipse.lsp.cobol.cli;

import java.util.UUID;

public class DeadEndFlowNode extends FlowNode {
    public DeadEndFlowNode() {
        super(UUID.randomUUID().toString());
    }
}
