package org.poc.vm;

import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ConditionResolver;
import poc.common.flowchart.ConsoleColors;

import java.util.Scanner;

import static poc.common.flowchart.ConsoleColors.coloured;
import static poc.common.flowchart.ConsoleColors.green;

public class ConsoleInputResolver implements ConditionResolver {

    @Override
    public boolean resolve(ChartNode node) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(coloured("Waiting for condition " + node.label().trim() + ": ", 208));
        String trueOrFalse = scanner.nextLine().trim().toUpperCase();
        return "Y".equals(trueOrFalse);
    }
}
