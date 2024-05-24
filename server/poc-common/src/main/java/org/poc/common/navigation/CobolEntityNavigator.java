package org.poc.common.navigation;

import org.antlr.v4.runtime.tree.ParseTree;

public interface CobolEntityNavigator {
    ParseTree findTarget(String procedureName);
}
