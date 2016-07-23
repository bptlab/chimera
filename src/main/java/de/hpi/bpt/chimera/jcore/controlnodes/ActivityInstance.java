package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.database.DbSelectedDataObjects;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.jcore.executionbehaviors.*;
import de.hpi.bpt.chimera.database.controlnodes.DbActivityInstance;
import de.hpi.bpt.chimera.database.controlnodes.DbBoundaryEvent;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNode;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.database.history.DbLogEntry;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.jcore.flowbehaviors.TaskIncomingControlFlowBehavior;
import de.hpi.bpt.chimera.jcore.flowbehaviors.TaskOutgoingBehavior;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements the specific state transitions of an activity instance.
 */

public class ActivityInstance extends AbstractControlNodeInstance {
	private final String label;
    private static Logger log = Logger.getLogger(ActivityInstance.class);

    /**
	 * Database Connection objects.
	 */
	private final DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
	private final DbActivityInstance dbActivityInstance = new DbActivityInstance();
	private final DbControlNode dbControlNode = new DbControlNode();

	private TaskExecutionBehavior taskExecutionBehavior;
    private ActivityExecutionBehavior executionBehavior;

    private boolean isAutomaticTask;
	private boolean canTerminate;

    /**
	 * Creates and initializes a new activity instance.
	 * Creates a new entry in the database for the new activity instance.
	 *
	 * @param controlNodeId      the database control node id.
	 * @param fragmentInstanceId the id of the fragment instance.
	 * @param scenarioInstance   the ScenarioInstance the activity should be added to.
	 */
	public ActivityInstance(int controlNodeId, int fragmentInstanceId,
			ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		this.setControlNodeId(controlNodeId);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.label = dbControlNode.getLabel(controlNodeId);
		scenarioInstance.getControlNodeInstances().add(this);
        this.setState(State.INIT);
		this.createDatabaseRepresentation();
		this.initActivityInstance();
	}

	/**
	 * Loads an existing ActivityInstance from the from the database.
	 *
	 * @param controlNodeId      the id of the activity model of the ActivityInstance.
	 * @param fragmentInstanceId the id from the fragment instance.
	 * @param scenarioInstance   the ScenarioInstance to which the ActivityInstance belongs.
	 * @param instanceId         the id of the existing ActivityInstance.
	 */
	public ActivityInstance(int controlNodeId, int fragmentInstanceId,
			ScenarioInstance scenarioInstance, int instanceId) {
		this.scenarioInstance = scenarioInstance;
		this.setControlNodeId(controlNodeId);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.label = dbControlNode.getLabel(controlNodeId);
		scenarioInstance.getControlNodeInstances().add(this);
		if (instanceId == -1) {
			setControlNodeInstanceId(dbControlNodeInstance.getControlNodeInstanceId(
                    controlNodeId, fragmentInstanceId));
		} else {
			this.setControlNodeInstanceId(instanceId);
		}
		this.initActivityInstance();
	}

    /**
     * Adds activity instance to the `controlnodeinstance` and `activityinstance` table.
     * TODO find out what happens when already exists.
     * This should only be executed after a new activity instance has been created.
     */
    private void createDatabaseRepresentation() {
        int controlNodeId = this.getControlNodeId();
        this.setControlNodeInstanceId(dbControlNodeInstance
                .createNewControlNodeInstance(controlNodeId,
                        "Activity", this.getFragmentInstanceId()));
        switch (dbControlNode.getType(controlNodeId)) {
            case "EmailTask":
                dbActivityInstance.createNewActivityInstance(
                        getControlNodeInstanceId(), "EmailTask", "init");
                dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
                break;
            case "WebServiceTask":
                dbActivityInstance.createNewActivityInstance(getControlNodeInstanceId(),
                        "WebServiceTask", "init");
                dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
                break;
            case "SendTask":
                dbActivityInstance.createNewActivityInstance(
                        getControlNodeInstanceId(), "SendTask", "init");
                dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
                break;
            case "IntermediateThrowEvent":
                dbActivityInstance.createNewActivityInstance(
                        getControlNodeInstanceId(), "IntermediateThrowEvent", "init");
                dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
                break;
            default:
                dbActivityInstance.createNewActivityInstance(
                        getControlNodeInstanceId(), "HumanTask", "init");
        }
    }

    /**
     * Sets incoming outgoing and execution behavior, according to the type of
     * the activity.
     *
     * This can be used when creating a new activity or when loading an activity
     * from the database.
     */
    private void initActivityInstance() {
        this.canTerminate = dbActivityInstance.getCanTerminate(getControlNodeInstanceId());
        this.setIncomingBehavior(new TaskIncomingControlFlowBehavior(
                this, scenarioInstance));
        this.setOutgoingBehavior(new TaskOutgoingBehavior(getControlNodeId(),
                scenarioInstance, getFragmentInstanceId(), this));
        switch (dbControlNode.getType(getControlNodeId())) {
            case "EmailTask":
                this.taskExecutionBehavior =
                        new EmailTaskExecutionBehavior(getControlNodeInstanceId(),
                                scenarioInstance, this);
                this.isAutomaticTask = true;
                break;
            case "WebServiceTask":
                this.taskExecutionBehavior = new WebServiceTaskExecutionBehavior(
                        getControlNodeInstanceId(), scenarioInstance, this);
                this.isAutomaticTask = true;
                break;
            //Added additional case: activities can be terminated every time
            case "SendTask":
            case "IntermediateThrowEvent":
                this.taskExecutionBehavior = new SendTaskExecutionBehavior(
                        getControlNodeInstanceId(), scenarioInstance, this);
                this.isAutomaticTask = true;
                break;
            case "Activity":
                this.setCanTerminate(true);
                this.taskExecutionBehavior = new HumanTaskExecutionBehavior(
                        getControlNodeInstanceId(), scenarioInstance, this);
                this.isAutomaticTask = false;
                break;
            default:
                this.taskExecutionBehavior = new HumanTaskExecutionBehavior(
                        getControlNodeInstanceId(), scenarioInstance, this);
                this.isAutomaticTask = false;
        }
    }

    public void terminate(Map<String, String> dataClassNameToStateName) {
        this.getOutgoingBehavior().terminate(dataClassNameToStateName);
    }

    /**
     * Checks if the Activity is now data enabled and updates the status accordingly.
     */
    public void checkDataFlowEnabled() {
        ((TaskIncomingControlFlowBehavior) getIncomingBehavior())
                .updateDataFlow();
    }

    /**
     * Cancels the running activity. The activity instance has to be running to be cancelled.
     * Cancelling of an activity unlocks all used data objects.
     */
    public void cancel() {
        if (!this.getState().equals(State.RUNNING)) {
            String errorMsg = "Tried cancelling an activity instance, which is not running";
            log.warn(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
        this.setState(State.CANCEL);
        TaskOutgoingBehavior out = this.getOutgoingBehavior();
        out.cancel();
    }

	@Override public void skip() {
		this.setState(State.SKIPPED);
	}

	// ************************************** Getter & Setter *************************//

	public TaskExecutionBehavior getTaskExecutionBehavior() {
		return taskExecutionBehavior;
	}


	public String getLabel() {
		return label;
	}

	public boolean isAutomaticTask() {
		return isAutomaticTask;
	}

	public void setCanTerminate(boolean canTerminate) {
		this.canTerminate = canTerminate;
		this.dbActivityInstance.setCanTerminate(getControlNodeInstanceId(), canTerminate);
	}


    public void setAutomaticTask(boolean automaticTask) {
        isAutomaticTask = automaticTask;
    }

    @Override
    public TaskOutgoingBehavior getOutgoingBehavior() {
        return (TaskOutgoingBehavior) super.getOutgoingBehavior();
    }

    public void begin(List<Integer> usedDataObjects) {
        ((ActivityExecutionBehavior) this.getExecutionBehavior()).begin(usedDataObjects);
    }
}