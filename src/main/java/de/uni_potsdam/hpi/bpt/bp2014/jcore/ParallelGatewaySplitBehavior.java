package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;

import java.util.LinkedList;


/***********************************************************************************
*   
*   _________ _______  _        _______ _________ _        _______ 
*   \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
*      )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
*      |  |  | (__    |   \ | || |         | |   |   \ | || (__    
*      |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)   
*      |  |  | (      | | \   || | \_  )   | |   | | \   || (      
*   |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
*   (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
*
*******************************************************************
*
*   Copyright © All Rights Reserved 2014 - 2015
*
*   Please be aware of the License. You may found it in the root directory.
*
************************************************************************************/


public class ParallelGatewaySplitBehavior extends OutgoingBehavior {
    //Database Connection objects
    private DbDataFlow dbDataFlow = new DbDataFlow();

    ParallelGatewaySplitBehavior(int gateway_id, ScenarioInstance scenarioInstance, int fragmentInstance_id){
        this.controlNode_id = gateway_id;
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
    }
    @Override
    public void terminate(){
        scenarioInstance.checkDataFlowEnabled();
        this.enableFollowing();
    }

    public void enableFollowing(){
        LinkedList<Integer> followingControlNode_ids = this.dbControlFlow.getFollowingControlNodes(controlNode_id);
        for(int followingControlNode_id: followingControlNode_ids){
            ControlNodeInstance followingControlNodeInstance = getFollowingNodeInstance(followingControlNode_id);
            //enable following instances
            followingControlNodeInstance.incomingBehavior.enableControlFlow();
        }
    }
    //get the following control node instance, also initialize them
    private ControlNodeInstance getFollowingNodeInstance(int controlNode_id){
        for(ControlNodeInstance controlNodeInstance : scenarioInstance.controlNodeInstances){
            if(controlNode_id == controlNodeInstance.controlNode_id) return controlNodeInstance;
        }
        String type = dbControlNode.getType(controlNode_id);
        ControlNodeInstance controlNodeInstance = null;
        //TODO type
        if(type.equals("Activity") || type.equals("EmailTask")){
            controlNodeInstance = new ActivityInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
        }else if(type.equals("Endevent")){
            controlNodeInstance = new EventInstance(fragmentInstance_id, scenarioInstance, "Endevent");
        }else if(type.equals("XOR") || type.equals("AND")){
            controlNodeInstance = new GatewayInstance(controlNode_id, fragmentInstance_id, scenarioInstance);
        }
        return controlNodeInstance;    }
}
