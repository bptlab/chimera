package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbReference;

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



/*
represents the activity instance, it save the state of the activity in the statemachine, it has an outgoing behavior and
an incoming behavior
the constructor looks for an activity instance in the database or create a new one in the database
 */

public class ActivityInstance extends ControlNodeInstance {
    private TaskExecutionBehavior taskExecutionBehavior;
    private ScenarioInstance scenarioInstance;
    private String label;
    private LinkedList<Integer> references;
    private boolean isMailTask;
    //Database Connection objects
    private DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
    private DbActivityInstance dbActivityInstance = new DbActivityInstance();
    private DbControlNode dbControlNode = new DbControlNode();
    private DbReference dbReference = new DbReference();


    public ActivityInstance(int controlNode_id, int fragmentInstance_id, ScenarioInstance scenarioInstance) {
        this.scenarioInstance = scenarioInstance;
        this.controlNode_id = controlNode_id;
        this.fragmentInstance_id = fragmentInstance_id;
        this.label = dbControlNode.getLabel(controlNode_id);
        this.references = dbReference.getReferenceActivitiesForActivity(controlNode_id);
        scenarioInstance.getControlNodeInstances().add(this);
        if (dbControlNodeInstance.existControlNodeInstance(controlNode_id, fragmentInstance_id)) {
            //creates an existing Activity Instance using the database information
            controlNodeInstance_id = dbControlNodeInstance.getControlNodeInstanceID(controlNode_id, fragmentInstance_id);
            this.stateMachine = new ActivityStateMachine(controlNodeInstance_id, scenarioInstance, this);
        } else {
            //creates a new Activity Instance also in database
            this.controlNodeInstance_id = dbControlNodeInstance.createNewControlNodeInstance(controlNode_id, "Activity", fragmentInstance_id);
            dbActivityInstance.createNewActivityInstance(controlNodeInstance_id, "HumanTask", "init");
            this.stateMachine = new ActivityStateMachine(controlNodeInstance_id, scenarioInstance, this);
            ((ActivityStateMachine) stateMachine).enableControlFlow();
        }
        if (dbControlNode.getType(controlNode_id).equals("EmailTask")) {
            this.taskExecutionBehavior = new EmailTaskExecutionBehavior(controlNodeInstance_id, scenarioInstance, this);
            this.isMailTask = true;
        } else {
            this.taskExecutionBehavior = new HumanTaskExecutionBehavior(controlNodeInstance_id, scenarioInstance, this);
            this.isMailTask = false;
        }
        this.incomingBehavior = new TaskIncomingControlFlowBehavior(this, scenarioInstance, stateMachine);
        this.outgoingBehavior = new TaskOutgoingControlFlowBehavior(controlNode_id, scenarioInstance, fragmentInstance_id);
    }

    public boolean begin() {
        if (((ActivityStateMachine) stateMachine).isEnabled()) {
            ((ActivityStateMachine) stateMachine).begin();
            ((TaskIncomingControlFlowBehavior) incomingBehavior).startReferences();
            ((TaskIncomingControlFlowBehavior) incomingBehavior).setDataObjectInstancesOnChange();
            scenarioInstance.checkDataFlowEnabled();
            taskExecutionBehavior.execute();
            System.out.println("Start Activity " + controlNode_id);
            return true;
        } else {
            return false;
        }
    }

    public boolean referenceStarted() {
        return ((ActivityStateMachine) stateMachine).referenceStarted();
    }

    public boolean terminate() {
        boolean workingFine = ((ActivityStateMachine) stateMachine).terminate();
        ((TaskOutgoingControlFlowBehavior) outgoingBehavior).terminate();
        return workingFine;
    }

    //checks if the Activity is now data enabled
    public void checkDataFlowEnabled() {
        ((TaskIncomingControlFlowBehavior) incomingBehavior).checkDataFlowEnabled();
    }


    public TaskExecutionBehavior getTaskExecutionBehavior() {
        return taskExecutionBehavior;
    }

    public ScenarioInstance getScenarioInstance() {
        return scenarioInstance;
    }

    public String getLabel() {
        return label;
    }

    public LinkedList<Integer> getReferences() {
        return references;
    }

    public boolean getIsMailTask() {
        return isMailTask;
    }

}
