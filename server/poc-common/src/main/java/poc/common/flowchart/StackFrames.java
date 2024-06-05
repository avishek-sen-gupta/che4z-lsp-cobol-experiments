package poc.common.flowchart;

import java.util.Optional;

public interface StackFrames {
    StackFrames add(ChartNode frame);

    ChartNode getLast();

    Optional<ChartNode> find(ChartNodeCondition c);

    String stackTrace();

    CobolVmSignal callSite();
}
