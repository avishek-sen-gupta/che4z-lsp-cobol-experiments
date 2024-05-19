package org.eclipse.lsp.cobol.cli;

import com.google.common.collect.ImmutableList;
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
            FlowNavigator flowNavigator = new FlowNavigator(ImmutableList.of(performTarget));
            CobolFrame frame = new CobolFrame(flowNavigator, this.frame, unit);
            CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
            System.out.println("Branching to PERFORM procedure at " + performTarget.executionContextName());
            return vm2.run();
        }
        else if (unit.scope() == ProgramScope.IF) {
            FlowNavigator flowNavigator = new FlowNavigator(unit.units());
            CobolFrame frame = new CobolFrame(flowNavigator, this.frame, unit);
            CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
            System.out.println("Branching to IF...THEN at " + unit.executionContextName());
            return vm2.run();
        }
        else if (unit.scope() == ProgramScope.GOTO) {
            System.out.println("Encountered a GOTO statement, but ignoring for now..." + ((GoToFlowUnit) unit).getProcedureName().getText());
            return new PassThrough();
//            FlowNavigator flowNavigator = new FlowNavigator(unit.units());
//            CobolFrame frame = new CobolFrame(flowNavigator, this.frame, unit);
//            CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
//            System.out.println("Branching to IF...THEN at " + unit.executionContextName());
//            return vm2.run();
        }

        FlowNavigator flowNavigator = new FlowNavigator(unit.units());
        CobolFrame frame = new CobolFrame(flowNavigator, this.frame, unit);
        CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
        return vm2.run();
    }
}
