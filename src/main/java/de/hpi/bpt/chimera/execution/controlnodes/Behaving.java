package de.hpi.bpt.chimera.execution.controlnodes;

public interface Behaving {
	/**
	 * IncomingBehaviour
	 */
	public void enableControlFlow();

	/**
	 * ExecutionBehaviour
	 */
	public void begin();

	/**
	 * OutgoingBehaviour
	 */
	public void terminate();

	public void skip();
}
