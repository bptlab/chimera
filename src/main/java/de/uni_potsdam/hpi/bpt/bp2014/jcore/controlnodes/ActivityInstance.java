package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.TaskIncomingControlFlowBehavior;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.TaskOutgoingControlFlowBehavior;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryLogger;

import java.util.LinkedList;
import java.util.Map;

/**
 * Represents the activity instance.
 * It save the state of the activity in the state machine. It has an outgoing behavior
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
	 * @param controlNodeId      This is the database id from the control node.
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 */
	public ActivityInstance(int controlNodeId, int fragmentInstanceId,
			ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		this.setControlNodeId(controlNodeId);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.label = dbControlNode.getLabel(controlNodeId);
		this.references = dbReference.getReferenceActivitiesForActivity(controlNodeId);
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
		this.references = dbReference.getReferenceActivitiesForActivity(controlNodeId);
		scenarioInstance.getControlNodeInstances().add(this);
		if (instanceId == -1) {
			setControlNodeInstanceId(dbControlNodeInstance
					.getControlNodeInstanceID(
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
		//Added additional case: activities can be terminated every time
		case "Activity":
			this.setCanTerminate(true);
		default:
			this.taskExecutionBehavior =
					new HumanTaskExecutionBehavior(getControlNodeInstanceId(),
					scenarioInstance, this);
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
		if (((ActivityStateMachine) getStateMachine()).isEnabled()) {
			((ActivityStateMachine) getStateMachine()).begin();
			((TaskIncomingControlFlowBehavior) getIncomingBehavior()).startReferences();
			((TaskIncomingControlFlowBehavior) getIncomingBehavior())
					.lockDataObjectInstances();
			scenarioInstance.updateDataFlow();
			scenarioInstance.checkXorGatewaysForTermination(getControlNodeId());
			taskExecutionBehavior.execute();
            enableAttachedEvents();
            if (isAutomaticTask) {
				this.terminate();
			}
			return true;
		} else {
			return false;
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
	 * Sets an activity to referential running.
	 *
	 * @return true if the activity could set to referential running.
	 * false if the activity couldn't set.
	 */
	public boolean referenceStarted() {
		return ((ActivityStateMachine) getStateMachine()).referenceStarted();
	}

	/**
	 * Terminates a referential running activity.
	 * Enables the following control nodes.
	 *
	 * @return true if the activity could set to terminated. false if the activity couldn't set.
	 */
	public boolean referenceTerminated() {
		boolean workFine = ((ActivityStateMachine) getStateMachine()).referenceTerminated();
		((TaskOutgoingControlFlowBehavior) getOutgoingBehavior()).enableFollowing();
		return workFine;
	}

	/**
	 * Terminates a running activity.
	 * Enables the following control nodes and sets the data outputs.
	 *
	 * @return true if the activity could set to terminated. false if the activity couldn't set.
	 */
	@Override public boolean terminate() {
        return this.terminate(-1);
	}

	/**
	 * Terminates a running activity.
	 * Enables the following control nodes and sets the data outputs.
	 *
	 * @param outputSetId ID of the output set of the activity.
	 * @return true if the activity could set to terminated. false if the activity couldn't set.
	 */
	public boolean terminate(int outputSetId) {
		if (canTerminate) {
            HistoryLogger logger = new HistoryLogger();
            logger.logActivityTransition(this.getControlNodeInstanceId(), "terminated");
            boolean workingFine = getStateMachine().terminate();
			((TaskOutgoingControlFlowBehavior) getOutgoingBehavior())
					.terminateReferences();
			((TaskOutgoingControlFlowBehavior) getOutgoingBehavior())
					.terminate(outputSetId);
			cancelAttachedEvents();
            return workingFine;
		}
		return false;
	}

    private void cancelAttachedEvents() {
        DbBoundaryEvent boundaryEventDao = new DbBoundaryEvent();
        int boundaryEventId = boundaryEventDao.getBoundaryEventForActivity(this.getControlNodeId());
        EventDispatcher.unregisterEvent(boundaryEventId, this.getFragmentInstanceId());
    }

	/**
	 * sets the dataAttributes for an activity.
	 *
	 * @param values values that the attributes should be set to.
	 */
	public void setDataAttributeValues(Map<Integer, String> values) {
		if (AbstractStateMachine.STATE.RUNNING == getStateMachine().getState()) {
			taskExecutionBehavior.setDataAttributeValues(values);
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
	 * @return the Scenario Instance
	 */
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	/**
	 * @return the Label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the References
	 */
	public LinkedList<Integer> getReferences() {
		return references;
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

}