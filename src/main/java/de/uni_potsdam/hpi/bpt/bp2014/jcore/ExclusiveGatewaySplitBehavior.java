package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

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
    /**
     * List of IDs of following control nodes.
     */
    private LinkedList<LinkedList<Integer>> followingControlNodes = new LinkedList<LinkedList<Integer>>();

    /**
     * Initializes and creates an ExclusiveGatewaySplitBehavior
     *
     * @param gateway_id          The id of the gateway.
     * @param scenarioInstance    An instance from the class ScenarioInstance.
     * @param fragmentInstance_id The id of the fragment instance.
     */
    public ExclusiveGatewaySplitBehavior(int gateway_id, ScenarioInstance scenarioInstance, int fragmentInstance_id) {
        this.controlNode_id = gateway_id;
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        initializeFollowingControlNodeIds();
    }

    /**
     * Adds the ids of the following control nodes to the list.
     * Creates for every control node a bucket.
     */
    private void initializeFollowingControlNodeIds() {
        LinkedList<Integer> ids = this.dbControlFlow.getFollowingControlNodes(controlNode_id);
        for (int i = 0; i < ids.size(); i++) {
            followingControlNodes.add(new LinkedList<Integer>());
            this.addFollowingControlNode(i, ids.get(i));
        }
    }

    /**
     * Adds the control node id to the bucket. Looks for XOR and AND to add the following control nodes.
     *
     * @param bucket_id The bucket id.
     * @param id        The id of the control node getting added.
     */
    private void addFollowingControlNode(int bucket_id, int id) {
        LinkedList<Integer> ids = followingControlNodes.get(bucket_id);
        ids.add(id);
        followingControlNodes.set(bucket_id, ids);
        if (dbControlNode.getType(id).equals("XOR") || dbControlNode.getType(id).equals("AND")) {
            for (int controlNode_id : dbControlFlow.getFollowingControlNodes(id)) {
                this.addFollowingControlNode(bucket_id, controlNode_id);
            }
        }
    }

    @Override
    public void terminate() {
        this.checkAfterTermination();
        this.runAfterTermination();
    }

    /**
     * Executes the XOR gateway and enable the following control nodes.
     */
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
                ((ActivityInstance) controlNodeInstance).setAutomaticExecution(false);
                break;
            case "Endevent":
                controlNodeInstance = new EventInstance(fragmentInstance_id, scenarioInstance, "Endevent");
                break;
            case "XOR":
                controlNodeInstance = new GatewayInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
                break;
            case "AND":
                controlNodeInstance = new GatewayInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
                ((GatewayInstance) controlNodeInstance).setAutomaticExecution(false);
                break;
        }
        return controlNodeInstance;
    }

    /**
     * Checks if the gateway can terminate, because the given control node has changed his state.
     *
     * @param controlNode_id The id of the control node.
     * @return True if the gateway can terminate. false if not.
     */
    public boolean checkTermination(int controlNode_id) {
        if ((dbControlNode.getType(controlNode_id).equals("AND")) || (dbControlNode.getType(controlNode_id).equals("XOR"))) {
            return false;
        }
        for (int i = 0; i < followingControlNodes.size(); i++) {
            if (followingControlNodes.get(i).contains(new Integer(controlNode_id))) {
                followingControlNodes.remove(i);
                for (LinkedList<Integer> followingControlNode_ids : followingControlNodes) {
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

    /**
     * Evaluates Conditions for the control flow.
     */
    public void evaluateConditions() {
        Map<Integer, String> conditions = dbControlFlow.getConditions(controlNode_id);
        Set keys = conditions.keySet();
        Iterator key = keys.iterator();
        Integer controlNode_id = 0;
        while(key.hasNext()){
            controlNode_id = (Integer)key.next();
            if(evaluateCondition(conditions.get(controlNode_id))){
                break;
            }
        }
        ControlNodeInstance controlNodeInstance = createFollowingNodeInstance(controlNode_id);
        controlNodeInstance.getIncomingBehavior().enableControlFlow();

        //TODO: Conditions holen
        //TODO: Auswerten
        //TODO: richtige Nachfolger enablen
    }

    private boolean evaluateCondition(String condition){
        String dataObjectName = getDataObjectName(condition);
        String dataAttributeName = getDataAttributeName(condition);

        if(condition.contains("=")){

        }else if(condition.contains("<")){

        }else if(condition.contains(">")){

        }

        return true;
    }

    private String getDataObjectName(String condition){
        System.out.println(condition.split(".")[0]);
        return condition.split(".")[0];
    }

    private String getDataAttributeName(String condition){
        String name = condition.split(".")[1];
        if(condition.contains("=")){
            name = name.split("=")[1];
        }else if(condition.contains("<")){
            name = name.split("<")[1];
        }else if(condition.contains(">")){
            name = name.split(">")[1];
        }
        name.replace(" ", "");
        System.out.println(name);
        return name;
    }
}
