package de.hpi.bpt.chimera.jcore.executionbehaviors.scripttasks.context;

import bpt.chimera.scripttasklibrary.IChimeraContext;

import de.hpi.bpt.chimera.database.DbSelectedDataObjects;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Context object with restricted access to chimera functions
 */
public class ChimeraContext implements IChimeraContext {
    private ActivityInstance activityInstance;
    private Logger logger = Logger.getLogger(ChimeraContext.class);
    private List<DataObject> dataObjects = new ArrayList<>();

    /**
     * Initialize the ChimeraContext
     *
     * @param activityInstance  The activity instance of the script task.
     */
    public ChimeraContext(ActivityInstance activityInstance) {
        this.activityInstance = activityInstance;

        updateDataObjects();
    }

    /**
     * Updates the dataObjects object to get the newest values of the data objects
     */
    private void updateDataObjects() {
        DbSelectedDataObjects dbDataObjectSelection = new DbSelectedDataObjects();
        List<Integer> dataIds = dbDataObjectSelection.getDataObjectSelection(getScenarioId(), getActivityId());

        DataManager dataManager = new DataManager(activityInstance.getScenarioInstance());
        dataObjects.clear();
        for(int id : dataIds) {
            dataObjects.add(dataManager.getDataObjectForId(id).get());
        }
    }

    /**
     * Writes a message to the log file.
     *
     * @param message   The message to log.
     */
    @Override
    public void log(String message) {
        logger.info(message);
    }

    /**
     * Returns the value of a given input data object of the activity.
     *
     * @param dataObjectName    The name of the data object to get the value from.
     * @param attributeName     The name of the attribute to get.
     * @return an object with the value of the attribute.
     */
    public Object getParam(String dataObjectName, String attributeName) {
        // TODO rewrite... Update value in 'dataObjects' after change
        for(DataObject dataObject : dataObjects) {
            if (dataObject.getName().equals(dataObjectName)) {
                for (DataAttributeInstance dataAttributeInstance : dataObject.getDataAttributeInstances()) {
                    if (dataAttributeInstance.getName().equals(attributeName)) {
                        return dataAttributeInstance.getValue();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Sets the value of a given input data object of the activity.
     *
     * @param dataObjectName    The name of the incoming data object.
     * @param attributeName     The name of the attribute to change.
     * @param value             The value to set to in the data object.
     */
    public void setParam(String dataObjectName, String attributeName, String value) {
        for(DataObject dataObject : dataObjects) {
            if(dataObject.getName().equals(dataObjectName)) {
                for(DataAttributeInstance dataAttributeInstance : dataObject.getDataAttributeInstances()) {
                    if(dataAttributeInstance.getName().equals(attributeName)) {
                        Map<Integer, String> map = new HashMap<>();
                        map.put(dataAttributeInstance.getId(), value);

                        DataManager dataManager = new DataManager(activityInstance.getScenarioInstance());
                        dataManager.setAttributeValues(getActivityId(), map);

                        updateDataObjects();
                        return;
                    }
                }
            }
        }
    }

    /**
     * Returns the id of the current working scenario.
     *
     * @return an int with the current scenario id.
     */
    public int getScenarioId() {
        return activityInstance.getScenarioInstance().getId();
    }

    /**
     * Returns the id of the current activity.
     *
     * @return an int with the current activity id.
     */
    public int getActivityId() {
        return activityInstance.getControlNodeInstanceId();
    }

    /**
     * Returns the id of the current executing fragment.
     *
     * @return an int with the current fragment id.
     */
    public int getFragmentId() {
        return activityInstance.getFragmentInstanceId();
    }

}