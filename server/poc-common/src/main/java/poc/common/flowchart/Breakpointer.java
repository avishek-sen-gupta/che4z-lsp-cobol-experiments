package poc.common.flowchart;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Breakpointer {
    void addBreakpoint(ChartNodeCondition breakpoint);
    CobolVmSignal run(ChartNode node, Supplier<CobolVmSignal> execution);
}
