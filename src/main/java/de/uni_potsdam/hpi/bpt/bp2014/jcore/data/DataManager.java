package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The data manager
 */
public class DataManager {
    private final ScenarioInstance scenarioInstance;
    private List<DataObject> dataObjects = new ArrayList<>();

    public DataManager(ScenarioInstance instance) {
        this.scenarioInstance = instance;
    }

    public void loadDataObjects() {
        DbDataObject dbDataObject = new DbDataObject();
        List<Integer> dataObjectIds = dbDataObject.getDataObjectIds(
                scenarioInstance.getScenarioInstanceId());
        for (Integer dataObjectId : dataObjectIds) {
            this.dataObjects.add(new DataObject(dataObjectId, scenarioInstance));
        }
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
        Optional<DataObject> dataObjectInstance = getDataobjectInstanceForId(dataObjectId);
        if (dataObjectInstance.isPresent()) {
            dataObjectInstance.get().setState(stateId);
            int dataobjectInstanceId = dataObjectInstance.get().getId();
            new DbLogEntry().logDataobjectTransition(dataobjectInstanceId, stateId,
                activityInstanceId, this.scenarioInstance.getScenarioInstanceId());
            return true;
        }
        return false;
    }

    public Optional<DataObject> getDataobjectInstanceForId(int dataObjectId) {
        return this.dataObjects.stream()
                .filter(x -> x.getDataClassId() == dataObjectId).findFirst();
    }

    public List<DataAttributeInstance> getAllDataAttributeInstances() {
        return dataObjects.stream().map(DataObject::getDataAttributeInstances)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    /**
     * Returns the states of data objects for a scenario instance id.
     *
     * @return a Map. Keys are the data objects ids. Values are the states of the data objects.
     */
    public Map<Integer, Integer> getDataObjectStates() {
        return this.getDataObjects().stream().collect(Collectors.toMap(
                DataObject::getDataClassId, DataObject::getStateId)
        );
    }

    public boolean checkInputSet(int inputSetId) {
        DbDataNode dbDataNode = new DbDataNode();
        Map<Integer, Integer> dataObjectIdToStateId = getDataObjectStates();
        Set<Integer> lockedDataObjectIds = dataObjects.stream()
                .filter(DataObject::isLocked)
                .map(DataObject::getDataClassId)
                .collect(Collectors.toSet());
        List<de.uni_potsdam.hpi.bpt.bp2014.database.DataObject> inputSet = dbDataNode.getDataObjectsForDataSets(inputSetId);
        for (de.uni_potsdam.hpi.bpt.bp2014.database.DataObject dataObject : inputSet) {
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

    public List<DataObject> getDataObjects() {
        return dataObjects;
    }


    /**
     * Sets the data object to on change.
     * Write this into the database.
     *
     * @param dataObjectId This is the database id from the data object.
     * @return true if the on change could been set. false if not.
     */
    public Boolean lockDataobject(int dataObjectId) {
        Optional<DataObject> dataObjectInstance =
                this.getDataobjectInstanceForId(dataObjectId);

        if (dataObjectInstance.isPresent()) {
            dataObjectInstance.get().lock();
        }
        return false;
    }

}
