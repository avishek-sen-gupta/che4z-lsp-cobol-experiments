package org.eclipse.lsp.cobol.cli.flowchart;

public interface ChartNodeVisitor {
    void visit(ChartNode node);
    void visitSpecific(ChartNode parent, ChartNode internalTreeRoot);
}
