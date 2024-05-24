package org.flowchart;

import org.poc.common.navigation.NavigatorFactoryMethod;
import org.poc.common.navigation.ProcedureDivisionBodyFinder;

public interface PocOps {
    CobolTreeVisualiser getVisualiser();

    NavigatorFactoryMethod getNavigatorFactory();

    ProcedureDivisionBodyFinder getFinder();

    FlowchartBuilderFactoryMethod getFlowchartBuilderFactory();
}
