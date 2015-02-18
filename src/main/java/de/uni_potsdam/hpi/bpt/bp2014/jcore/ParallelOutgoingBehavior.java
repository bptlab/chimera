package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import java.util.LinkedList;

/**
 * Created by jaspar.mang on 17.02.15.
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
    private ControlNodeInstance createFollowingNodeInstance(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : scenarioInstance.getControlNodeInstances()) {
            if (controlNode_id == controlNodeInstance.controlNode_id) {
                return controlNodeInstance;
            }
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
}
