package de.hpi.bpt.chimera.execution;

public interface ControlNodeInstance {
	/**
	 * Skips the control node.
	 */
	public void skip();

	/**
	 * Terminates the control node.
	 */
	public void terminate();

	public void begin();
}
