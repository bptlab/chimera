package de.uni_potsdam.hpi.bpt.bp2014.jengine;

import java.util.LinkedList;

/**
 * Created by jaspar.mang on 24.11.14.
 */
public class TaskOutgoingControlFlowBehavior extends OutgoingBehavior{

    public TaskOutgoingControlFlowBehavior(int activity_id, ScenarioInstance scenarioInstance, int fragmentInstance_id){
        this.controlNode_id = activity_id;
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
    }

    public void enableFollowing(){
        LinkedList<Integer> followingControlNode_ids = this.dbControlFlow.getFollowingControlNodes(controlNode_id);
        for(int followingControlNode_id: followingControlNode_ids){
            ControlNodeInstance followingControlNodeInstance = getFollowingNodeInstance(followingControlNode_id);
            if(followingControlNodeInstance instanceof ActivityInstance) {
                ((TaskIncomingControlFlowBehavior) followingControlNodeInstance.incomingBehavior).enableControlFlow();
            }else if(followingControlNodeInstance instanceof EventInstance){
                ((EventInstance) followingControlNodeInstance).doEndevent();
            }
            //TODO: Instance of decision Gateway
        }
    }

    private ControlNodeInstance getFollowingNodeInstance(int controlNode_id){
        for(ControlNodeInstance controlNodeInstance : scenarioInstance.controlNodeInstances){
            if(controlNode_id == controlNodeInstance.controlNode_id) return controlNodeInstance;
        }
        String type = dbControlNode.getType(controlNode_id);
        ControlNodeInstance controlNodeInstance = null;
        if(type.equals("Activity")){
            controlNodeInstance = new ActivityInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
        }else if(type.equals("Endevent")){
            controlNodeInstance = new EventInstance(fragmentInstance_id, scenarioInstance);
        }
        //TODO: Gateways
        return controlNodeInstance;
    }
}
