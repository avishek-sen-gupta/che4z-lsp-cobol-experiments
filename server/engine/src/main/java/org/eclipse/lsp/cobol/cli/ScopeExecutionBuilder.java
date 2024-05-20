package org.eclipse.lsp.cobol.cli;

import com.google.common.collect.ImmutableList;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

public class ScopeExecutionBuilder {
    private final FlowUnit unit;
    private final GlobalFlowUnitNavigator globalNavigator;
    private final CobolFrame frame;

    public ScopeExecutionBuilder(FlowUnit unit, GlobalFlowUnitNavigator globalNavigator, CobolFrame frame) {
        this.unit = unit;
        this.globalNavigator = globalNavigator;
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
            FlowUnit performTarget = globalNavigator.findTarget(procedureName);
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
            GoToFlowUnit goToFlowUnit = (GoToFlowUnit) unit;
            String procedureName = goToFlowUnit.getProcedureName();
            System.out.println("Encountered a GOTO statement..." + procedureName);
            FlowUnit targetParaOrSection = globalNavigator.findTarget(procedureName);
            FlowUnit parentGroup = targetParaOrSection.getParent();
            FlowNavigator flowNavigator = null;
            List<FlowUnit> remainingExecutions;
            if (parentGroup.getExecutionContext().getClass() != CobolParser.ProcedureSectionContext.class) {
                // targetParaOrSection is a top-level unit, use this as the start frame
                remainingExecutions = globalNavigator.allFlowUnitsFrom(targetParaOrSection);
                flowNavigator = new FlowNavigator(remainingExecutions);
                CobolFrame frame = new CobolFrame(flowNavigator, this.frame, unit);
                CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
                System.out.println("Transferring to GO TO at " + targetParaOrSection.executionContextName());
                return vm2.run();
            } else {
                // targetParaOrSection is a paragraph
                // parentGroup is the top-level section
                // Enter parentGroup, but start from targetParaOrSection
                remainingExecutions = globalNavigator.allFlowUnitsFrom(parentGroup);
                flowNavigator = new FlowNavigator(remainingExecutions);
                CobolFrame frame = new CobolFrame(flowNavigator, this.frame, unit, new CustomEntryPoint(targetParaOrSection));
                CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
                System.out.println("Transferring to GO TO at " + targetParaOrSection.executionContextName());
                return vm2.run();
            }
        }
        else if (unit.scope() == ProgramScope.SECTION) {
            List<FlowUnit> remainingExecutions = unit.units();
            FlowNavigator flowNavigator = new FlowNavigator(remainingExecutions);
            CobolFrame frame = new CobolFrame(flowNavigator, this.frame, unit, this.frame.getIpStrategy());
            CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
            return vm2.run();
        }
        // Entering sections or paragraphs
        List<FlowUnit> remainingExecutions = unit.units();
        FlowNavigator flowNavigator = new FlowNavigator(remainingExecutions);
        CobolFrame frame = new CobolFrame(flowNavigator, this.frame, unit);
        CobolVM2 vm2 = new CobolVM2(frame, flowNavigator);
        return vm2.run();
    }
}
