package org.eclipse.lsp.cobol;

import org.flowchart.CobolTreeVisualiser;
import org.flowchart.FlowchartBuilderFactoryMethod;
import org.flowchart.PocOps;
import org.poc.common.navigation.NavigatorFactoryMethod;
import org.poc.common.navigation.ProcedureDivisionBodyFinder;

public class DummyPoCOps implements PocOps {
    @Override
    public CobolTreeVisualiser getVisualiser() {
        return null;
    }

    @Override
    public NavigatorFactoryMethod getNavigatorFactory() {
        return null;
    }

    @Override
    public ProcedureDivisionBodyFinder getFinder() {
        return null;
    }

    @Override
    public FlowchartBuilderFactoryMethod getFlowchartBuilderFactory() {
        return null;
    }
}
