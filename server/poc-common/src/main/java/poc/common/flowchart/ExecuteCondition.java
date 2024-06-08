package poc.common.flowchart;

import java.util.function.Function;
import java.util.function.Supplier;

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
        public CobolVmSignal run(Supplier<CobolVmSignal> callback) {
            return callback.get();
        }

    };

    boolean evaluate(ChartNode chartNode);

    boolean shouldExecute();

    CobolVmSignal run(Supplier<CobolVmSignal> callback);
}
