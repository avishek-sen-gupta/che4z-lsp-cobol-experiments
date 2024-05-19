package org.eclipse.lsp.cobol.cli;

import lombok.Getter;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.List;

public class GoToFlowUnit extends FlowUnit {

    private final CobolParser.GoToStatementContext goToStatement;
    @Getter private final CobolParser.ProcedureNameContext procedureName;

    public GoToFlowUnit(CobolParser.StatementContext s) {
        super(s);
        goToStatement = (CobolParser.GoToStatementContext) s.getChild(0);

        // Will need to handle multiple GO TOs in the future
        procedureName = goToStatement.procedureName().get(0);
    }

    @Override
    public List<FlowUnit> units() {
        // This will contain a sublist of all top-level sections/paragraphs from where to continue execution
        return null;
    }

    @Override
    public void buildChildren() {
    }

    @Override
    public ProgramScope scope() {
        return ProgramScope.GOTO;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }
}
