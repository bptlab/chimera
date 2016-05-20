package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The data manager
 */
public class DataManager {
    private Logger log = Logger.getLogger(DataManager.class);
    private final ScenarioInstance scenarioInstance;
    private DataState dataState;

    public DataManager(ScenarioInstance instance) {
        this.scenarioInstance = instance;
        this.dataState = new DataState(instance);
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
        Optional<DataObject> dataObjectInstance = getDataobjectForId(dataObjectId);
        if (dataObjectInstance.isPresent()) {
            dataObjectInstance.get().setState(stateId);
            int dataobjectId = dataObjectInstance.get().getId();
            new DbLogEntry().logDataobjectTransition(dataobjectId, stateId,
                activityInstanceId, this.scenarioInstance.getScenarioInstanceId());
            return true;
        }
        return false;
    }

    public Optional<DataObject> getDataobjectForId(int dataObjectId) {
        return this.dataState.getDataObjects().stream()
                .filter(x -> x.getId() == dataObjectId).findFirst();
    }

    public List<DataAttributeInstance> getAllDataAttributeInstances() {
        return this.getDataObjects().stream().map(DataObject::getDataAttributeInstances)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


    public List<DataObject> getDataObjects() {
        return this.dataState.getDataObjects();
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
                this.getDataobjectForId(dataObjectId);

        if (dataObject.isPresent()) {
            dataObject.get().lock();
            return true;
        }

        log.warn("Data object could not be locked");
        return false;
    }

    public boolean checkInputSet(Integer inputSet) {
        return this.dataState.checkInputSet(inputSet);
    }
}
