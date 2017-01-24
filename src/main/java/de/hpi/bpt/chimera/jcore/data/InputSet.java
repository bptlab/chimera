package de.hpi.bpt.chimera.jcore.data;

import de.hpi.bpt.chimera.database.data.DbDataNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class InputSet extends DataConditions {
	public InputSet(int inputSetId) {
		DbDataNode dbDataNode = new DbDataNode();
		this.dataClassToState = dbDataNode.getDataSetClassToStateMap(inputSetId);
	}

	public boolean isFulfilled(List<DataObject> dataObjects) {
		dataObjects = dataObjects.stream().filter(x -> !x.isLocked()).collect(Collectors.toList());
		return super.checkConditions(dataObjects);
	}
}
