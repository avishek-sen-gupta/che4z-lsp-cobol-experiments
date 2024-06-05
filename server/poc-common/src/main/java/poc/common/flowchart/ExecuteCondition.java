package poc.common.flowchart;

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
    };

    boolean evaluate(ChartNode chartNode);

    boolean shouldExecute();
}
