package org.eclipse.lsp.cobol.cli;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;

public class ScopeExecutionBuilder {
    private final FlowUnit unit;
    private final CobolFlowUnitNavigator navigator;
    private final CobolFrame frame;

    public ScopeExecutionBuilder(FlowUnit unit, CobolFlowUnitNavigator navigator, CobolFrame frame) {
        this.unit = unit;
        this.navigator = navigator;
        this.frame = frame;
    }

    public CobolVmInstruction run() {
        if (unit.scope() == ProgramScope.PERFORM) {
            CobolParser.StatementContext statement = (CobolParser.StatementContext) unit.getExecutionContext();
            CobolParser.PerformStatementContext performStatement = (CobolParser.PerformStatementContext) statement.children.get(0);
            if (performStatement.performInlineStatement() != null) {
                System.out.println("Inline PERFORM VARYING statement, skipping...");
                return new PassThrough();
            }
            String procedureName = performStatement.performProcedureStatement().procedureName().paragraphName().getText();
            FlowUnit performTarget = navigator.findTarget(procedureName);
            FlowNavigator flowNavigator = new FlowNavigator(performTarget.units());
            CobolFrame frame = new CobolFrame(flowNavigator, unit, this.frame);
            CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
            System.out.println("Branching to PERFORM procedure at " + performTarget.executionContextName());
            return vm2.run();
        }

        FlowNavigator flowNavigator = new FlowNavigator(unit.units());
        CobolFrame frame = new CobolFrame(flowNavigator, unit, this.frame);
        CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
        return vm2.run();
    }
}
