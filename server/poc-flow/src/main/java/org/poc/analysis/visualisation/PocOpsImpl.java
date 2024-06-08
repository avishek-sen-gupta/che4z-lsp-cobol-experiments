package org.poc.analysis.visualisation;

import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.CobolTreeVisualiser;
import poc.common.flowchart.FlowchartBuilderFactoryMethod;
import poc.common.flowchart.IDataStructureBuilder;
import poc.common.flowchart.PocOps;
import org.poc.vm.CobolEntityNavigatorBuilderImpl;
import org.poc.common.navigation.EntityNavigatorBuilder;
import org.poc.vm.DataStructureBuilder;

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

    @Override
    public IDataStructureBuilder getDataStructureBuilder(CobolEntityNavigator navigator) {
        return new DataStructureBuilder(navigator);
    }
}
