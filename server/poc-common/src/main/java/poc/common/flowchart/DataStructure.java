package poc.common.flowchart;

import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import org.eclipse.lsp.cobol.core.CobolParser;

import java.util.ArrayList;
import java.util.List;

public class DataStructure extends SimpleTreeNode {
    private CobolParser.DataDescriptionEntryFormat1Context dataDescription;
    private final int levelNumber;
    private List<DataStructure> structures = new ArrayList<>();
    private DataStructure parent;
    private List<ConditionalDataStructure> conditions = new ArrayList<>();

    @Override
    public String content() {
        return dataDescription != null ? dataDescription.getText() : "<ROOT>";
    }

    @Override
    public List<TreeNode> children() {
        return new ArrayList<>(structures);
    }

    public DataStructure(CobolParser.DataDescriptionEntryFormat1Context dataDescription) {
        super(dataDescription.getText());
        this.dataDescription = dataDescription;
        levelNumber = Integer.valueOf(dataDescription.levelNumber().getText());
    }

    public DataStructure(int levelNumber) {
        super("<ROOT>");
        this.levelNumber = levelNumber;
    }

    public DataStructure parent() {
        return parent;
    }

    public DataStructure addChild(DataStructure dataStructure) {
        dataStructure.setParent(this);
        structures.add(dataStructure);
        return dataStructure;
    }

    private void setParent(DataStructure dataStructure) {
        this.parent = dataStructure;
    }

    public int level() {
        return levelNumber;
    }

    public DataStructure parent(int level) {
        DataStructure current = this;
        while(current != null && current.level() > level) {
            current = current.parent();
        }
        if (current == null) throw new RuntimeException("Parent of level " + level + " does not exist!");
        return current;
    }

    public DataStructure addPeer(DataStructure peer) {
        return parent.addChild(peer);
    }

    public DataStructure addConditionalVariable(ConditionalDataStructure conditionalDataStructure) {
        conditions.add(conditionalDataStructure);
        return this;
    }

    public void report() {
        new ListingTreePrinter().print(this);
    }
}
