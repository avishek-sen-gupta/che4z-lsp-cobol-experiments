package poc.common.flowchart;

public interface ChartNodeCondition {
    public static ChartNodeCondition ALWAYS_SHOW = new ChartNodeCondition() {
        @Override
        public boolean apply(ChartNode node) {
            return false;
        }
    };

    boolean apply(ChartNode node);
}
