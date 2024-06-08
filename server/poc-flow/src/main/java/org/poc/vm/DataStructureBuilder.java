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
        Optional<CobolParser.DataDivisionSectionContext> maybeWorkingStorage = dataDivisionBody.dataDivisionSection().stream().filter(s -> s.workingStorageSection() != null).findFirst();
        CobolParser.WorkingStorageSectionContext workingStorageSection = maybeWorkingStorage.get().workingStorageSection();
        List<CobolParser.DataDescriptionEntryForWorkingStorageSectionContext> workingStorageDataLayouts = workingStorageSection.dataDescriptionEntryForWorkingStorageSection();
        extractFrom2(workingStorageDataLayouts, zerothStructure, this::wsData);

        Optional<CobolParser.DataDivisionSectionContext> maybeLinkageSection = dataDivisionBody.dataDivisionSection().stream().filter(s -> s.linkageSection() != null).findFirst();
        CobolParser.LinkageSectionContext linkageSection = maybeLinkageSection.get().linkageSection();
        List<CobolParser.DataDescriptionEntryForWorkingStorageAndLinkageSectionContext> linkageSectionDataLayouts = linkageSection.dataDescriptionEntryForWorkingStorageAndLinkageSection();
        extractFrom2(linkageSectionDataLayouts, zerothStructure, this::linkageData);

        return zerothStructure;
    }

    private CobolParser.DataDescriptionEntryContext wsData(CobolParser.DataDescriptionEntryForWorkingStorageSectionContext e) {
        return e.dataDescriptionEntryForWorkingStorageAndLinkageSection().dataDescriptionEntry();
    }

    private CobolParser.DataDescriptionEntryContext linkageData(CobolParser.DataDescriptionEntryForWorkingStorageAndLinkageSectionContext e) {
        return e.dataDescriptionEntry();
    }

    private void extractFrom(List<CobolParser.DataDescriptionEntryForWorkingStorageSectionContext> dataLayouts) {
        int currentLevel = 0;
        DataStructure dataStructure = zerothStructure;
        for (CobolParser.DataDescriptionEntryForWorkingStorageSectionContext dataDescriptionEntry : dataLayouts) {
            CobolParser.DataDescriptionEntryContext dataDescription = dataDescriptionEntry.dataDescriptionEntryForWorkingStorageAndLinkageSection().dataDescriptionEntry();
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
                    dataStructure = dataStructure.parent(entryLevel - 1).addChild(new DataStructure(format1));
                }
            } else if (dataDescription.dataDescriptionEntryFormat3() != null) {
                CobolParser.DataDescriptionEntryFormat3Context conditionalFormat = dataDescription.dataDescriptionEntryFormat3();
                dataStructure = dataStructure.addConditionalVariable(new ConditionalDataStructure(conditionalFormat));
            }
            currentLevel = dataStructure.level();
        }
    }

    private static <T> DataStructure extractFrom2(List<T> dataLayouts, DataStructure root, Function<T, CobolParser.DataDescriptionEntryContext> retriever) {
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
                    dataStructure = dataStructure.parent(entryLevel - 1).addChild(new DataStructure(format1));
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
