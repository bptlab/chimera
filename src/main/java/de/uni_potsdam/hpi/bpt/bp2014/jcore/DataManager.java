package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.jhistory.Log;

import java.util.Optional;

/**
 * The data manager
 */
public class DataManager {
    private final ScenarioInstance scenarioInstance;

    public DataManager(ScenarioInstance instance) {
        this.scenarioInstance = instance;
    }


    public Boolean changeDataObjectInstanceState(int dataObjectId, int stateId) {
        Optional<DataObjectInstance> dataObjectInstance =
                this.scenarioInstance.getDataObjectInstances().stream()
                        .filter(x -> x.getDataObjectId() == dataObjectId).findFirst();
        if (dataObjectInstance.isPresent()) {
            dataObjectInstance.get().setState(stateId);
            Log scenarioLog = new Log();

            // TODO implement properly
            scenarioLog.logDataobjectStateTransition(0, 0);
            return true;
        }

        return false;

    }
}
