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
    private LinkedList<LinkedList<Integer>> followingControlNodes = new LinkedList<LinkedList<Integer>>();

    public ExclusiveGatewaySplitBehavior(int gateway_id, ScenarioInstance scenarioInstance, int fragmentInstance_id) {
        this.controlNode_id = gateway_id;
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        //followingControlNode_ids = this.dbControlFlow.getFollowingControlNodes(controlNode_id);
        initializeFollowingControlNodeIds();
    }

    private void initializeFollowingControlNodeIds(){
        LinkedList<Integer> ids = this.dbControlFlow.getFollowingControlNodes(controlNode_id);
        for (int i = 0; i < ids.size(); i++){
            followingControlNodes.add(new LinkedList<Integer>());
            this.addFollowingControlNode(i, ids.get(i));
        }
    }
    private void addFollowingControlNode(int bucket_id, int id){
        LinkedList<Integer> ids = followingControlNodes.get(bucket_id);
        ids.add(id);
        followingControlNodes.set(bucket_id, ids);
        if(dbControlNode.getType(id).equals("XOR") || dbControlNode.getType(id).equals("AND")){
            for(int controlNode_id: dbControlFlow.getFollowingControlNodes(id)){
                this.addFollowingControlNode(bucket_id, controlNode_id);
            }
        }
    }

    @Override
    public void terminate() {
        this.checkAfterTermination();
        this.runAfterTermination();
    }

    public void execute() {
        enableFollowing();
    }

    @Override
    protected ControlNodeInstance createFollowingNodeInstance(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : scenarioInstance.getControlNodeInstances()) {
            if (controlNode_id == controlNodeInstance.controlNode_id) {
                return controlNodeInstance;
            }
        }
        String type = dbControlNode.getType(controlNode_id);
        ControlNodeInstance controlNodeInstance = null;
        //TODO type
        switch (type) {
            case "Activity":
            case "EmailTask":
                controlNodeInstance = new ActivityInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
                ((ActivityInstance)controlNodeInstance).setAutomaticExecution(false);
                break;
            case "Endevent":
                controlNodeInstance = new EventInstance(fragmentInstance_id, scenarioInstance, "Endevent");
                break;
            case "XOR":
                controlNodeInstance = new GatewayInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
                break;
            case "AND":
                controlNodeInstance = new GatewayInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
                ((GatewayInstance)controlNodeInstance).setAutomaticExecution(false);
                break;
        }
        return controlNodeInstance;
    }

    public boolean checkTermination(int controlNode_id){
        if((dbControlNode.getType(controlNode_id).equals("AND")) || (dbControlNode.getType(controlNode_id).equals("XOR"))){
            return false;
        }
        for(int i = 0; i < followingControlNodes.size(); i++){
            if(followingControlNodes.get(i).contains(new Integer(controlNode_id))){
                followingControlNodes.remove(i);
                for(LinkedList<Integer> followingControlNode_ids: followingControlNodes){
                    for (int id : followingControlNode_ids) {
                        ControlNodeInstance controlNodeInstance = scenarioInstance.getControlNodeInstanceForControlNodeId(id);
                        controlNodeInstance.skip();
                    }
                }
                return true;
            }
        }
        return false;
    }

}
