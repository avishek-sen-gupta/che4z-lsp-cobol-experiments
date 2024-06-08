package poc.common.flowchart;

import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

public interface CobolInterpreter {
    CobolInterpreter scope(ChartNode scope);

    CobolVmSignal execute(ChartNode node);

    void enter(ChartNode node);

    void exit(ChartNode node);

    CobolVmSignal executeIf(ChartNode node, ChartNodeService nodeService);

    CobolVmSignal executePerformProcedure(List<ChartNode> procedures, ChartNodeService nodeService, ChartNode node);

    CobolVmSignal executeGoto(List<ChartNode> destinationNodes, ChartNodeService nodeService, ChartNode node);

    CobolVmSignal executeExit(ChartNodeService nodeService, ChartNode node);

    CobolVmSignal executeNextSentence(ChartNodeService nodeService, ChartNode node);

    CobolVmSignal executeDisplay(List<CobolParser.DisplayOperandContext> messages, ChartNodeService nodeService, ChartNode node);

    CobolVmSignal executeMove(ChartNode moveChartNode, ChartNodeService nodeService);

    CobolVmSignal executeAdd(ChartNode addChartNode, ChartNodeService nodeService);
}
