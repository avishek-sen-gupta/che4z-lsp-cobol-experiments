package poc.common.flowchart;

import java.util.function.Function;

public interface ExecuteCondition {
    public static ExecuteCondition ALWAYS_EXECUTE = new ExecuteCondition() {
        @Override
        public boolean evaluate(ChartNode chartNode) {
            return true;
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public CobolVmSignal run(Function<Void, CobolVmSignal> callback) {
            return callback.apply(null);
        }

    };

    boolean evaluate(ChartNode chartNode);

    boolean shouldExecute();

    CobolVmSignal run(Function<Void, CobolVmSignal> callback);
}
