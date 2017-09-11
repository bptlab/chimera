package de.hpi.bpt.chimera.execution;

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
