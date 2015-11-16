package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * Represents the abstract control node.
 */
public abstract class AbstractControlNodeInstance {
	private AbstractOutgoingBehavior outgoingBehavior;
	private AbstractIncomingBehavior incomingBehavior;
	private AbstractStateMachine stateMachine;
	private int fragmentInstanceId;
	private int controlNodeInstanceId;
	private int controlNodeId;

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
	public AbstractOutgoingBehavior getOutgoingBehavior() {
		return outgoingBehavior;
	}

	/**
	 *
	 * @param outgoingBehavior an AbstractOutgoingBehavior
	 */
	public void setOutgoingBehavior(AbstractOutgoingBehavior outgoingBehavior) {
		this.outgoingBehavior = outgoingBehavior;
	}

	/**
	 * @return the incoming behavior.
	 */
	public AbstractIncomingBehavior getIncomingBehavior() {
		return incomingBehavior;
	}

	/**
	 *
	 * @param incomingBehavior an AbstractIncomingBehavior
	 */
	public void setIncomingBehavior(AbstractIncomingBehavior incomingBehavior) {
		this.incomingBehavior = incomingBehavior;
	}

	/**
	 * @return the state machine.
	 */
	public AbstractStateMachine getStateMachine() {
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
