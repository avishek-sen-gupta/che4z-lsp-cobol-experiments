package org.eclipse.lsp.cobol;

import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.CobolTreeVisualiser;
import poc.common.flowchart.FlowchartBuilderFactoryMethod;
import poc.common.flowchart.IDataStructureBuilder;
import poc.common.flowchart.PocOps;
import org.poc.common.navigation.EntityNavigatorBuilder;

public class DummyPoCOps implements PocOps {
    @Override
    public CobolTreeVisualiser getVisualiser() {
        return null;
    }

    @Override
    public FlowchartBuilderFactoryMethod getFlowchartBuilderFactory() {
        return null;
    }

    @Override
    public EntityNavigatorBuilder getCobolEntityNavigatorBuilder() {
        return null;
    }

    @Override
    public IDataStructureBuilder getDataStructureBuilder(CobolEntityNavigator navigator) {
        return null;
    }
}
