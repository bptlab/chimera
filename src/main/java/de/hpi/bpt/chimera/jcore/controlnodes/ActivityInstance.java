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
import de.hpi.bpt.chimera.jcore.flowbehaviors.TaskOutgoingControlFlowBehavior;
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
		this.createDatabaseRepresentation();
		this.setStateMachine(new ActivityStateMachine(
				getControlNodeInstanceId(), scenarioInstance, this));
        // TODO why does this have to happen
		((ActivityStateMachine) getStateMachine()).enableControlFlow();
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
		this.setStateMachine(new ActivityStateMachine(
				getControlNodeInstanceId(), scenarioInstance, this));
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
                this, scenarioInstance, getStateMachine()));
        this.setOutgoingBehavior(new TaskOutgoingControlFlowBehavior(getControlNodeId(),
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

	/**
	 * Begins an ActivityInstance without specifying the data objects used.
     *
     * This is possible when either, the input set of the ActivityInstance is empty,
     * or there is only one possible data object configuration for the input set.
     * Internally determines the data objects and calls {@link #begin(List)}.
	 *
     * @throws IllegalArgumentException when there is more than one possible input selection.
	 * @return true if the activity could started. false if the activity couldn't started.
	 */
	public boolean begin() {
        if (!((ActivityStateMachine) getStateMachine()).isEnabled()) {
            return false;
        }
        DataManager dataManager = scenarioInstance.getDataManager();
        List<DataObject> dataObjects = dataManager.getAvailableInput(
                this.getControlNodeId());
        long distinctDataclasses = dataObjects.stream().map(DataObject::getDataClassId)
                .distinct().count();
        if (!(dataObjects.size() == distinctDataclasses)) {
            String errorMsg = "Trying to start an activity instance with multiple possible" +
                    " input data objects, without specifying selected data object.";
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        List<Integer> dataobjectids = dataObjects.stream().map(DataObject::getId)
                .collect(Collectors.toList());
        return this.begin(dataobjectids);
	}

    /**
     * Begins the activity instance. This locks all data objects, which are used by this activity.
     * Beginning an activity also begins all events attached to it.
     *
     * @param workingItems Ids of the data objects used by this activity
     * @return Whether the activity could have been started.
     */
    public boolean begin(List<Integer> workingItems) {
        if (!((ActivityStateMachine) getStateMachine()).isEnabled()) {
            return false;
        }
        ((ActivityStateMachine) getStateMachine()).begin();

        ((TaskIncomingControlFlowBehavior) getIncomingBehavior())
                .lockDataObjects(workingItems);
        DbSelectedDataObjects dbDataObjectSelection = new DbSelectedDataObjects();
        int scenarioInstanceId = this.getScenarioInstance().getId();
        dbDataObjectSelection.saveDataObjectSelection(scenarioInstanceId,
                this.getControlNodeInstanceId(), workingItems);

        beginExecution();
        return true;
    }

    private void beginExecution() {
        int scenarioInstanceId = this.scenarioInstance.getId();
        new DbLogEntry().logActivity(
                this.getControlNodeInstanceId(), "running", scenarioInstanceId);
        scenarioInstance.updateDataFlow();
        scenarioInstance.checkXorGatewaysForTermination(getControlNodeId());
        taskExecutionBehavior.execute();

        registerAttachedEvents();
        if (isAutomaticTask) {
            this.terminate();
        }
    }


	/**
	 * Terminates a running ActivityInstance without specifying the states, to which
     * the data objects used by the ActivityInstance are set to.
	 *
     * TODO check whether this works
	 * @return true if the activity could set to terminated. false if the activity couldn't set.
	 */
	@Override public boolean terminate() {
        return this.terminate(new HashMap<>());
	}

	/**
	 * Terminates a running ActivityInstance.
	 * Enables the following control nodes and sets the data outputs, according to the
     * passed specification.
	 *
	 * @param dataClassNameToStateName the specification for each data object to
     *                                 which state it should be set.
     * @return true if the activity could set to terminated. false if the activity couldn't set.
     */
	public boolean terminate(Map<String, String> dataClassNameToStateName) {
		if (canTerminate) {
            int scenarioInstanceId = this.getScenarioInstance().getId();
            new DbLogEntry().logActivity(
                    this.getControlNodeInstanceId(), "terminated", scenarioInstanceId);
            boolean workingFine = getStateMachine().terminate();
			((TaskOutgoingControlFlowBehavior) getOutgoingBehavior()).terminate(
                    dataClassNameToStateName);
			cancelAttachedEvents();
            return workingFine;
		}
		return false;
	}

    /**
     * Checks if the Activity is now data enabled and updates the status accordingly.
     */
    public void checkDataFlowEnabled() {
        ((TaskIncomingControlFlowBehavior) getIncomingBehavior())
                .checkDataFlowEnabledAndEnableDataFlow();
    }

    /**
     * Cancels the running activity. The activity instance has to be running to be cancelled.
     * Cancelling of an activity unlocks all used data objects.
     */
    public void cancel() {
        ActivityStateMachine stateMachine = (ActivityStateMachine) this.getStateMachine();
        AbstractStateMachine.STATE activityState = stateMachine.getState();
        if (!activityState.equals(AbstractStateMachine.STATE.RUNNING)) {
            String errorMsg = "Tried cancelling an activity instance, which is not running";
            log.warn(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
        stateMachine.cancel();
        TaskOutgoingControlFlowBehavior out = (TaskOutgoingControlFlowBehavior)
                this.getOutgoingBehavior();
        out.cancel();
    }

    private void registerAttachedEvents() {
        DbBoundaryEvent boundaryEventDao = new DbBoundaryEvent();
        int boundaryEventId = boundaryEventDao.getBoundaryEventForActivity(this.getControlNodeId());
        if (boundaryEventId != -1) {
            BoundaryEvent event = new BoundaryEvent(boundaryEventId,
                    this.getFragmentInstanceId(), this.getScenarioInstance());
            event.enableControlFlow();
        }
    }

    private void cancelAttachedEvents() {
        DbBoundaryEvent boundaryEventDao = new DbBoundaryEvent();
        int boundaryEventId = boundaryEventDao.getBoundaryEventForActivity(this.getControlNodeId());
		// if activity has attached event
		if (boundaryEventId > 0) {
			EventDispatcher.unregisterEvent(boundaryEventId, this.getFragmentInstanceId());
		}
    }


	@Override public boolean skip() {
		return getStateMachine().skip();
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

    public AbstractStateMachine.STATE getState() {
        return this.getStateMachine().getState();
    }
}