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


public class ExclusiveGatewaySplitBehavior extends ParallelOutgoingBehavior {
    private  LinkedList<Integer> followingControlNode_ids;

    public ExclusiveGatewaySplitBehavior(int gateway_id, ScenarioInstance scenarioInstance, int fragmentInstance_id) {
        this.controlNode_id = gateway_id;
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        followingControlNode_ids = this.dbControlFlow.getFollowingControlNodes(controlNode_id);
    }

    @Override
    public void terminate() {
        this.checkAfterTermination();
        this.runAfterTermination();
    }

    public void execute() {
        enableFollowing();
    }

    public boolean checkTermination(int controlNode_id){
        if(followingControlNode_ids.contains(new Integer(controlNode_id))){
            followingControlNode_ids.remove(new Integer(controlNode_id));
            for (int id : followingControlNode_ids) {
                ControlNodeInstance controlNodeInstance = scenarioInstance.getControlNodeInstanceForControlNodeId(id);
                if(controlNodeInstance.getClass() == ActivityInstance.class) {
                    ((ActivityInstance)controlNodeInstance).skip();
                }
            }
            return true;
        }
        return false;
    }

}
