package org.poc.flowchart;

import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeCondition;
import poc.common.flowchart.CobolVmSignal;
import poc.common.flowchart.StackFrames;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CobolStackFrames implements StackFrames {
    List<ChartNode> frames = new ArrayList<>();

    public CobolStackFrames() {
        this(new ArrayList<>());
    }

    public CobolStackFrames(List<ChartNode> frames) {
        this.frames = frames;
    }

    @Override
    public StackFrames add(ChartNode frame) {
        List<ChartNode> shallowCopy = new ArrayList<>(frames);
        shallowCopy.add(frame);
        return new CobolStackFrames(shallowCopy);
    }

    @Override
    public ChartNode getLast() {
        return frames.getLast();
    }

    @Override
    public Optional<ChartNode> find(ChartNodeCondition c) {
        return frames.stream().filter(c::apply).findFirst();
    }

    @Override
    public String stackTrace() {
        StringBuilder builder = new StringBuilder();
        frames.forEach(f -> builder.append(f.getClass().getSimpleName()).append("/").append(f).append("\n"));
        return builder.toString();
    }

    @Override
    public CobolVmSignal callSite() {
        int i = frames.size() - 1;
        while (i >= 0 && frames.get(i).getClass() != GoToChartNode.class) {
            if (frames.get(i).getClass() == PerformProcedureChartNode.class) return CobolVmSignal.EXIT_PERFORM;
            i --;
        }
        return CobolVmSignal.EXIT_SCOPE;
    }
}
