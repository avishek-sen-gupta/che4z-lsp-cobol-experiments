package poc.common.flowchart;

import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;
import java.util.Scanner;

public interface ConditionResolver {
    ConditionResolver ALWAYS_TRUE = new ConditionResolver() {
        @Override
        public boolean resolve(ChartNode node) {
            return true;
        }
    };
    boolean resolve(ChartNode node);
}
