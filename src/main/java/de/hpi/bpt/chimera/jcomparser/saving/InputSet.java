package de.hpi.bpt.chimera.jcomparser.saving;

import de.hpi.bpt.chimera.jcomparser.jaxb.DataNode;

import java.util.List;

/**
 * A class which represents an InputSet.
 */
public class InputSet extends AbstractSet implements IPersistable {

    public InputSet(AbstractControlNode controlNode, List<DataNode> dataNodes) {
        this.dataNodes = dataNodes;
        this.controlNode = controlNode;
    }

    @Override public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertDataSet(true);

        for (DataNode dataNode : this.dataNodes) {
            connector.insertDataSetConsistOfDataNode(this.databaseId,
                    dataNode.getDatabaseId());
            connector.insertDataFlow(controlNode.getDatabaseId(), this.databaseId,
                    true);
        }

        return databaseId;
    }
}
