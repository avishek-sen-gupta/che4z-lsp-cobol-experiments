package poc.common.flowchart;

import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;
import java.util.Scanner;

public interface ConditionResolver {
    ConditionResolver ALWAYS_TRUE = node -> {
        System.out.println(ConsoleColors.green("Resolved " + node.label().trim() + " condition to TRUE automatically..."));
        return true;
    };
    boolean resolve(ChartNode node);
}
