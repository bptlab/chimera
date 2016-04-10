package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The data manager
 */
public class DataManager {
    private final ScenarioInstance scenarioInstance;
    private List<DataObjectInstance> dataObjectInstances = new ArrayList<>();
    private List<DataObjectInstance> dataObjectInstancesOnChange = new ArrayList<>();

    public DataManager(ScenarioInstance instance) {
        this.scenarioInstance = instance;
        initializeDataObjects();
    }

    /**
     * Changes the state of a data object instance if there exists one matching the passed id.
     * Logs the appropriate change to the database.
     *
     * @param dataObjectId Id of the dataobject, to which the instance belongs
     * @param stateId Id of the new state
     * @param activityId id of the activity which changed the dataobject
     * @return Returns if the state was successfully changed
     */
    public Boolean changeDataObjectInstanceState(int dataObjectId, int stateId, int activityId) {
        Optional<DataObjectInstance> dataObjectInstance = getDataobjectInstanceForId(dataObjectId);
        if (dataObjectInstance.isPresent()) {
            dataObjectInstance.get().setState(stateId);
            HistoryLogger scenarioLog = new HistoryLogger();
            scenarioLog.logDataobjectStateTransition(
                    dataObjectInstance.get().getDataObjectInstanceId(), stateId, activityId);
            return true;
        }

        return false;

    }

    public Optional<DataObjectInstance> getDataobjectInstanceForId(int dataObjectId) {
        return this.dataObjectInstances.stream()
                .filter(x -> x.getDataObjectId() == dataObjectId).findFirst();
    }

    /**
     * Initializes all data objects for the scenario instance.
     */
    private void initializeDataObjects() {
        HistoryLogger logger = new HistoryLogger();
        DbDataObject dbDataObject = new DbDataObject();
        int scenarioId = this.scenarioInstance.getScenarioId();
        int scenarioInstanceId = this.scenarioInstance.getScenarioInstanceId();

        List<Integer> data = dbDataObject.getDataObjectsForScenario(scenarioId);
        for (Integer dataObject : data) {
            DataObjectInstance dataObjectInstance = new DataObjectInstance(
                    dataObject, scenarioId, scenarioInstanceId, scenarioInstance);
            logger.logDataObjectCreation(dataObjectInstance.getDataObjectInstanceId());
            //checks if dataObjectInstance is locked
            if (dataObjectInstance.getOnChange()) {
                dataObjectInstancesOnChange.add(dataObjectInstance);
            } else {
                dataObjectInstances.add(dataObjectInstance);
            }
        }
    }
    public List<DataObjectInstance> getDataObjectInstances() {
        return dataObjectInstances;
    }

    public List<DataObjectInstance> getDataObjectInstancesOnChange() {
        return dataObjectInstancesOnChange;
    }
}
