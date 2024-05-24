package org.flowchart;

import org.poc.common.navigation.EntityNavigatorBuilder;

public interface PocOps {
    CobolTreeVisualiser getVisualiser();

    FlowchartBuilderFactoryMethod getFlowchartBuilderFactory();

    EntityNavigatorBuilder getCobolEntityNavigatorBuilder();
}
