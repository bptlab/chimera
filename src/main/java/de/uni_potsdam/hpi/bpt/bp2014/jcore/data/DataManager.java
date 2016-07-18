package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.*;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.SseNotifier;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The data manager provides an interface to the state of the scenario data objects
 * and attributes. It is responsible for proper locking of data object and
 * for logging changes in the state of the data.
 */
public class DataManager {
    private Logger log = Logger.getLogger(DataManager.class);
    private final ScenarioInstance scenarioInstance;

    private List<DataObject> dataObjects = new ArrayList<>();

    public DataManager(ScenarioInstance instance) {
        this.scenarioInstance = instance;
        loadFromDatabase();
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
    public Boolean changeDataObjectState(int dataObjectId, int stateId, int activityInstanceId) {
        Optional<DataObject> dataObject = getDataObjectForId(dataObjectId);
        if (dataObject.isPresent()) {
            dataObject.get().setState(stateId);
            int dataobjectId = dataObject.get().getId();
            new DbLogEntry().logDataObjectTransition(dataobjectId, stateId,
                activityInstanceId, this.scenarioInstance.getId());
            return true;
        }
        return false;
    }

    /**
     * Searches for data objects which fulfill one of the conditions of
     * the input set.
     * @param activityInstanceId Id of the activity instance
     * @return List of data objects
     */
    public List<DataObject> getAvailableInput(int activityId) {
        DbDataConditions conditions = new DbDataConditions();
        Map<String, Set<String>> input = conditions.loadInputSets(
                activityId);
        List<DataObject> dataobjectsFulfillingInputCondition = new ArrayList<>();
        for (DataObject dataObject : this.dataObjects) {
            if (input.containsKey(dataObject.getName())) {
                Set<String> possibleStates = input.get(dataObject.getName());
                if (possibleStates.contains(dataObject.getStateName())) {
                    dataobjectsFulfillingInputCondition.add(dataObject);
                }
            }
        }
        return dataobjectsFulfillingInputCondition;
    }

    /**
     * This method load all dataobjects for the scenario instance, with which the
     * DataManager was created. If the scenario instance is newly created, nothing happens
     * since there are no data object entries in the database.
     */
    public void loadFromDatabase() {
        DbDataObject dbDataObject = new DbDataObject();
        List<Integer> dataObjectIds = dbDataObject.getDataObjectIds(
                scenarioInstance.getId());
        this.dataObjects.clear();
        for (Integer dataObjectId : dataObjectIds) {
            this.dataObjects.add(new DataObject(dataObjectId, scenarioInstance));
        }
    }


    public Optional<DataObject> getDataObjectForId(int dataObjectId) {
        return this.dataObjects.stream()
                .filter(x -> x.getId() == dataObjectId).findFirst();
    }

    public List<DataAttributeInstance> getDataAttributeInstances() {
        return this.getDataObjects().stream().map(DataObject::getDataAttributeInstances)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


    public List<DataObject> getDataObjects() {
        return this.dataObjects;
    }


    /**
     * Sets the data object to on change.
     * Write this into the database.
     *
     * @param dataObjectId This is the database id from the data object.
     * @return true if the on change could been set. false if not.
     */
    public Boolean lockDataobject(int dataObjectId) {
        Optional<DataObject> dataObject =
                this.getDataObjectForId(dataObjectId);

        if (dataObject.isPresent()) {
            dataObject.get().lock();
            return true;
        }

        log.warn("Data object could not be locked");
        return false;
    }

    public boolean checkInputSet(Integer inputSetId) {
        InputSet inputSet = new InputSet(inputSetId);
        return inputSet.isFulfilled(dataObjects);
    }

    public void initializeDataObject(int dataClassId, int stateId) {
        DataObject dataObject = new DataObject(dataClassId, scenarioInstance, stateId);
        new DbLogEntry().logDataObjectCreation(
                dataObject.getId(), stateId, scenarioInstance.getId());
        this.dataObjects.add(dataObject);
    }

    /**
     * Translates the Map of dataclass name and state name
     * to a map of dataclass id and state id
     * for easier database access and use
     * @param dataClassNameToStateName map containing names
     * @return map containing ids
     */
    public Map<Integer, Integer> translate(Map<String, String> dataClassNameToStateName) {
        DbDataClass dbDataClass = new DbDataClass();
        DbState dbState = new DbState();
        Map<Integer, Integer> dataClassIdToStateId = new HashMap<>();
        for(Map.Entry<String, String> entry : dataClassNameToStateName.entrySet()) {
            int dataClassId = dbDataClass.getId(entry.getKey(), this.scenarioInstance.getScenarioId());
            dataClassIdToStateId.put(
                    dataClassId,
                    dbState.getStateId(dataClassId, entry.getValue()));
        }
        return dataClassIdToStateId;
    }

    /**
     * Sets the values of data attributes and logs the attribute change.
     * Checks whether the values are of valid type for the attribute.
     *
     * @param activityInstanceId needed for the log entry
     * @param idToValue map of attribute instance id to new value
     * @return true if all attributes could be set correctly, false if
     * an attribute was of the wrong data type
     */
    public boolean setAttributeValues(int activityInstanceId, Map<Integer, String> idToValue) {
        boolean allValuesValid = true;
        for (Map.Entry<Integer, String> attributeInstanceIdToValue : idToValue.entrySet()) {
            Integer dataAttributeInstanceId = attributeInstanceIdToValue.getKey();
            String value = attributeInstanceIdToValue.getValue();
            DataAttributeInstance dataAttributeInstance =
                    new DataAttributeInstance(dataAttributeInstanceId);
            if (dataAttributeInstance.isValueAllowed(value)) {
                new DbLogEntry().logDataAttributeTransition(dataAttributeInstanceId, value,
                        activityInstanceId, scenarioInstance.getId());
                dataAttributeInstance.setValue(value);
                updateDataObjectAttribute(dataAttributeInstance);
            } else {
                log.error("Attribute value could not be set "
                        + "because it has the wrong data type.");
                SseNotifier.notifyError("Attribute value could nto be set "
                        + "because it has the wrong data type.");
                allValuesValid = false;
            }
        }
        return allValuesValid;
    }

    private void updateDataObjectAttribute(DataAttributeInstance attributeInstance) {
        DbDataAttributeInstance db = new DbDataAttributeInstance();
        int attributeInstanceId = attributeInstance.getId();
        Optional<DataObject> object =
                getDataObjectForId(db.getDataObjectId(attributeInstanceId));
        if (object.isPresent()) {
            object.get().getDataAttributeInstanceMap().put(attributeInstanceId, attributeInstance);
        }
    }

    public void setDataObjects(List<DataObject> dataObjects) {
        this.dataObjects = dataObjects;
    }
}
