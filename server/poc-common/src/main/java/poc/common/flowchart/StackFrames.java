package poc.common.flowchart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ChartNode getLast() {
        return frames.getLast();
    }

    public Optional<ChartNode> find(ChartNodeCondition c) {
        return frames.stream().filter(c::apply).findFirst();
    }

    public String stackTrace() {
        StringBuilder builder = new StringBuilder();
        frames.forEach(f -> builder.append(f.getClass().getSimpleName()).append("/").append(f).append("\n"));
        return builder.toString();
    }
}
