package vm;

import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ConditionResolver;

import java.util.Scanner;

public class ConsoleInputResolver implements ConditionResolver {

    @Override
    public boolean resolve(ChartNode node) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Waiting for condition " + node.label().trim() + ": ");
        String trueOrFalse = scanner.nextLine().trim().toUpperCase();
        return "Y".equals(trueOrFalse);
    }
}
