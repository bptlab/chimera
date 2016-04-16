package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryLogger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The data manager
 */
public class DataManager {
    private final ScenarioInstance scenarioInstance;
    private List<DataObjectInstance> dataObjectInstances = new ArrayList<>();

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
     * @param activityInstanceId id of the activity instance which changed the dataobject
     * @return Returns if the state was successfully changed
     */
    public Boolean changeDataObjectInstanceState(int dataObjectId, int stateId, int activityInstanceId) {
        Optional<DataObjectInstance> dataObjectInstance = getDataobjectInstanceForId(dataObjectId);
        if (dataObjectInstance.isPresent()) {
            dataObjectInstance.get().setState(stateId);
            HistoryLogger stateLogger = new HistoryLogger();
            stateLogger.logDataobjectStateTransition(
                    dataObjectInstance.get().getDataObjectInstanceId(), stateId, activityInstanceId);
            return true;
        }

        return false;

    }

    public Optional<DataObjectInstance> getDataobjectInstanceForId(int dataObjectId) {
        return this.dataObjectInstances.stream()
                .filter(x -> x.getDataObjectId() == dataObjectId).findFirst();
    }

    /**
     * Returns the states of data objects for a scenario instance id.
     *
     * @return a Map. Keys are the data objects ids. Values are the states of the data objects.
     */
    public Map<Integer, Integer> getDataObjectStates() {
        return this.getDataObjectInstances().stream().collect(Collectors.toMap(
                DataObjectInstance::getDataObjectId, DataObjectInstance::getStateId)
        );
    }

    public boolean checkInputSet(int inputSetId) {
        DbDataNode dbDataNode = new DbDataNode();
        Map<Integer, Integer> dataObjectIdToStateId = getDataObjectStates();
        Set<Integer> lockedDataObjectIds = dataObjectInstances.stream()
                .filter(DataObjectInstance::isLocked)
                .map(DataObjectInstance::getDataObjectId)
                .collect(Collectors.toSet());
        List<DataObject> inputSet = dbDataNode.getDataObjectsForDataSets(inputSetId);
        for (DataObject dataObject : inputSet) {
            Integer dataObjectId = dataObject.getId();
            if (lockedDataObjectIds.contains(dataObjectId)) {
                return false;
            }
            if (!(dataObjectIdToStateId.get(dataObjectId) == dataObject.getStateID())) {
                return false;
            }
        }
        return true;
    }


    /**
     * Initializes all data objects for the scenario instance.
     */
    private void initializeDataObjects() {
        HistoryLogger dataObjectLogger = new HistoryLogger();
        DbDataObject dbDataObject = new DbDataObject();
        int scenarioId = this.scenarioInstance.getScenarioId();
        int scenarioInstanceId = this.scenarioInstance.getScenarioInstanceId();

        List<Integer> data = dbDataObject.getDataObjectsForScenario(scenarioId);
        for (Integer dataObject : data) {
            DataObjectInstance dataObjectInstance = new DataObjectInstance(
                    dataObject, scenarioId, scenarioInstanceId, scenarioInstance);
            this.dataObjectInstances.add(dataObjectInstance);
            dataObjectLogger.logDataObjectCreation(dataObjectInstance.getDataObjectInstanceId());
        }
    }

    public List<DataObjectInstance> getDataObjectInstances() {
        return dataObjectInstances;
    }


    /**
     * Sets the data object to on change.
     * Write this into the database.
     *
     * @param dataObjectId This is the database id from the data object.
     * @return true if the on change could been set. false if not.
     */
    public Boolean lockDataobject(int dataObjectId) {
        Optional<DataObjectInstance> dataObjectInstance =
                this.getDataobjectInstanceForId(dataObjectId);

        if (dataObjectInstance.isPresent()) {
            dataObjectInstance.get().lock();
        }
        return false;
    }

}
