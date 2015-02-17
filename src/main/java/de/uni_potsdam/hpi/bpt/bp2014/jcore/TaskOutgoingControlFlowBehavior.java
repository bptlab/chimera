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
public class TaskOutgoingControlFlowBehavior extends OutgoingBehavior {
    private ControlNodeInstance controlNodeInstance;
    /**
     * Database Connection objects
     */
    private DbDataNode dbDataNode = new DbDataNode();
    private DbDataFlow dbDataFlow = new DbDataFlow();

    /**
     * Initializes the TaskOutgoingControlFlowBehavior
     * @param activity_id This is the database id from the activity instance.
     * @param scenarioInstance This is an instance from the class ScenarioInstance.
     * @param fragmentInstance_id This is the database id from the fragment instance.
     * @param controlNodeInstance This is an instance from the class ControlNodeInstance.
     */
    public TaskOutgoingControlFlowBehavior(int activity_id, ScenarioInstance scenarioInstance, int fragmentInstance_id, ControlNodeInstance controlNodeInstance) {
        this.controlNode_id = activity_id;
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        this.controlNodeInstance = controlNodeInstance;
    }

    /**
     * Terminates the control node instance.
     */
    @Override
    public void terminate() {
        setDataStates();
        scenarioInstance.checkDataFlowEnabled();
        this.enableFollowing();
    }

    /**
     * Set all following control nodes to control flow enabled and initializes them.
     */
    public void enableFollowing() {
        LinkedList<Integer> followingControlNode_ids = this.dbControlFlow.getFollowingControlNodes(controlNode_id);
        for (int followingControlNode_id : followingControlNode_ids) {
            ControlNodeInstance followingControlNodeInstance = createFollowingNodeInstance(followingControlNode_id);
            followingControlNodeInstance.incomingBehavior.enableControlFlow();
        }
    }

    /**
     * Initializes and creates the following control node instance for the given control node instance id.
     * @param controlNode_id This is the database id from the control node instance.
     * @return the created control node instance.
     */
    private ControlNodeInstance createFollowingNodeInstance(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : scenarioInstance.getControlNodeInstances()) {
            if (controlNode_id == controlNodeInstance.controlNode_id) return controlNodeInstance;
        }
        String type = dbControlNode.getType(controlNode_id);
        ControlNodeInstance controlNodeInstance = null;
        //TODO type
        if (type.equals("Activity") || type.equals("EmailTask")) {
            controlNodeInstance = new ActivityInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
        } else if (type.equals("Endevent")) {
            controlNodeInstance = new EventInstance(fragmentInstance_id, scenarioInstance, "Endevent");
        } else if (type.equals("XOR") || type.equals("AND")) {
            controlNodeInstance = new GatewayInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
        }
        return controlNodeInstance;
    }

    /**
     * Sets the states of the data object to the output states of the activity.
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
