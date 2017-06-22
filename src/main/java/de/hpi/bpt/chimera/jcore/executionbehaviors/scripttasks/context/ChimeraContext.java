package de.hpi.bpt.chimera.jcore.executionbehaviors.scripttasks.context;

import com.sun.xml.bind.v2.schemagen.xmlschema.List;

import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;

/**
 * Provide 
 */
public class ChimeraContext implements IChimeraContext {
    private ActivityInstance activityInstance;

    public void ChimeraContext(ActivityInstance activityInstance) {
        this.activityInstance = activityInstance;
    }

    public Object getParam(String name) {
        return null;
    }
    public void setParam(String name, Object value) {

    }
    public int getScenarioId() {
        return activityInstance.getScenarioInstance().getId();
    }

    /**
     * Return the id of the activity itself.
     */
    public int getActivityId() {
        return activityInstance.getControlNodeId();
    }
    /**
     * Return the id of the fragment which owns this service task. 
     */
    public int getFragmentId() {
        return activityInstance.getFragmentInstanceId();
    }

    public List<Object> getDataObjects() {
        return activityInstance.getScenarioInstance().getDataManager().getDataObjectForId(0);
    }
}