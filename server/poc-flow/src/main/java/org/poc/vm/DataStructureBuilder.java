package org.poc.vm;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.ConditionalDataStructure;
import poc.common.flowchart.DataStructure;
import poc.common.flowchart.IDataStructureBuilder;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DataStructureBuilder implements IDataStructureBuilder {
    private final CobolEntityNavigator navigator;
    private DataStructure zerothStructure;

    public DataStructureBuilder(CobolEntityNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public DataStructure build() {
        zerothStructure = new DataStructure(0);
        ParseTree dataDivision = navigator.dataDivisionBodyRoot();
        CobolParser.DataDivisionContext dataDivisionBody = (CobolParser.DataDivisionContext) dataDivision;
        extractFromWorkingStorage(dataDivisionBody);
        extractFromLinkage(dataDivisionBody);
        return zerothStructure;
    }

    private void extractFromLinkage(CobolParser.DataDivisionContext dataDivisionBody) {
        Optional<CobolParser.DataDivisionSectionContext> maybeLinkageSection = dataDivisionBody.dataDivisionSection().stream().filter(s -> s.linkageSection() != null).findFirst();
        CobolParser.LinkageSectionContext linkageSection = maybeLinkageSection.get().linkageSection();
        List<CobolParser.DataDescriptionEntryForWorkingStorageAndLinkageSectionContext> linkageSectionDataLayouts = linkageSection.dataDescriptionEntryForWorkingStorageAndLinkageSection();
        extractFrom(linkageSectionDataLayouts, zerothStructure, this::linkageData);
    }

    private void extractFromWorkingStorage(CobolParser.DataDivisionContext dataDivisionBody) {
        Optional<CobolParser.DataDivisionSectionContext> maybeWorkingStorage = dataDivisionBody.dataDivisionSection().stream().filter(s -> s.workingStorageSection() != null).findFirst();
        CobolParser.WorkingStorageSectionContext workingStorageSection = maybeWorkingStorage.get().workingStorageSection();
        List<CobolParser.DataDescriptionEntryForWorkingStorageSectionContext> workingStorageDataLayouts = workingStorageSection.dataDescriptionEntryForWorkingStorageSection();
        extractFrom(workingStorageDataLayouts, zerothStructure, this::wsData);
    }

    private CobolParser.DataDescriptionEntryContext wsData(CobolParser.DataDescriptionEntryForWorkingStorageSectionContext e) {
        return e.dataDescriptionEntryForWorkingStorageAndLinkageSection().dataDescriptionEntry();
    }

    private CobolParser.DataDescriptionEntryContext linkageData(CobolParser.DataDescriptionEntryForWorkingStorageAndLinkageSectionContext e) {
        return e.dataDescriptionEntry();
    }

    // TODO: Refactor to state machine maybe
    private <T> DataStructure extractFrom(List<T> dataLayouts, DataStructure root, Function<T, CobolParser.DataDescriptionEntryContext> retriever) {
        int currentLevel = 0;
        DataStructure dataStructure = root;
        for (T dataDescriptionEntry : dataLayouts) {
            CobolParser.DataDescriptionEntryContext dataDescription = retriever.apply(dataDescriptionEntry);
            if (dataDescription.dataDescriptionEntryFormat1() != null) {
                CobolParser.DataDescriptionEntryFormat1Context format1 = dataDescription.dataDescriptionEntryFormat1();
                int entryLevel = Integer.valueOf(format1.levelNumber().LEVEL_NUMBER().getSymbol().getText());
                if (currentLevel == 0) {
                    if (entryLevel != 1) throw new RuntimeException("Top Level entry must be 01");
                    dataStructure = dataStructure.addChild(new DataStructure(format1));
                } else if (entryLevel == currentLevel) {
                    dataStructure = dataStructure.addPeer(new DataStructure(format1));
                } else if (entryLevel > currentLevel) {
                    dataStructure = dataStructure.addChild(new DataStructure(format1));
                } else {
                    dataStructure = dataStructure.parent(entryLevel).addChild(new DataStructure(format1));
                }
            } else if (dataDescription.dataDescriptionEntryFormat3() != null) {
                CobolParser.DataDescriptionEntryFormat3Context conditionalFormat = dataDescription.dataDescriptionEntryFormat3();
                dataStructure = dataStructure.addConditionalVariable(new ConditionalDataStructure(conditionalFormat));
            }
            currentLevel = dataStructure.level();
        }

        return root;
    }
}
