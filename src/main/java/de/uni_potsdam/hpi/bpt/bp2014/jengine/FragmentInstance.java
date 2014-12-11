package de.uni_potsdam.hpi.bpt.bp2014.jengine;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragmentInstance;

import java.util.LinkedList;

/**
 * Created by jaspar.mang on 24.11.14.
 */
public class FragmentInstance {
    private ScenarioInstance scenarioInstance;
    public int fragment_id;
    public int fragmentInstance_id;
    private int scenarioInstance_id;
    private DbFragmentInstance dbFragmentInstance = new DbFragmentInstance();
    private DbControlNode dbControlNode = new DbControlNode();
    private DbControlFlow dbControlFlow = new DbControlFlow();

    public FragmentInstance(int fragment_id, int scenarioInstance_id, ScenarioInstance scenarioInstance){
        this.scenarioInstance = scenarioInstance;
        this.fragment_id = fragment_id;
        this.scenarioInstance_id = scenarioInstance_id;
        if (dbFragmentInstance.existFragment(fragment_id, scenarioInstance_id)){
            fragmentInstance_id = dbFragmentInstance.getFragmentInstanceID(fragment_id, scenarioInstance_id);
        }else {
            dbFragmentInstance.createNewFragmentInstance(fragment_id, scenarioInstance_id);
            fragmentInstance_id = dbFragmentInstance.getFragmentInstanceID(fragment_id, scenarioInstance_id);
            this.initializeNodeInstanceForFragment();
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
