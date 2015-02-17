package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;

import java.util.LinkedList;

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


/**
 * Handles the behavior of a terminating activity instance.
 */
public class TaskOutgoingControlFlowBehavior extends ParallelOutgoingBehavior {
    private ControlNodeInstance controlNodeInstance;
    /**
     * Database Connection objects
     */
    private DbDataNode dbDataNode = new DbDataNode();
    private DbDataFlow dbDataFlow = new DbDataFlow();

    /**
     * Initializes the TaskOutgoingControlFlowBehavior
     *
     * @param activity_id         This is the database id from the activity instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param fragmentInstance_id This is the database id from the fragment instance.
     * @param controlNodeInstance This is an instance from the class ControlNodeInstance.
     */
    public TaskOutgoingControlFlowBehavior(int activity_id, ScenarioInstance scenarioInstance, int fragmentInstance_id, ControlNodeInstance controlNodeInstance) {
        this.controlNode_id = activity_id;
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        this.controlNodeInstance = controlNodeInstance;
    }

    @Override
    public void terminate() {
        setDataStates();
        scenarioInstance.checkDataFlowEnabled();
        this.enableFollowing();
    }

    /**
     * Sets the states of the data object to the output states of the activity.
     * Sets all this data object to not on change.
     */
    private void setDataStates() {
        LinkedList<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(controlNode_id);
        //TODO: Output Set
        for (int outputSet : outputSets) {
            LinkedList<Integer> dataObjects = dbDataNode.getDataObjectIdsForDataSets(outputSet);
            LinkedList<Integer> states = dbDataNode.getDataStatesForDataSets(outputSet);
            for (int i = 0; i < dataObjects.size(); i++) {
                //resets DataObjectInstance from OnChange back to not OnChange
                scenarioInstance.setDataObjectInstanceToNotOnChange(dataObjects.get(i));
                scenarioInstance.changeDataObjectInstanceState(dataObjects.get(i), states.get(i));
            }
        }
    }

    /**
     * Terminates all referential running activities.
     */
    public void terminateReferences() {
        for (int activity_id : ((ActivityInstance) controlNodeInstance).getReferences()) {
            scenarioInstance.terminateReferenceControlNodeInstanceForControlNodeInstanceID(activity_id);
        }
    }
}
