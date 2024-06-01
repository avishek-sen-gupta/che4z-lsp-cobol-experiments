package poc.common.flowchart;

public interface ChartNodeRecursionStrategy {
    public static ChartNodeRecursionStrategy ALWAYS_SHOW = new ChartNodeRecursionStrategy() {
        @Override
        public ChartNode apply(ChartNode node) {
            return null;
        }
    };

    ChartNode apply(ChartNode node);
}
