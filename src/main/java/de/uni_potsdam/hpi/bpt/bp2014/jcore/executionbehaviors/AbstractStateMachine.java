package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;

/**
 * This is a abstract class for all state machines.
 */
public abstract class AbstractStateMachine {
	private ScenarioInstance scenarioInstance;
	private int controlNodeInstanceId;
	private AbstractControlNodeInstance controlNodeInstance;
	private STATE state;

    public enum STATE {
        DATAFLOW_ENABLED, CONTROLFLOW_ENABLED, READY, EXECUTING, TERMINATED,
        SKIPPED, INIT, RUNNING, CANCEL, REFERENTIAL_RUNNING
    }
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

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public void setScenarioInstance(ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	public int getControlNodeInstanceId() {
		return controlNodeInstanceId;
	}

	public void setControlNodeInstanceId(int controlNodeInstanceId) {
		this.controlNodeInstanceId = controlNodeInstanceId;
	}

	public AbstractControlNodeInstance getControlNodeInstance() {
		return controlNodeInstance;
	}

	public void setControlNodeInstance(AbstractControlNodeInstance controlNodeInstance) {
		this.controlNodeInstance = controlNodeInstance;
	}

	public STATE getState() {
		return state;
	}

	public void setState(STATE state) {
		this.state = state;
	}
}
