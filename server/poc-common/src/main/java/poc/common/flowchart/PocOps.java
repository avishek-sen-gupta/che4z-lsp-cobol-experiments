package poc.common.flowchart;

import org.poc.common.navigation.CobolEntityNavigator;
import org.poc.common.navigation.EntityNavigatorBuilder;

public interface PocOps {
    CobolTreeVisualiser getVisualiser();

    FlowchartBuilderFactoryMethod getFlowchartBuilderFactory();

    EntityNavigatorBuilder getCobolEntityNavigatorBuilder();

    IDataStructureBuilder getDataStructureBuilder(CobolEntityNavigator navigator);
}
