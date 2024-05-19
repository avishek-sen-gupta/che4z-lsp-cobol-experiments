package org.eclipse.lsp.cobol.cli;

import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;
import java.util.stream.Collectors;

public class IfFlowUnit extends FlowUnit {
    private CobolParser.IfStatementContext ifStatement;
    private List<FlowUnit> thenStatements;

    @Override
    public boolean isAtomic() {
        return false;
    }

    private List<FlowUnit> elseStatements;

    public IfFlowUnit(CobolParser.StatementContext ifStatement) {
        super(ifStatement);
        this.ifStatement = (CobolParser.IfStatementContext) ifStatement.children.get(0);
    }


    @Override
    public void buildChildren() {
        thenStatements = ifStatement.ifThen().conditionalStatementCall().stream().map(c -> {
            return statementFlowUnit(c.statement());
        }).collect(Collectors.toList());
        if (ifStatement.ifElse() != null)
            elseStatements = ifStatement.ifElse().conditionalStatementCall().stream().map(c -> {
                return statementFlowUnit(c.statement());
            }).collect(Collectors.toList());
    }

    @Override
    public String executionContextName() {
        return "" + ifStatement.start.getLine();
    }

    @Override
    public List<FlowUnit> units() {
        // This will change depend on condition evaluation
        return thenStatements;
    }

    @Override
    public ProgramScope scope() {
        return ProgramScope.IF;
    }
}
