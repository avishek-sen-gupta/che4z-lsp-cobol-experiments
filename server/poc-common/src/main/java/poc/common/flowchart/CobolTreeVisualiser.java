package poc.common.flowchart;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.poc.common.navigation.CobolEntityNavigator;

public interface CobolTreeVisualiser {
    void writeCobolAST(ParserRuleContext tree, String cobolParseTreeOutputPath, CobolEntityNavigator navigator);
    void writeCobolAST(ParseTree tree, String cobolParseTreeOutputPath, boolean outputTree, CobolEntityNavigator navigator);
}
