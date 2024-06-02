package poc.common.flowchart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VisitContext {
    private int level;

    public VisitContext oneLower() {
        return new VisitContext(level + 1);
    }
}
