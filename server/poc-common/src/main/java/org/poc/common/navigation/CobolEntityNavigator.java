package org.poc.common.navigation;

import org.antlr.v4.runtime.tree.ParseTree;

public interface CobolEntityNavigator {
    ParseTree target(String procedureName);
}
