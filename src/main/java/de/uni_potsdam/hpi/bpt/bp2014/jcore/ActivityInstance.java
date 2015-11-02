package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbReference;

import java.util.LinkedList;
import java.util.Map;

/**
 * Represents the activity instance.
 * It save the state of the activity in the state machine. It has an outgoing behavior
 * and an incoming behavior.
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
	 * @param controlNodeId      This is the database id from the control node.
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 */
	public ActivityInstance(int controlNodeId, int fragmentInstanceId,
			ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		this.controlNodeId = controlNodeId;
		this.fragmentInstanceId = fragmentInstanceId;
		this.label = dbControlNode.getLabel(controlNodeId);
		this.references = dbReference.getReferenceActivitiesForActivity(controlNodeId);
		scenarioInstance.getControlNodeInstances().add(this);
		//creates a new Activity Instance also in database
		this.controlNodeInstanceId = dbControlNodeInstance
				.createNewControlNodeInstance(
						controlNodeId, "Activity", fragmentInstanceId);
		switch (dbControlNode.getType(controlNodeId)) {
		case "EmailTask":
			dbActivityInstance
					.createNewActivityInstance(
							controlNodeInstanceId,
							"EmailTask",
							"init");
			dbActivityInstance.setAutomaticExecution(controlNodeInstanceId, true);
			break;
		case "WebServiceTask":
			dbActivityInstance
					.createNewActivityInstance(
							controlNodeInstanceId,
							"WebServiceTask",
							"init");
			dbActivityInstance.setAutomaticExecution(controlNodeInstanceId, true);
			break;
		default:
			dbActivityInstance
					.createNewActivityInstance(
							controlNodeInstanceId,
							"HumanTask",
							"init");
		}
		this.stateMachine = new ActivityStateMachine(
				controlNodeInstanceId, scenarioInstance, this);
		((ActivityStateMachine) stateMachine).enableControlFlow();
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
		this.controlNodeId = controlNodeId;
		this.fragmentInstanceId = fragmentInstanceId;
		this.label = dbControlNode.getLabel(controlNodeId);
		this.references = dbReference.getReferenceActivitiesForActivity(controlNodeId);
		scenarioInstance.getControlNodeInstances().add(this);
		if (instanceId == -1) {
			controlNodeInstanceId = dbControlNodeInstance
					.getControlNodeInstanceID(
							controlNodeId, fragmentInstanceId);
		} else {
			this.controlNodeInstanceId = instanceId;
		}
		this.stateMachine = new ActivityStateMachine(
				controlNodeInstanceId, scenarioInstance,
				this);
		this.initActivityInstance();
	}

	/**
	 * Initialize other information for the instance.
	 */
	private void initActivityInstance() {
		this.canTerminate = dbActivityInstance.getCanTerminate(controlNodeInstanceId);
		this.automaticExecution =
				dbActivityInstance.getAutomaticExecution(controlNodeInstanceId);
		this.incomingBehavior = new TaskIncomingControlFlowBehavior(this, scenarioInstance,
				stateMachine);
		this.outgoingBehavior = new TaskOutgoingControlFlowBehavior(controlNodeId,
				scenarioInstance, fragmentInstanceId, this);
		switch (dbControlNode.getType(controlNodeId)) {
		case "EmailTask":
			this.taskExecutionBehavior =
					new EmailTaskExecutionBehavior(controlNodeInstanceId,
					scenarioInstance, this);
			this.isAutomaticTask = true;
			break;
		case "WebServiceTask":
			this.taskExecutionBehavior =
					new WebServiceTaskExecutionBehavior(controlNodeInstanceId,
					scenarioInstance, this);
			this.isAutomaticTask = true;
			break;
		//Added additional case: activities can be terminated every time
		case "Activity":
			this.setCanTerminate(true);
		default:
			this.taskExecutionBehavior =
					new HumanTaskExecutionBehavior(controlNodeInstanceId,
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
		if (((ActivityStateMachine) stateMachine).isEnabled()) {
			((ActivityStateMachine) stateMachine).begin();
			((TaskIncomingControlFlowBehavior) incomingBehavior).startReferences();
			((TaskIncomingControlFlowBehavior) incomingBehavior)
					.setDataObjectInstancesOnChange();
			scenarioInstance.checkDataFlowEnabled();
			scenarioInstance.checkExecutingGateways(controlNodeId);
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
	 * @return true if the activity could set to referential running.
	 * false if the activity couldn't set.
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
			boolean workingFine = stateMachine.terminate();
			((TaskOutgoingControlFlowBehavior) outgoingBehavior).terminateReferences();
			((TaskOutgoingControlFlowBehavior) outgoingBehavior).terminate(outputSetId);
			return workingFine;
		}
		return false;
	}

	/**
	 * sets the dataAttributes for an activity.
	 *
	 * @param values values that the attributes should be set to.
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
		((TaskIncomingControlFlowBehavior) incomingBehavior)
				.checkDataFlowEnabledAndEnableDataFlow();
	}

	@Override public boolean skip() {
		return stateMachine.skip();
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
		this.dbActivityInstance
				.setAutomaticExecution(getControlNodeInstanceId(), automaticExecution);
	}

	/**
	 * @param canTerminate a boolean for setting the termination type.
	 */
	public void setCanTerminate(boolean canTerminate) {
		this.canTerminate = canTerminate;
		this.dbActivityInstance.setCanTerminate(getControlNodeInstanceId(), canTerminate);
	}
}
