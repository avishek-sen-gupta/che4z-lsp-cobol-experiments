package org.poc.vm;

import org.poc.flowchart.CobolStackFrames;
import poc.common.flowchart.*;

public class CobolInterpreterFactory {

    public static CobolInterpreter interpreter() {
        return interpreter(new ConsoleInputResolver());
    }

    public static CobolInterpreter interpreter(ConditionResolver conditionResolver) {
        return interpreter(ExecuteCondition.ALWAYS_EXECUTE, new CobolStackFrames(), conditionResolver);
    }

    public static CobolInterpreter interpreter(ExecuteCondition condition, StackFrames runtimeStackFrames, ConditionResolver conditionResolver) {
        return new SmolCobolInterpreter(runtimeStackFrames, condition, conditionResolver);
    }
}
