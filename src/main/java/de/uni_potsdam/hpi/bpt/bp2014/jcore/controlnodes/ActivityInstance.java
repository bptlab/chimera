package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbBoundaryEvent;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.TaskIncomingControlFlowBehavior;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.TaskOutgoingControlFlowBehavior;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the activity instance.
 * It saves the state of the activity in the state machine. It has an outgoing behavior
 * and an incoming behavior.
 */

public class ActivityInstance extends AbstractControlNodeInstance {
	private final String label;
	/**
	 * Database Connection objects.
	 */
	private final DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
	private final DbActivityInstance dbActivityInstance = new DbActivityInstance();
	private final DbControlNode dbControlNode = new DbControlNode();
	private TaskExecutionBehavior taskExecutionBehavior;
	private boolean isAutomaticTask;
	private boolean automaticExecution;
	private boolean canTerminate;

    /**
	 * Creates and initializes a new activity instance.
	 * Creates a new entry in the database for the new activity instance.
	 *
	 * @param controlNodeId      This is the database id from the control node.
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param scenarioInstance   This is an instance from the class ScenarioInstance.
	 */
	public ActivityInstance(int controlNodeId, int fragmentInstanceId,
			ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		this.setControlNodeId(controlNodeId);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.label = dbControlNode.getLabel(controlNodeId);
		scenarioInstance.getControlNodeInstances().add(this);
		//creates a new Activity Instance also in database
		this.setControlNodeInstanceId(dbControlNodeInstance
				.createNewControlNodeInstance(
						controlNodeId, "Activity", fragmentInstanceId));
		switch (dbControlNode.getType(controlNodeId)) {
		case "EmailTask":
			dbActivityInstance
					.createNewActivityInstance(
							getControlNodeInstanceId(),
							"EmailTask",
							"init");
			dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
			break;
		case "WebServiceTask":
			dbActivityInstance
					.createNewActivityInstance(
							getControlNodeInstanceId(),
							"WebServiceTask",
							"init");
			dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
			break;
		case "SendTask":
			dbActivityInstance
					.createNewActivityInstance(
							getControlNodeInstanceId(),
							"SendTask",
							"init");
			dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
			break;
		case "IntermediateThrowEvent":
			dbActivityInstance
					.createNewActivityInstance(
							getControlNodeInstanceId(),
							"IntermediateThrowEvent",
							"init");
			dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
			break;
		default:
			dbActivityInstance
					.createNewActivityInstance(
							getControlNodeInstanceId(),
							"HumanTask",
							"init");
		}
		this.setStateMachine(new ActivityStateMachine(
				getControlNodeInstanceId(), scenarioInstance, this));
		((ActivityStateMachine) getStateMachine()).enableControlFlow();
		this.initActivityInstance();
	}

	/**
	 * Creates and initializes a new activity instance.
	 * Reads the information for an existing activity instance from the database.
	 *
	 * @param controlNodeId      This is the database id from the control node.
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param instanceId         This is an id of the activity instance.
	 */
	public ActivityInstance(int controlNodeId, int fragmentInstanceId,
			ScenarioInstance scenarioInstance, int instanceId) {
		this.scenarioInstance = scenarioInstance;
		this.setControlNodeId(controlNodeId);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.label = dbControlNode.getLabel(controlNodeId);
		scenarioInstance.getControlNodeInstances().add(this);
		if (instanceId == -1) {
			setControlNodeInstanceId(dbControlNodeInstance
					.getControlNodeInstanceId(
							controlNodeId, fragmentInstanceId));
		} else {
			this.setControlNodeInstanceId(instanceId);
		}
		this.setStateMachine(new ActivityStateMachine(
				getControlNodeInstanceId(), scenarioInstance,
				this));
		this.initActivityInstance();
	}

	/**
	 * Initialize other information for the instance.
	 */
	private void initActivityInstance() {
		this.canTerminate = dbActivityInstance.getCanTerminate(getControlNodeInstanceId());
		this.automaticExecution =
				dbActivityInstance.getAutomaticExecution(
						getControlNodeInstanceId());
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
			this.taskExecutionBehavior =
					new WebServiceTaskExecutionBehavior(
							getControlNodeInstanceId(),
					scenarioInstance, this);
			this.isAutomaticTask = true;
			break;
		case "SendTask":
		case "IntermediateThrowEvent":
			this.taskExecutionBehavior =
					new SendTaskExecutionBehavior(
							getControlNodeInstanceId(),
							scenarioInstance, this);
			this.isAutomaticTask = true;
			break;
		//Added additional case: activities can be terminated every time
		case "Activity":
			this.setCanTerminate(true);
		default:
			this.taskExecutionBehavior = new HumanTaskExecutionBehavior(
                    getControlNodeInstanceId(), scenarioInstance, this);
			this.isAutomaticTask = false;
		}
	}

	/**
	 * Starts the activity instance.
	 * Sets the state of the activity to enabled. Starts the referential activities.
	 * Perform the execution behavior.
	 *
	 * @return true if the activity could started. false if the activity couldn't started.
	 */
	public boolean begin() {
        DataManager dataManager = scenarioInstance.getDataManager();
        List<DataObject> dataObjects = dataManager.getAvailableInput(
                this.getControlNodeId());
        Set<Integer> dataclassIds = dataObjects.stream().map(DataObject::getDataClassId)
                .collect(Collectors.toSet());
        assert dataObjects.size() == dataclassIds.size(): "Data object selection underspecified";

        // TODO find better error msg
        List<Integer> dataobjectids = dataObjects.stream().map(DataObject::getId)
                .collect(Collectors.toList());
        return this.begin(dataobjectids);
	}

    /**
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
        enableAttachedEvents();
        if (isAutomaticTask) {
            this.terminate();
        }
    }

    private void enableAttachedEvents() {
        DbBoundaryEvent boundaryEventDao = new DbBoundaryEvent();
        int boundaryEventId = boundaryEventDao.getBoundaryEventForActivity(this.getControlNodeId());
        if (boundaryEventId != -1) {
            BoundaryEvent event = new BoundaryEvent(boundaryEventId,
                    this.getFragmentInstanceId(), this.getScenarioInstance());
            event.enableControlFlow();
        }
    }

	/**
	 * Terminates a running activity.
	 * Enables the following control nodes and sets the data outputs.
	 *
	 * @return true if the activity could set to terminated. false if the activity couldn't set.
	 */
	@Override public boolean terminate() {
        return this.terminate(new HashMap<>());
	}

	/**
	 * Terminates a running activity.
	 * Enables the following control nodes and sets the data outputs.
	 *
	 * @param dataClassNameToStateName ids of the data o
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

    private void cancelAttachedEvents() {
        DbBoundaryEvent boundaryEventDao = new DbBoundaryEvent();
        int boundaryEventId = boundaryEventDao.getBoundaryEventForActivity(this.getControlNodeId());
		// if activity has attached event
		if (boundaryEventId > 0) {
			EventDispatcher.unregisterEvent(
					boundaryEventId, this.getFragmentInstanceId());
		}
    }

	/**
	 * Checks if the Activity is now data enabled.
	 */
	public void checkDataFlowEnabled() {
		((TaskIncomingControlFlowBehavior) getIncomingBehavior())
				.checkDataFlowEnabledAndEnableDataFlow();
	}

	@Override public boolean skip() {
		return getStateMachine().skip();
	}

	// ************************************** Getter & Setter *************************//

	/**
	 * @return the Task Execution Behavior
	 */
	public TaskExecutionBehavior getTaskExecutionBehavior() {
		return taskExecutionBehavior;
	}

	/**
	 * @return the Label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return whether the Task is automatic
	 */
	public boolean getIsAutomaticTask() {
		return isAutomaticTask;
	}

	/**
	 * @return the Execution is automatic
	 */
	public boolean isAutomaticExecution() {

		return automaticExecution;
	}

	/**
	 * @param automaticExecution a boolean for setting the execution type.
	 */
	public void setAutomaticExecution(boolean automaticExecution) {
		this.automaticExecution = automaticExecution;
		this.dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(),
				automaticExecution);
	}

	/**
	 * @param canTerminate a boolean for setting the termination type.
	 */
	public void setCanTerminate(boolean canTerminate) {
		this.canTerminate = canTerminate;
		this.dbActivityInstance.setCanTerminate(getControlNodeInstanceId(), canTerminate);
	}

    /**
     *
     */
    public void cancel() {
        ActivityStateMachine stateMachine = (ActivityStateMachine) this.getStateMachine();
        stateMachine.cancel();
        TaskOutgoingControlFlowBehavior out = (TaskOutgoingControlFlowBehavior)
                this.getOutgoingBehavior();
        out.cancel();
    }

    public AbstractStateMachine.STATE getState() {
        return this.getStateMachine().getState();
    }
}