package org.eclipse.lsp.cobol;

import org.flowchart.CobolTreeVisualiser;
import org.flowchart.FlowchartBuilderFactoryMethod;
import org.flowchart.PocOps;
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
}
