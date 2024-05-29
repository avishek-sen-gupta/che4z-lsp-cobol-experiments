package poc.common.flowchart;

import com.google.common.hash.HashingOutputStream;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;

public class ChartNodeTransformRules {
    List<NodeTransformRule> rules = new ArrayList<>();

    public ChartNodeTransformRules() {
        rules.add(ChartNodeTransformRules::hideNode);
    }

    public static void hideNode(ChartNode node) {
        if (StatementIdentity.isClutter(node.getExecutionContext())) {
            System.out.println("Removing node " + node);
            node.remove();
        }
//        if (node.getExecutionContext().getClass() == CobolParser.SentenceContext.class) {
//            if ()
//        }
//        if (node.getExecutionContext().getClass() == CobolParser.SentenceContext.class) {
//            System.out.println("Removing node " + node);
//            node.remove();
//        }
    }

    public void apply(ChartNode node) {
        System.out.println("Applying rules to node " + node);
        rules.forEach(r -> r.apply(node));
    }
}
