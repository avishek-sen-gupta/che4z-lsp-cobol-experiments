package poc.common.flowchart;

import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

public interface CobolInterpreter {
    CobolInterpreter scope(ChartNode scope);

    CobolVmSignal execute(ChartNode node);

    void enter(ChartNode node);

    void exit(ChartNode node);

    CobolVmSignal executeIf(ChartNode node, ChartNodeService nodeService);

    CobolVmSignal executePerformProcedure(List<ChartNode> procedures, ChartNodeService nodeService);

    CobolVmSignal executeGoto(List<ChartNode> destinationNodes, ChartNodeService nodeService);

    CobolVmSignal executeExit(ChartNodeService nodeService);

    CobolVmSignal executeNextSentence(ChartNodeService nodeService);

    CobolVmSignal executeDisplay(List<CobolParser.DisplayOperandContext> messages, ChartNodeService nodeService);
}
