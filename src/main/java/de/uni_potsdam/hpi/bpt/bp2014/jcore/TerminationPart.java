package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbTerminationCondition;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class TerminationPart {
    Map<Integer, Integer> dataobjectIdToState;
    public TerminationPart(String key) {
        DbTerminationCondition dbTerminationCondition = new DbTerminationCondition();
        this.dataobjectIdToState = dbTerminationCondition.retrieveDataobjectIdToStateId(key);
    }

    public boolean checkTermination(List<DataObjectInstance> dataObjectInstances) {
        for (DataObjectInstance instance : dataObjectInstances) {
            Integer dataObjectName = instance.getDataObjectId();
            if (dataobjectIdToState.get(dataObjectName) != instance.getStateId()) {
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