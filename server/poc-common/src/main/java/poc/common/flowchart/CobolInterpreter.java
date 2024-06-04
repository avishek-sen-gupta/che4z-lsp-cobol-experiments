package poc.common.flowchart;

public interface CobolInterpreter {
    CobolInterpreter scope(ChartNode scope);

    CobolVmSignal execute(ChartNode node);

    void enter(ChartNode node);

    void exit(ChartNode node);

    CobolVmSignal executeIf(ChartNode node, ChartNodeService nodeService);
}
