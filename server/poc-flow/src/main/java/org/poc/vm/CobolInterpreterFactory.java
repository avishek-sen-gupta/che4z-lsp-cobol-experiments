package org.poc.vm;

import org.poc.flowchart.CobolStackFrames;
import poc.common.flowchart.*;

public class CobolInterpreterFactory {

    public static CobolInterpreter interpreter() {
        return new SmolCobolInterpreter(new CobolStackFrames(), ExecuteCondition.ALWAYS_EXECUTE, new ConsoleInputResolver());
    }

    public static CobolInterpreter interpreter(ExecuteCondition condition, StackFrames runtimeStackFrames) {
        return new SmolCobolInterpreter(runtimeStackFrames, condition, new ConsoleInputResolver());
    }
}
