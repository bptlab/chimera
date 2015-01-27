package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;

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



/*
represents a fragment instance
the constructor looks for an fragment instance in the database or create a new one in the database
the constructor also initialize the control nodes
 */
public class FragmentInstance {
    private ScenarioInstance scenarioInstance;
    public int fragment_id;
    public int fragmentInstance_id;
    private int scenarioInstance_id;
    private DbFragmentInstance dbFragmentInstance = new DbFragmentInstance();
    private DbControlNode dbControlNode = new DbControlNode();
    private DbControlFlow dbControlFlow = new DbControlFlow();
    private DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();

    public FragmentInstance(int fragment_id, int scenarioInstance_id, ScenarioInstance scenarioInstance){
        this.scenarioInstance = scenarioInstance;
        this.fragment_id = fragment_id;
        this.scenarioInstance_id = scenarioInstance_id;
        if (dbFragmentInstance.existFragment(fragment_id, scenarioInstance_id)){
            //creates an existing Fragment Instance using the database information
            fragmentInstance_id = dbFragmentInstance.getFragmentInstanceID(fragment_id, scenarioInstance_id);
            this.initializeExistingNodeInstanceForFragment();
        }else {
            //creates a new Fragment Instance also in database
            this.fragmentInstance_id = dbFragmentInstance.createNewFragmentInstance(fragment_id, scenarioInstance_id);
            this.initializeNewNodeInstanceForFragment();
        }
    }
    private void initializeExistingNodeInstanceForFragment(){
        //initializes all Activity Instances in the database
        LinkedList<Integer> activities = dbControlNodeInstance.getActivitiesForFragmentInstanceID(fragmentInstance_id);
        for(int activity: activities) {
            ActivityInstance activityInstance = new ActivityInstance(activity, fragmentInstance_id, scenarioInstance);
        }
        //initializes all Gateway Instances in the database
        LinkedList<Integer> gateways = dbControlNodeInstance.getGatewaysForFragmentInstanceID(fragmentInstance_id);
        for(int gateway: gateways) {
            GatewayInstance gatewayInstance = new GatewayInstance(gateway, fragmentInstance_id, scenarioInstance);
        }
    }
    private void initializeNewNodeInstanceForFragment(){
        //gets the Start Event and then the following Control Node to initialize it
        int startEvent = dbControlNode.getStartEventID(fragment_id);
        int controlNode = dbControlFlow.getNextControlNodeAfterStartEvent(startEvent);
        String controlNodeType = dbControlNode.getType(controlNode);
        if (controlNodeType.equals("Activity")){
            ActivityInstance activityInstance = new ActivityInstance(controlNode, fragmentInstance_id, scenarioInstance);
            activityInstance.incomingBehavior.enableControlFlow();
        }else if(controlNodeType.equals("AND") || controlNodeType.equals("XOR")){
            GatewayInstance gatewayInstance = new GatewayInstance(controlNode, fragmentInstance_id, scenarioInstance);
            gatewayInstance.incomingBehavior.enableControlFlow();
        }
    }

    //sets a Fragment Instances as terminated in the database
    public void terminate(){
        dbFragmentInstance.terminateFragmentInstance(fragmentInstance_id);
    }

}
