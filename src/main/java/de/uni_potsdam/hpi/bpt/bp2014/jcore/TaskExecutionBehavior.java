package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import java.util.Map;

/**
 * Performs the execution for one activity.
 */
public class TaskExecutionBehavior {
    protected final ScenarioInstance scenarioInstance;
    protected final int activityInstance_id;
    protected final ControlNodeInstance controlNodeInstance;

    /**
     * Initializes.
     *
     * @param activityInstance_id Id from the activity instance in the database.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param controlNodeInstance This is an instance from the class ControlNodeInstance.
     */
    public TaskExecutionBehavior(int activityInstance_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance) {
        this.activityInstance_id = activityInstance_id;
        this.scenarioInstance = scenarioInstance;
        this.controlNodeInstance = controlNodeInstance;
    }

    /**
     * Executes the behavior of the activity.
     */
    public void execute() {
    }

    /**
     * @param canTerminate
     */
    protected void setCanTerminate(boolean canTerminate) {
        ((ActivityInstance) controlNodeInstance).setCanTerminate(canTerminate);
    }

    /**
     * @param values
     */
    public void setDataAttributeValues(Map<Integer, String> values) {
    }


}
