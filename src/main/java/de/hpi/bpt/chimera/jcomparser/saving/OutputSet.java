package de.hpi.bpt.chimera.jcomparser.saving;


import de.hpi.bpt.chimera.jcomparser.jaxb.DataNode;

import java.util.List;

/**
 * This class represents an OutputSet.
 */
public class OutputSet extends AbstractSet implements IPersistable {

	public OutputSet(AbstractControlNode controlNode, List<DataNode> dataNodes) {
		this.dataNodes = dataNodes;
		this.controlNode = controlNode;
	}

	@Override
	public int save() {
		Connector connector = new Connector();
		this.databaseId = connector.insertDataSet(false);

		for (DataNode dataNode : this.dataNodes) {
			connector.insertDataSetConsistOfDataNode(this.databaseId, dataNode.getDatabaseId());
			connector.insertDataFlow(controlNode.getDatabaseId(), this.databaseId, false);
		}

		return databaseId;
	}


}
