package poc.common.flowchart;

import java.util.ArrayList;
import java.util.List;

public class StackFrames {
    List<ChartNode> frames = new ArrayList<>();

    public StackFrames() {
        this(new ArrayList<>());
    }

    public StackFrames(List<ChartNode> frames) {
        this.frames = frames;
    }

    public StackFrames add(ChartNode frame) {
        List<ChartNode> shallowCopy = new ArrayList<>(frames);
        shallowCopy.add(frame);
        return new StackFrames(shallowCopy);
    }
}
