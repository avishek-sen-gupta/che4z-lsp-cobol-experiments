package org.eclipse.lsp.cobol.cli;

import java.util.UUID;

public class PlaceholderFlowNode extends FlowNode {
    public PlaceholderFlowNode() {
        super(UUID.randomUUID().toString());
    }
}
