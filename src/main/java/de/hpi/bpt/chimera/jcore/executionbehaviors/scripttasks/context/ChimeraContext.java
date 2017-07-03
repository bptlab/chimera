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
 * Provide 
 */
public class ChimeraContext implements IChimeraContext {
    private ActivityInstance activityInstance;
    private Logger logger = Logger.getLogger(ChimeraContext.class);
    private List<DataObject> dataObjects = new ArrayList<>();

    public ChimeraContext(ActivityInstance activityInstance) {
        this.activityInstance = activityInstance;

        DbSelectedDataObjects dbDataObjectSelection = new DbSelectedDataObjects();
        List<Integer> dataIds = dbDataObjectSelection.getDataObjectSelection(getScenarioId(), getActivityId());

        DataManager dataManager = new DataManager(activityInstance.getScenarioInstance());

        for(int id : dataIds) {
            dataObjects.add(dataManager.getDataObjectForId(id).get());
        }
    }

    @Override
    public void log(String message) {
        logger.info(message);
    }

    public Object getParam(String dataObjectName, String attributeName) {
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

    public void setParam(String dataObjectName, String attributeName, String value) {
        // TODO Geht die value nur als String?
        for(DataObject dataObject : dataObjects) {
            if(dataObject.getName().equals(dataObjectName)) {
                for(DataAttributeInstance dataAttributeInstance : dataObject.getDataAttributeInstances()) {
                    if(dataAttributeInstance.getName().equals(attributeName)) {
                        Map<Integer, String> map = new HashMap<>();
                        map.put(dataAttributeInstance.getId(), value);

                        DataManager dataManager = new DataManager(activityInstance.getScenarioInstance());
                        dataManager.setAttributeValues(getActivityId(), map);
                        return;
                    }
                }
            }
        }
    }

    public int getScenarioId() {
        return activityInstance.getScenarioInstance().getId();
    }

    /**
     * Return the id of the activity itself.
     */
    public int getActivityId() {
        return activityInstance.getControlNodeInstanceId();
    }
    /**
     * Return the id of the fragment which owns this service task. 
     */
    public int getFragmentId() {
        return activityInstance.getFragmentInstanceId();
    }

    /*public List<Object> getDataObjects() {
        return activityInstance.getScenarioInstance().getDataManager().getDataObjectForId(0);
    }*/
}