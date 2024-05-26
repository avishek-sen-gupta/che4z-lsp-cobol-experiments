package org.poc.ai;

import org.antlr.v4.runtime.tree.ParseTree;
import org.poc.common.navigation.CobolEntityNavigator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SymbolReferences {
    private final CobolEntityNavigator navigator;
    private Map<String, ParseTree> symbolReferences = new HashMap();

    public SymbolReferences(CobolEntityNavigator navigator) {
        this.navigator = navigator;
    }

    public void add(String bracketedSymbol) {
        String symbol = bracketedSymbol.replace("[", "").replace("]", "");
        ParseTree target = navigator.target(symbol);
        if (target == null) return;
        symbolReferences.put(symbol, target);
    }

    public ParseTree get(String symbol) {
        return symbolReferences.get(symbol);
    }

    public Collection<ParseTree> getSymbols() {
        return symbolReferences.values();
    }
}
