package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbTerminationCondition;

import java.util.List;
import java.util.Map;

/**
 * TerminationPart is one set of dataobjects and their respective state which can lead to
 * termination.
 */
public class TerminationPart extends DataConditions {

    public TerminationPart(String key) {
        DbTerminationCondition dbTerminationCondition = new DbTerminationCondition();
        // TODO implement
        // this.dataobjectIdToState = dbTerminationCondition.retrieveDataobjectIdToStateId(key);
    }

    public boolean checkTermination(List<DataObject> dataObjects) {
        return super.checkConditions(dataObjects);
    }
}