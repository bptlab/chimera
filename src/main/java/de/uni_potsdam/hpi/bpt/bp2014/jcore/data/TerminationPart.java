package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbTerminationCondition;

import java.util.List;
import java.util.Map;

/**
 * TerminationPart is one set of dataobjects and their respective state which can lead to
 * termination.
 */
public class TerminationPart {
    Map<Integer, Integer> dataobjectIdToState;
    public TerminationPart(String key) {
        DbTerminationCondition dbTerminationCondition = new DbTerminationCondition();
        this.dataobjectIdToState = dbTerminationCondition.retrieveDataobjectIdToStateId(key);
    }

    public boolean checkTermination(List<DataObject> dataObjects) {
        for (DataObject instance : dataObjects) {
            Integer dataObjectId = instance.getDataClassId();
            // Data objects which are not present in this termination condition part don't matter
            if (!dataobjectIdToState.containsKey(dataObjectId)) {
                continue;
            }
            if (dataobjectIdToState.get(dataObjectId) != instance.getStateId()) {
                return false;
            }
        }
        return true;
    }

    public Map<Integer, Integer> getDataobjectToState() {
        return dataobjectIdToState;
    }

    public void setDataobjectToState(Map<Integer, Integer> dataobjectToState) {
        this.dataobjectIdToState = dataobjectToState;
    }
}