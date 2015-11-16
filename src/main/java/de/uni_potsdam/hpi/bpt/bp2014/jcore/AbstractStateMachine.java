package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * This is a abstract class for all state machines.
 */
public abstract class AbstractStateMachine {
	private ScenarioInstance scenarioInstance;
	private int controlNodeInstanceId;
	private AbstractControlNodeInstance controlNodeInstance;
	private String state;

	/**
	 *
	 * @return true if successfully skipped.
	 */
	public abstract boolean skip();

	/**
	 *
	 * @return true if successfully terminated.
	 */
	public abstract boolean terminate();

	public void setControlNodeInstanceId(int controlNodeInstanceId) {
		this.controlNodeInstanceId = controlNodeInstanceId;
	}

	public String getState() {
		return state;
	}
}
