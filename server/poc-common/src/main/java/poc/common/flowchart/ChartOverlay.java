package poc.common.flowchart;

import java.util.List;
import java.util.Optional;

public class ChartOverlay {
    private final List<GenericProcessingChartNode> groups;

    public ChartOverlay(List<GenericProcessingChartNode> groups) {
        this.groups = groups;
    }

    public ChartNode block(ChartNode node) {
//        return node;
        Optional<GenericProcessingChartNode> first = groups.stream().filter(g -> g.contains(node)).findFirst();
        if (first.isPresent()) return first.get();
        return node;
    }
}
