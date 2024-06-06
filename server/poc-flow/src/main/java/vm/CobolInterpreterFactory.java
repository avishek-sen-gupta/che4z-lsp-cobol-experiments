package vm;

import org.poc.flowchart.CobolStackFrames;
import org.poc.flowchart.IfChartNode;
import org.poc.flowchart.ParagraphChartNode;
import org.poc.flowchart.SectionChartNode;
import poc.common.flowchart.*;

import java.util.List;

public class CobolInterpreterFactory {

    public static CobolInterpreter interpreter() {
        return new CobolInterpreterProxy(ExecuteCondition.ALWAYS_EXECUTE, new SmolCobolInterpreter());
    }

    public static CobolInterpreter interpreter(ExecuteCondition condition, StackFrames runtimeStackFrames) {
        return new CobolInterpreterProxy(condition, new SmolCobolInterpreter(runtimeStackFrames));
    }
}
