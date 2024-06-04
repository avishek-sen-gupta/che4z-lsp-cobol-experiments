package poc.common.flowchart;

import java.util.function.Function;

public interface FlowControl {
    public static CobolVmSignal STOP(Function<Void, CobolVmSignal> action, CobolVmSignal defaultSignal) {
        return defaultSignal;
    }

    public static CobolVmSignal CONTINUE(Function<Void, CobolVmSignal> action, CobolVmSignal defaultSignal) {
        return action.apply(null);
    }

    public CobolVmSignal apply(Function<Void, CobolVmSignal> action, CobolVmSignal defaultSignal);
}
