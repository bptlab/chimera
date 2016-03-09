package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving;


import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;

import java.util.List;

/**
 * This class represents an OutputSet.
 */
public class OutputSet extends Set implements IPersistable {

    public OutputSet(AbstractControlNode controlNode, List<DataNode> dataNodes) {
        this.dataNodes = dataNodes;
        this.controlNode = controlNode;
    }

    @Override public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertDataSetIntoDatabase(false);

        for (DataNode dataNode : this.dataNodes) {
            connector.insertDataSetConsistOfDataNodeIntoDatabase(this.databaseId,
                    dataNode.getDatabaseId());
            connector.insertDataFlowIntoDatabase(controlNode.getDatabaseId(), this.databaseId,
                    false);
        }

        return databaseId;
    }


}
