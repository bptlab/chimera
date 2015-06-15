package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbReference;

import java.util.LinkedList;
import java.util.Map;

/**
 * Represents the activity instance.
 * It save the state of the activity in the state machine. It has an outgoing behavior and an incoming behavior.
 */

public class ActivityInstance extends ControlNodeInstance {
    private final ScenarioInstance scenarioInstance;
    private final String label;
    /**
     * Database Connection objects.
     */
    private final DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
    private final DbActivityInstance dbActivityInstance = new DbActivityInstance();
    private final DbControlNode dbControlNode = new DbControlNode();
    private final DbReference dbReference = new DbReference();
    private TaskExecutionBehavior taskExecutionBehavior;
    private boolean isAutomaticTask;
    private LinkedList<Integer> references;
    private boolean automaticExecution;
    private boolean canTerminate;

    /**
     * Creates and initializes a new activity instance.
     * Creates a new entry in the database for the new activity instance.
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
        //creates a new Activity Instance also in database
        this.controlNodeInstance_id = dbControlNodeInstance.createNewControlNodeInstance(controlNode_id, "Activity", fragmentInstance_id);
        switch (dbControlNode.getType(controlNode_id)) {
            case "EmailTask":
                dbActivityInstance.createNewActivityInstance(controlNodeInstance_id, "EmailTask", "init");
                dbActivityInstance.setAutomaticExecution(controlNodeInstance_id, true);
                break;
            case "WebServiceTask":
                dbActivityInstance.createNewActivityInstance(controlNodeInstance_id, "WebServiceTask", "init");
                dbActivityInstance.setAutomaticExecution(controlNodeInstance_id, true);
                break;
            default:
                dbActivityInstance.createNewActivityInstance(controlNodeInstance_id, "HumanTask", "init");
        }
        this.stateMachine = new ActivityStateMachine(controlNodeInstance_id, scenarioInstance, this);
        ((ActivityStateMachine) stateMachine).enableControlFlow();
        this.initActivityInstance();
    }

    /**
     * Creates and initializes a new activity instance.
     * Reads the information for an existing activity instance from the database.
     *
     * @param controlNode_id      This is the database id from the control node.
     * @param fragmentInstance_id This is the database id from the fragment instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param instance_id         This is an id of the activity instance.
     */
    public ActivityInstance(int controlNode_id, int fragmentInstance_id, ScenarioInstance scenarioInstance, int instance_id) {
        this.scenarioInstance = scenarioInstance;
        this.controlNode_id = controlNode_id;
        this.fragmentInstance_id = fragmentInstance_id;
        this.label = dbControlNode.getLabel(controlNode_id);
        this.references = dbReference.getReferenceActivitiesForActivity(controlNode_id);
        scenarioInstance.getControlNodeInstances().add(this);
        if (instance_id == -1) {
            controlNodeInstance_id = dbControlNodeInstance.getControlNodeInstanceID(controlNode_id, fragmentInstance_id);
        } else {
            this.controlNodeInstance_id = instance_id;
        }
        this.stateMachine = new ActivityStateMachine(controlNodeInstance_id, scenarioInstance, this);
        this.initActivityInstance();
    }

    /**
     * Initialize other information for the instance.
     */
    private void initActivityInstance() {
        this.canTerminate = dbActivityInstance.getCanTerminate(controlNodeInstance_id);
        this.automaticExecution = dbActivityInstance.getAutomaticExecution(controlNodeInstance_id);
        this.incomingBehavior = new TaskIncomingControlFlowBehavior(this, scenarioInstance, stateMachine);
        this.outgoingBehavior = new TaskOutgoingControlFlowBehavior(controlNode_id, scenarioInstance, fragmentInstance_id, this);
        switch (dbControlNode.getType(controlNode_id)) {
            case "EmailTask":
                this.taskExecutionBehavior = new EmailTaskExecutionBehavior(controlNodeInstance_id, scenarioInstance, this);
                this.isAutomaticTask = true;
                break;
            case "WebServiceTask":
                this.taskExecutionBehavior = new WebServiceTaskExecutionBehavior(controlNodeInstance_id, scenarioInstance, this);
                this.isAutomaticTask = true;
                break;
            default:
                this.taskExecutionBehavior = new HumanTaskExecutionBehavior(controlNodeInstance_id, scenarioInstance, this);
                this.isAutomaticTask = false;
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
            if (isAutomaticTask) {
                this.terminate();
            }
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
        return this.terminate(-1);
    }

    public boolean terminate(int outputSet_id) {
        if (canTerminate) {
            boolean workingFine = stateMachine.terminate();
            ((TaskOutgoingControlFlowBehavior) outgoingBehavior).terminateReferences();
            ((TaskOutgoingControlFlowBehavior) outgoingBehavior).terminate(outputSet_id);
            return workingFine;
        }
        return false;
    }

    /**
     * sets the dataAttributes for an activity
     */
    public void setDataAttributeValues(Map<Integer, String> values) {
        if (((ActivityStateMachine) stateMachine).state.equals("running")) {
            taskExecutionBehavior.setDataAttributeValues(values);
        }
    }

    /**
     * Checks if the Activity is now data enabled.
     */
    public void checkDataFlowEnabled() {
        ((TaskIncomingControlFlowBehavior) incomingBehavior).checkDataFlowEnabledAndEnableDataFlow();
    }

    @Override
    public boolean skip() {
        return stateMachine.skip();
    }

    // ************************************** Getter & Setter *************************//

    /**
     * @return
     */
    public TaskExecutionBehavior getTaskExecutionBehavior() {
        return taskExecutionBehavior;
    }

    /**
     * @return
     */
    public ScenarioInstance getScenarioInstance() {
        return scenarioInstance;
    }

    /**
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return
     */
    public LinkedList<Integer> getReferences() {
        return references;
    }

    /**
     * @return
     */
    public boolean getIsAutomaticTask() {
        return isAutomaticTask;
    }

    /**
     * @return
     */
    public boolean isAutomaticExecution() {

        return automaticExecution;
    }

    /**
     * @param automaticExecution
     */
    public void setAutomaticExecution(boolean automaticExecution) {
        this.automaticExecution = automaticExecution;
        this.dbActivityInstance.setAutomaticExecution(controlNodeInstance_id, automaticExecution);
    }

    /**
     * @param canTerminate
     */
    public void setCanTerminate(boolean canTerminate) {
        this.canTerminate = canTerminate;
        this.dbActivityInstance.setCanTerminate(controlNodeInstance_id, canTerminate);
    }
}
