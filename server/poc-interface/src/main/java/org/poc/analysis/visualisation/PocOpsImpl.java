package org.poc.analysis.visualisation;

import org.flowchart.CobolTreeVisualiser;
import org.flowchart.FlowchartBuilderFactoryMethod;
import org.flowchart.PocOps;
import vm.CobolEntityNavigatorBuilderImpl;
import org.poc.common.navigation.EntityNavigatorBuilder;

public class PocOpsImpl implements PocOps {
    private final CobolTreeVisualiser visualiser;
    private final FlowchartBuilderFactoryMethod flowchartBuilderFactory;
    private final CobolEntityNavigatorBuilderImpl cobolEntityNavigatorBuilder;

    @Override
    public CobolTreeVisualiser getVisualiser() {
        return visualiser;
    }

    @Override
    public FlowchartBuilderFactoryMethod getFlowchartBuilderFactory() {
        return flowchartBuilderFactory;
    }

    public PocOpsImpl(CobolTreeVisualiser visualiser, FlowchartBuilderFactoryMethod flowchartBuilderFactory, CobolEntityNavigatorBuilderImpl cobolEntityNavigatorBuilder) {
        this.visualiser = visualiser;
        this.flowchartBuilderFactory = flowchartBuilderFactory;
        this.cobolEntityNavigatorBuilder = cobolEntityNavigatorBuilder;
    }

    @Override
    public EntityNavigatorBuilder getCobolEntityNavigatorBuilder() {
        return cobolEntityNavigatorBuilder;
    }
}
