package poc.common.flowchart;

import org.poc.common.navigation.CobolEntityNavigator;

public interface FlowchartBuilderFactoryMethod {
    FlowchartBuilder apply(CobolEntityNavigator navigator);
}
