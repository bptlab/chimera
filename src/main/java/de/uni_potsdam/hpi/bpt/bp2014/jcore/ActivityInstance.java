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


/**
 * Represents the activity instance.
 * It save the state of the activity in the state machine. It has an outgoing behavior and an incoming behavior.
 */

public class ActivityInstance extends ControlNodeInstance {
    private final TaskExecutionBehavior taskExecutionBehavior;
    private final ScenarioInstance scenarioInstance;
    private final String label;
    private LinkedList<Integer> references;
    private final boolean isMailTask;
    /**
     * Database Connection objects.
     */
    private final DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
    private final DbActivityInstance dbActivityInstance = new DbActivityInstance();
    private final DbControlNode dbControlNode = new DbControlNode();
    private final DbReference dbReference = new DbReference();

    /**
     * Creates and initializes a new activity instance.
     * Reads the information for an existing activity instance from the database or creates a new one if no one
     * exist in the database.
     *
     * @param controlNode_id      This is the database id from the control node.
     * @param fragmentInstance_id This is the database id from the fragment instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     */
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
        this.incomingBehavior = new TaskIncomingControlFlowBehavior(this, scenarioInstance, stateMachine);
        this.outgoingBehavior = new TaskOutgoingControlFlowBehavior(controlNode_id, scenarioInstance, fragmentInstance_id, this);
        if (dbControlNode.getType(controlNode_id).equals("EmailTask")) {
            this.taskExecutionBehavior = new EmailTaskExecutionBehavior(controlNodeInstance_id, scenarioInstance, this);
            this.isMailTask = true;
        } else {
            this.taskExecutionBehavior = new HumanTaskExecutionBehavior(controlNodeInstance_id, scenarioInstance, this);
            this.isMailTask = false;
        }
    }

    /**
     * Starts the activity instance.
     * Sets the state of the activity to enabled. Starts the referential activities. Perform the execution behavior.
     *
     * @return true if the activity could started. false if the activity couldn't started.
     */
    public boolean begin() {
        if (((ActivityStateMachine) stateMachine).isEnabled()) {
            ((ActivityStateMachine) stateMachine).begin();
            ((TaskIncomingControlFlowBehavior) incomingBehavior).startReferences();
            ((TaskIncomingControlFlowBehavior) incomingBehavior).setDataObjectInstancesOnChange();
            scenarioInstance.checkDataFlowEnabled();
            scenarioInstance.checkExecutingGateways(controlNode_id);
            taskExecutionBehavior.execute();
            //System.out.println("Start Activity " + controlNode_id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets an activity to referential running.
     *
     * @return true if the activity could set to referential running. false if the activity couldn't set.
     */
    public boolean referenceStarted() {
        return ((ActivityStateMachine) stateMachine).referenceStarted();
    }

    /**
     * Terminates a referential running activity.
     * Enables the following control nodes.
     *
     * @return true if the activity could set to terminated. false if the activity couldn't set.
     */
    public boolean referenceTerminated() {
        boolean workFine = ((ActivityStateMachine) stateMachine).referenceTerminated();
        ((TaskOutgoingControlFlowBehavior) outgoingBehavior).enableFollowing();
        return workFine;
    }

    /**
     * Terminates a running activity.
     * Enables the following control nodes and sets the data outputs.
     *
     * @return true if the activity could set to terminated. false if the activity couldn't set.
     */
    @Override
    public boolean terminate() {
        boolean workingFine = ((ActivityStateMachine) stateMachine).terminate();
        ((TaskOutgoingControlFlowBehavior) outgoingBehavior).terminateReferences();
        outgoingBehavior.terminate();
        return workingFine;
    }

    /**
     * Checks if the Activity is now data enabled.
     */
    public void checkDataFlowEnabled() {
        ((TaskIncomingControlFlowBehavior) incomingBehavior).checkDataFlowEnabledAndEnableDataFlow();
    }

    @Override
    public boolean skip(){
        return ((ActivityStateMachine) stateMachine).skip();
    }
    /**
     * Getter
     */
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
