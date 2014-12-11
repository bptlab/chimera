package de.uni_potsdam.hpi.bpt.bp2014.jengine;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;

/**
 * Created by jaspar.mang on 24.11.14.
 */
public class ActivityInstance extends ControlNodeInstance {
    public TaskExecutionBehavior taskExecutionBehavior;
    public ScenarioInstance scenarioInstance;
    public DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
    public DbActivityInstance dbActivityInstance = new DbActivityInstance();

    public ActivityInstance(int controlNode_id, int fragmentInstance_id, ScenarioInstance scenarioInstance){
        this.scenarioInstance = scenarioInstance;
        this.controlNode_id = controlNode_id;
        this.fragmentInstance_id = fragmentInstance_id;
        scenarioInstance.controlNodeInstances.add(this);
        if(dbControlNodeInstance.existControlNodeInstance(controlNode_id, fragmentInstance_id)){
            System.out.println("Activity exist");
            //TODO: Activity already exists
        }else {
            dbControlNodeInstance.createNewControlNodeInstance(controlNode_id, "Activity", fragmentInstance_id);
            controlNodeInstance_id = dbControlNodeInstance.getControlNodeInstanceID(controlNode_id, fragmentInstance_id);
            dbActivityInstance.createNewActivityInstance(controlNodeInstance_id, "HumanTask", "init");
            this.stateMachine = new ActivityStateMachine(controlNodeInstance_id, scenarioInstance, this);
            ((ActivityStateMachine)stateMachine).enableControlFlow();
            this.taskExecutionBehavior = new HumanTaskExecutionBehavior(controlNodeInstance_id, scenarioInstance);
            this.incomingBehavior = new TaskIncomingControlFlowBehavior(this, scenarioInstance, stateMachine);
            this.outgoingBehavior = new TaskOutgoingControlFlowBehavior(controlNode_id, scenarioInstance, fragmentInstance_id);
        }
    }

    public Boolean begin(){
        return ((ActivityStateMachine) stateMachine).begin();
    }
    public Boolean terminate(){
        ((TaskOutgoingControlFlowBehavior) outgoingBehavior).enableFollowing();
        return ((ActivityStateMachine) stateMachine).terminate();
    }
}
