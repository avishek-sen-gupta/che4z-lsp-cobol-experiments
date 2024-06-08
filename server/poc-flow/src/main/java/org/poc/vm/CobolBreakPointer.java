package org.poc.vm;

import poc.common.flowchart.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

import static poc.common.flowchart.ConsoleColors.coloured;

public class CobolBreakPointer implements Breakpointer {
    private List<ChartNodeCondition> breakpoints = new ArrayList<>();

    @Override
    public void addBreakpoint(ChartNodeCondition breakpoint) {
        breakpoints.add(breakpoint);
    }

    @Override
    public CobolVmSignal run(ChartNode node, Supplier<CobolVmSignal> execution) {
        boolean shouldBreak = breakpoints.stream().anyMatch(bp -> bp.apply(node));
        if (!shouldBreak) return execution.get();
        Scanner scanner = new Scanner(System.in);
        System.out.println(coloured(String.format("Breakpoint hit at %s. Press ENTER to resume.", node.originalText().trim()), 202));
        scanner.nextLine();
        return execution.get();
    }
}
