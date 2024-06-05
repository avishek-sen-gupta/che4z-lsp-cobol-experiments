package poc.common.flowchart;

import java.util.function.Function;

public class ExecuteAtTargetFlipCondition implements ExecuteCondition {
    private final ChartNode specificLocation;
    private boolean isInterpreting = false;

    public ExecuteAtTargetFlipCondition(ChartNode specificLocation) {
        this.specificLocation = specificLocation;
    }

    @Override
    public boolean evaluate(ChartNode chartNode) {
        isInterpreting = chartNode == specificLocation;
        return isInterpreting;
    }

    @Override
    public boolean shouldExecute() {
        return isInterpreting;
    }
}
