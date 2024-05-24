package org.flowchart;

import org.antlr.v4.runtime.tree.ParseTree;
import org.poc.common.navigation.CobolEntityNavigator;

public interface FlowchartBuilderFactoryMethod {
    FlowchartBuilder apply(ParseTree root, CobolEntityNavigator navigator);
}
