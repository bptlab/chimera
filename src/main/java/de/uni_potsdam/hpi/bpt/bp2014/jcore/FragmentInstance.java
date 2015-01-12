package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragmentInstance;

import java.util.LinkedList;
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
            fragmentInstance_id = dbFragmentInstance.getFragmentInstanceID(fragment_id, scenarioInstance_id);
            this.initializeExistingNodeInstanceForFragment();
            System.out.println("Fragment exist, ScenarioInstanceID: "+scenarioInstance_id);
        }else {
            dbFragmentInstance.createNewFragmentInstance(fragment_id, scenarioInstance_id);
            fragmentInstance_id = dbFragmentInstance.getFragmentInstanceID(fragment_id, scenarioInstance_id);
            this.initializeNodeInstanceForFragment();
            System.out.println("Fragment not exist");
        }
    }
    private void initializeExistingNodeInstanceForFragment(){
        LinkedList<Integer> activities = dbControlNodeInstance.getActivitiesForFragmentInstanceID(fragmentInstance_id);
        for(int activity: activities) {
            ActivityInstance activityInstance = new ActivityInstance(activity, fragmentInstance_id, scenarioInstance);
        }
    }
    private void initializeNodeInstanceForFragment(){
        int startEvent = dbControlNode.getStartEventID(fragment_id);
        int controlNode = dbControlFlow.getNextControlNodeAfterStartEvent(startEvent);
        String controlNodeType = dbControlNode.getType(controlNode);
        if (controlNodeType.equals("Activity")){
            ActivityInstance activityInstance = new ActivityInstance(controlNode, fragmentInstance_id, scenarioInstance);
            ((TaskIncomingControlFlowBehavior)activityInstance.incomingBehavior).enableControlFlow();
        }
        //TODO: insert Gateways HERE!
    }
    public void terminate(){
        dbFragmentInstance.terminateFragmentInstance(fragmentInstance_id);
    }

}
