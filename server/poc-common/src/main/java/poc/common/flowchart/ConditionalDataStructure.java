package poc.common.flowchart;

import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;

public class ConditionalDataStructure {
    private CobolParser.DataDescriptionEntryFormat3Context dataDescription;
    private List<ConditionalDataStructure> structures = new ArrayList<>();
    private ConditionalDataStructure parent;

    public ConditionalDataStructure(CobolParser.DataDescriptionEntryFormat3Context dataDescription) {
        this.dataDescription = dataDescription;
    }
}
