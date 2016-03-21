package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;

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
        this.databaseId = connector.insertDataSetIntoDatabase(true);

        for (DataNode dataNode : this.dataNodes) {
            connector.insertDataSetConsistOfDataNodeIntoDatabase(this.databaseId,
                    dataNode.getDatabaseId());
            connector.insertDataFlowIntoDatabase(controlNode.getDatabaseId(), this.databaseId,
                    true);
        }

        return databaseId;
    }
}
