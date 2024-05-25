package org.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.poc.common.navigation.CobolEntityNavigator;

public interface ChartNodeService {
    ChartNode register(ChartNode chartNode);

    ChartNode node(ParseTree parseTree);

    ChartNode sectionOrParaWithName(String name);

    CobolEntityNavigator getNavigator();
}
