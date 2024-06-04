package org.poc.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import poc.common.flowchart.*;

import java.util.Optional;

public class ParagraphChartNode extends CompositeCobolNode {
    @Override
    public ChartNodeType type() {
        return ChartNodeType.PARAGRAPH;
    }

    public ParagraphChartNode(ParseTree parseTree, ChartNode scope, ChartNodeService nodeService, StackFrames stackFrames) {
        super(parseTree, scope, nodeService, stackFrames);
    }

    public ChartNode parentOrSelf() {
        Optional<ChartNode> parent = staticFrameContext.find(f -> f.getClass() == SectionChartNode.class);
        return parent.orElse(this);
    }
}
