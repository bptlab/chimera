package de.uni_potsdam.hpi.bpt.bp2014.jcore;


/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

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
