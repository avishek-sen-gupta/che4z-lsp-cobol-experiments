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
        if (chartNode == specificLocation && !isInterpreting) {
            isInterpreting = true;
            System.out.println("FLIPPED TO TRUE");
        }
        return isInterpreting;
    }

    @Override
    public boolean shouldExecute() {
        return isInterpreting;
    }
}
