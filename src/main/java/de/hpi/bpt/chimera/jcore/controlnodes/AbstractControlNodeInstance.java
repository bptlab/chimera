package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.executionbehaviors.AbstractExecutionBehavior;
import de.hpi.bpt.chimera.jcore.flowbehaviors.AbstractIncomingBehavior;
import de.hpi.bpt.chimera.jcore.flowbehaviors.AbstractOutgoingBehavior;

/**
 * Represents the abstract control node. Each control node has a state, an incoming and outgoing
 * behavior.
 */
public abstract class AbstractControlNodeInstance {
	protected ScenarioInstance scenarioInstance;
	protected State state;
	private AbstractOutgoingBehavior outgoingBehavior;
	private AbstractIncomingBehavior incomingBehavior;
	private AbstractExecutionBehavior executionBehavior;
	private int fragmentInstanceId;
	private int controlNodeInstanceId;
	private int controlNodeId;

	public void enableControlFlow() {
		incomingBehavior.enableControlFlow();
	}


	/**
	 * Skips the control node.
	 */
	public void skip() {
		this.getOutgoingBehavior().skip();
	}

	;

	/**
	 * Terminates the control node.
	 *
	 * @return true if the skip success. false if not.
	 */
	public void terminate() {
		this.outgoingBehavior.terminate();
	}

	public void begin() {
		this.getExecutionBehavior().begin();
	}

	// ********************* Getter/Setter *********************//

	public AbstractOutgoingBehavior getOutgoingBehavior() {
		return outgoingBehavior;
	}

	public void setOutgoingBehavior(AbstractOutgoingBehavior outgoingBehavior) {
		this.outgoingBehavior = outgoingBehavior;
	}

	public AbstractIncomingBehavior getIncomingBehavior() {
		return incomingBehavior;
	}

	public void setIncomingBehavior(AbstractIncomingBehavior incomingBehavior) {
		this.incomingBehavior = incomingBehavior;
	}


	public int getFragmentInstanceId() {
		return fragmentInstanceId;
	}

	public void setFragmentInstanceId(int fragmentInstanceId) {
		this.fragmentInstanceId = fragmentInstanceId;
	}

	public int getControlNodeInstanceId() {
		return controlNodeInstanceId;
	}

	public void setControlNodeInstanceId(int controlNodeInstanceId) {
		this.controlNodeInstanceId = controlNodeInstanceId;
	}

	public int getControlNodeId() {
		return controlNodeId;
	}

	public void setControlNodeId(int controlNodeId) {
		this.controlNodeId = controlNodeId;
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public void setScenarioInstance(ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
		dbControlNodeInstance.setState(state, this.controlNodeInstanceId);
		this.state = state;
	}

	public AbstractExecutionBehavior getExecutionBehavior() {
		return executionBehavior;
	}

	public void setExecutionBehavior(AbstractExecutionBehavior executionBehavior) {
		this.executionBehavior = executionBehavior;
	}
}
