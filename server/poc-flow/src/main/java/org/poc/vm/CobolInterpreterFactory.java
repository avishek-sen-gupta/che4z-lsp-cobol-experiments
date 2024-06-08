package org.poc.vm;

import org.poc.flowchart.CobolStackFrames;
import poc.common.flowchart.*;

public class CobolInterpreterFactory {

    public static CobolInterpreter interpreter() {
        return interpreter(new ConsoleInputResolver(), new CobolBreakPointer());
    }

    public static CobolInterpreter interpreter(ConditionResolver conditionResolver, Breakpointer bp) {
        return interpreter(ExecuteCondition.ALWAYS_EXECUTE, new CobolStackFrames(), conditionResolver, bp);
    }

    public static CobolInterpreter interpreter(ExecuteCondition condition, StackFrames runtimeStackFrames, ConditionResolver conditionResolver, Breakpointer bp) {
        return new SmolCobolInterpreter(runtimeStackFrames, condition, conditionResolver, bp);
    }
}
