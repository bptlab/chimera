package de.uni_potsdam.hpi.bpt.bp2014.jcore;

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
public abstract class ParallelOutgoingBehavior extends OutgoingBehavior {
    /**
     * Set all following control nodes to control flow enabled and initializes them.
     */
    public void enableFollowing() {
        LinkedList<Integer> followingControlNode_ids = this.dbControlFlow.getFollowingControlNodes(controlNode_id);
        for (int followingControlNode_id : followingControlNode_ids) {
            ControlNodeInstance followingControlNodeInstance = createFollowingNodeInstance(followingControlNode_id);
            //enable following instances
            followingControlNodeInstance.incomingBehavior.enableControlFlow();
        }
    }

    /**
     * Initializes and creates the following control node instance for the given control node instance id.
     *
     * @param controlNode_id This is the database id from the control node instance.
     * @return the created control node instance.
     */
    protected ControlNodeInstance createFollowingNodeInstance(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : scenarioInstance.getControlNodeInstances()) {
            if (controlNode_id == controlNodeInstance.controlNode_id && !controlNodeInstance.getClass().equals(ActivityInstance.class) && !controlNodeInstance.getStateMachine().state.equals("terminated")) {
                return controlNodeInstance;
            }
        }
        String type = dbControlNode.getType(controlNode_id);
        return createControlNode(type, controlNode_id);
    }
}
