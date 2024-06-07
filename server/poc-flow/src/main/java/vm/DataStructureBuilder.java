package vm;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.ConditionalDataStructure;
import poc.common.flowchart.DataStructure;

import java.util.List;
import java.util.Optional;

public class DataStructureBuilder {
    private final CobolEntityNavigator navigator;
    private DataStructure zerothStructure;

    public DataStructureBuilder(CobolEntityNavigator navigator) {
        this.navigator = navigator;
    }

    public DataStructure build() {
        ParseTree dataDivision = navigator.dataDivisionBodyRoot();
        CobolParser.DataDivisionContext dataDivisionBody = (CobolParser.DataDivisionContext) dataDivision;
        Optional<CobolParser.DataDivisionSectionContext> maybeWorkingStorage = dataDivisionBody.dataDivisionSection().stream().filter(s -> s.workingStorageSection() != null).findFirst();
        CobolParser.WorkingStorageSectionContext workingStorageSection = maybeWorkingStorage.get().workingStorageSection();
        List<CobolParser.DataDescriptionEntryForWorkingStorageSectionContext> dataLayouts = workingStorageSection.dataDescriptionEntryForWorkingStorageSection();
        zerothStructure = new DataStructure(0);
        int currentLevel = 0;
        DataStructure dataStructure = null;
        for (CobolParser.DataDescriptionEntryForWorkingStorageSectionContext dataDescriptionEntry : dataLayouts) {
            CobolParser.DataDescriptionEntryContext dataDescription = dataDescriptionEntry.dataDescriptionEntryForWorkingStorageAndLinkageSection().dataDescriptionEntry();
            if (dataDescription.dataDescriptionEntryFormat1() != null) {
                CobolParser.DataDescriptionEntryFormat1Context format1 = dataDescription.dataDescriptionEntryFormat1();
                int entryLevel = Integer.valueOf(format1.levelNumber().LEVEL_NUMBER().getSymbol().getText()).intValue();
                if (currentLevel == 0) {
                    if (entryLevel != 1) throw new RuntimeException("Top Level entry must be 01");
                    dataStructure = zerothStructure.addChild(new DataStructure(format1));
                } else if (entryLevel == currentLevel) {
                    dataStructure = dataStructure.addPeer(new DataStructure(format1));
                } else if (entryLevel > currentLevel) {
                    dataStructure = dataStructure.addChild(new DataStructure(format1));
                } else {
                    dataStructure = dataStructure.parent(entryLevel - 1).addChild(new DataStructure(format1));
                }
            } else if (dataDescription.dataDescriptionEntryFormat3() != null) {
                CobolParser.DataDescriptionEntryFormat3Context conditionalFormat = dataDescription.dataDescriptionEntryFormat3();
                dataStructure = dataStructure.addConditionalVariable(new ConditionalDataStructure(conditionalFormat));
            }
            currentLevel = dataStructure.level();
        }
        return zerothStructure;
    }
}
