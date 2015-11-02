package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * Represents the abstract control node.
 */
public abstract class ControlNodeInstance {
	OutgoingBehavior outgoingBehavior;
	IncomingBehavior incomingBehavior;
	StateMachine stateMachine;
	int fragmentInstanceId;
	int controlNodeInstanceId;
	int controlNodeId;

	/**
	 * Enables the control flow.
	 */
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
	// *************************************** Getter ***************************************//

	/**
	 * @return the outgoing behavior.
	 */
	public OutgoingBehavior getOutgoingBehavior() {
		return outgoingBehavior;
	}

	/**
	 * @return the incoming behavior.
	 */
	public IncomingBehavior getIncomingBehavior() {
		return incomingBehavior;
	}

	/**
	 * @return the state machine.
	 */
	public StateMachine getStateMachine() {
		return stateMachine;
	}

	/**
	 * @return the fragment instance id.
	 */
	public int getFragmentInstanceId() {
		return fragmentInstanceId;
	}

	/**
	 * @return the control node instance id.
	 */
	public int getControlNodeInstanceId() {
		return controlNodeInstanceId;
	}

	/**
	 * @return the control node id.
	 */
	public int getControlNodeId() {
		return controlNodeId;
	}
}
