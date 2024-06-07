package vm;

import org.poc.flowchart.CobolStackFrames;
import org.poc.flowchart.IfChartNode;
import org.poc.flowchart.ParagraphChartNode;
import org.poc.flowchart.SectionChartNode;
import poc.common.flowchart.*;

import java.util.List;

public class CobolInterpreterFactory {

    public static CobolInterpreter interpreter() {
        return new SmolCobolInterpreter(new CobolStackFrames(), ExecuteCondition.ALWAYS_EXECUTE);
    }

    public static CobolInterpreter interpreter(ExecuteCondition condition, StackFrames runtimeStackFrames) {
        return new SmolCobolInterpreter(runtimeStackFrames, condition);
    }
}
