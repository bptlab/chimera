package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.AbstractStateMachine;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.AbstractIncomingBehavior;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.AbstractOutgoingBehavior;

/**
 * Represents the abstract control node.
 */
public abstract class AbstractControlNodeInstance {

	protected ScenarioInstance scenarioInstance;
	private AbstractOutgoingBehavior outgoingBehavior;
	private AbstractIncomingBehavior incomingBehavior;
	private AbstractStateMachine stateMachine;
	private int fragmentInstanceId;
	private int controlNodeInstanceId;
	private int controlNodeId;

	public void enableControlFlow() {
		incomingBehavior.enableControlFlow();
    }

	/**
	 * Skips the control node.
	 *
	 * @return true if the skip success. false if not.
	 */
	public abstract boolean skip();

	/**
	 * Terminates the control node.
	 *
	 * @return true if the skip success. false if not.
	 */
	public abstract boolean terminate();

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

	public AbstractStateMachine getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(AbstractStateMachine stateMachine) {
		this.stateMachine = stateMachine;
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

}
