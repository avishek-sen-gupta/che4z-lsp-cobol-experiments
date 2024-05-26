package poc.common.flowchart;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public interface CobolTreeVisualiser {
    void visualiseCobolAST(ParserRuleContext tree, String cobolParseTreeOutputPath);
    void visualiseCobolAST(ParseTree tree, String cobolParseTreeOutputPath, boolean outputTree);
}
