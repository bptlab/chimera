package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;
import jersey.repackaged.com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A class which represents an InputSet.
 */
public class InputSet extends Set implements IPersistable {

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
