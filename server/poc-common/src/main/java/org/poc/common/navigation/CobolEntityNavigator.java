package org.poc.common.navigation;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public interface CobolEntityNavigator {
    public static String PASSTHROUGH(String text) {
        return text;
    }

    ParseTree target(String procedureName);

    ParseTree root();

    ParseTree findByCondition(ParseTree searchRoot, ParseTreeSearchCondition c, int maxLevel);

    ParseTree findByCondition(ParseTree searchRoot, ParseTreeSearchCondition c);

    List<ParseTree> statementsContaining(String symbol, ParseTree scope);

    List<ParseTree> findAllByCondition(ParseTreeSearchCondition c, ParseTree scope);

    List<ParseTree> findAllByCondition(ParseTreeSearchCondition c, ParseTree scope, int maxLevel);

    void buildDialectNodeRepository();

    String dialectText(String marker);
}
