package de.hpi.bpt.chimera.jcore.data;

import de.hpi.bpt.chimera.database.data.DbTerminationCondition;

import java.util.List;

/**
 * TerminationPart is one set of dataobjects and their respective state which can lead to
 * termination.
 */
public class TerminationPart extends DataConditions {

    public TerminationPart(String conditionSetId) {
        DbTerminationCondition dbTerminationCondition = new DbTerminationCondition();
        this.dataClassToState = dbTerminationCondition.getDataClassToState(conditionSetId);
    }

    public boolean checkTermination(List<DataObject> dataObjects) {
        return super.checkConditions(dataObjects);
    }
}