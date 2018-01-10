package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.database.controlnodes.DbActivityInstance;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNode;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.jcore.executionbehaviors.ActivityExecutionBehavior;
import de.hpi.bpt.chimera.jcore.executionbehaviors.EmailTaskExecutionBehavior;
import de.hpi.bpt.chimera.jcore.executionbehaviors.SendTaskExecutionBehavior;
import de.hpi.bpt.chimera.jcore.executionbehaviors.WebServiceTaskExecutionBehavior;
import de.hpi.bpt.chimera.jcore.flowbehaviors.ExclusiveGatewaySplitBehavior;
import de.hpi.bpt.chimera.jcore.flowbehaviors.TaskIncomingControlFlowBehavior;
import de.hpi.bpt.chimera.jcore.flowbehaviors.TaskOutgoingBehavior;
import de.hpi.bpt.chimera.jcore.flowbehaviors.WebServiceTaskOutgoingBehavior;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Implements the specific state transitions of an activity instance.
 */

public class ActivityInstance extends AbstractControlNodeInstance {
	private static Logger log = Logger.getLogger(ActivityInstance.class);
	private final String label;
	/**
	 * Database Connection objects.
	 */
	private final DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
	private final DbActivityInstance dbActivityInstance = new DbActivityInstance();
	private final DbControlNode dbControlNode = new DbControlNode();

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
	public ActivityInstance(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		this.setControlNodeId(controlNodeId);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.label = dbControlNode.getLabel(controlNodeId);
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
	public ActivityInstance(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance, int instanceId) {
		this.scenarioInstance = scenarioInstance;
		this.setControlNodeId(controlNodeId);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.label = dbControlNode.getLabel(controlNodeId);
		if (instanceId == -1) {
			setControlNodeInstanceId(dbControlNodeInstance.getControlNodeInstanceId(controlNodeId, fragmentInstanceId));
		} else {
			this.setControlNodeInstanceId(instanceId);
		}
		this.setState(new DbControlNodeInstance().getState(getControlNodeInstanceId()));
		this.initActivityInstance();
	}

	/**
	 * Adds activity instance to the `controlnodeinstance` and `activityinstance` table.
	 * TODO find out what happens when already exists.
	 * This should only be executed after a new activity instance has been created.
	 */
	private void createDatabaseRepresentation() {
		int controlNodeId = this.getControlNodeId();
		this.setControlNodeInstanceId(dbControlNodeInstance.createNewControlNodeInstance(controlNodeId, "Activity", this.getFragmentInstanceId(), State.INIT));
		switch (dbControlNode.getType(controlNodeId)) {
			case "EmailTask":
				dbActivityInstance.createNewActivityInstance(getControlNodeInstanceId(), "EmailTask");
				dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
				break;
			case "WebServiceTask":
				dbActivityInstance.createNewActivityInstance(getControlNodeInstanceId(), "WebServiceTask");
				dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
				break;
			case "SendTask":
				dbActivityInstance.createNewActivityInstance(getControlNodeInstanceId(), "SendTask");
				dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
				break;
			case "IntermediateThrowEvent":
				dbActivityInstance.createNewActivityInstance(getControlNodeInstanceId(), "IntermediateThrowEvent");
				dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
				break;
			default:
				dbActivityInstance.createNewActivityInstance(getControlNodeInstanceId(), "HumanTask");
		}
	}

	/**
	 * Sets incoming, outgoing and execution behavior, according to the type of
	 * the activity.
	 * <p>
	 * This can be used when creating a new activity or when loading an activity
	 * from the database.
	 */
	private void initActivityInstance() {
		this.canTerminate = dbActivityInstance.getCanTerminate(getControlNodeInstanceId());
		this.setIncomingBehavior(new TaskIncomingControlFlowBehavior(this, scenarioInstance));
		this.setOutgoingBehavior(new TaskOutgoingBehavior(getControlNodeId(), scenarioInstance, getFragmentInstanceId(), this));
		String type = dbControlNode.getType(getControlNodeId());
		switch (type) {
			case "EmailTask":
				this.setExecutionBehavior(new EmailTaskExecutionBehavior(this));
				allowAutomaticExecution();
				break;
			case "WebServiceTask":
				this.setExecutionBehavior(new WebServiceTaskExecutionBehavior(this));
				this.setOutgoingBehavior(new WebServiceTaskOutgoingBehavior(getControlNodeId(), scenarioInstance, getFragmentInstanceId(), this));
				allowAutomaticExecution();
				break;
			//Added additional case: activities can be terminated every time
			case "SendTask":
			case "IntermediateThrowEvent":
				this.setExecutionBehavior(new SendTaskExecutionBehavior(this));
				allowAutomaticExecution();
				break;
			case "Activity":
				this.setExecutionBehavior(new ActivityExecutionBehavior(this));
				forbidAutomaticExecution();
				break;
			default:
				this.setExecutionBehavior(new ActivityExecutionBehavior(this));
				forbidAutomaticExecution();
				log.warn(String.format("Initializing unsupported activity type: %s", type));
		}
	}

	/**
	 * Begin an enabled activity with provided data objects.
	 * @param selectedDataObjectIds - IDs of selected data objects
	 */
	public void begin(List<Integer> selectedDataObjectIds) {
	  ((ActivityExecutionBehavior) this.getExecutionBehavior()).begin(selectedDataObjectIds);
		this.setState(State.RUNNING);
		if (this.isAutomaticTask()) {
			this.terminate();
		}
	}

	/**
	 * @deprecated This is only called by Tests! Use begin(List<Integer>) instead which
	 * expects a list of selected data objects the activity should work on
	 */
	@Override
	public void begin() {
		super.begin();
		this.setState(State.RUNNING);
	}

	public void terminate(Map<String, String> dataClassNameToStateName) {
		this.getOutgoingBehavior().terminate(dataClassNameToStateName);
	}

	/**
	 * Checks if the Activity is now data enabled and updates the status accordingly.
	 */
	public void checkDataFlowEnabled() {
		((TaskIncomingControlFlowBehavior) getIncomingBehavior()).updateDataFlow();
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

	@Override
	public void skip() {
		super.skip();
		this.setState(State.SKIPPED);
	}

	// ************************************** Getter & Setter *************************//

	public String getLabel() {
		return label;
	}

	public boolean isAutomaticTask() {
		return isAutomaticTask;
	}

	/**
	 * Tries to set the flag for automatic execution of this activity instance to {@literal true}. 
	 * This fails if the activity has multiple input or output sets which would require user choice.
	 * Gateways themselves take care to forbid automatic execution of their successor activities, 
	 * @see ExclusiveGatewaySplitBehavior. 
	 */
	public void allowAutomaticExecution() {
		DbDataFlow dbDataFlow = new DbDataFlow();
		List<Integer> input = dbDataFlow.getInputSetsForControlNode(getControlNodeId());
		List<Integer> output = dbDataFlow.getOutputSetsForControlNode(getControlNodeId());
		if (input.size() > 1 && output.size() > 1) {
			log.warn("Tasks with more than one input or output set cannot be executed automatically.");
			isAutomaticTask = false;
			dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), false);
		} else {
			isAutomaticTask = true;
			dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), true);
		}
	}

	/**
	 * Sets the flag for automatic execution of this activity to {@literal false}. 
	 * This is used by exclusive gateways to prevent that the branch starting with the automatic
	 * activity is always taken. In this case, the automatic activity has to be started manually
	 * by the user.  
	 */
	public void forbidAutomaticExecution() {
		isAutomaticTask = false;
		dbActivityInstance.setAutomaticExecution(getControlNodeInstanceId(), false);
	}
	
	public void setCanTerminate(boolean canTerminate) {
		this.canTerminate = canTerminate;
		this.dbActivityInstance.setCanTerminate(getControlNodeInstanceId(), canTerminate);
	}

	@Override
	public TaskOutgoingBehavior getOutgoingBehavior() {
		return (TaskOutgoingBehavior) super.getOutgoingBehavior();
	}

	/**
	 * Output sets of this activity instance.
	 * TODO move to activity, äh first introduce a model level
	 * @return
	 */
  public List<List<DataObject>> getOutputSets() {
    int nodeId = dbControlNodeInstance.getControlNodeId(getControlNodeInstanceId());
    List<Integer> outputSetIds = new DbDataFlow().getOutputSetsForControlNode(nodeId);
    return null;
  }

}