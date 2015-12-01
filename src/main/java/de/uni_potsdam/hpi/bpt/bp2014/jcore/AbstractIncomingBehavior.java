package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;

/**
 * This class represents generic incoming behavior.
 */
public abstract class AbstractIncomingBehavior {
	private DbControlFlow dbControlFlow = new DbControlFlow();
	private ScenarioInstance scenarioInstance;
	private AbstractControlNodeInstance controlNodeInstance;
	private AbstractStateMachine stateMachine;

	/**
	 * Enable the control flow for the control node instance.
	 */
	public abstract void enableControlFlow();

	public DbControlFlow getDbControlFlow() {
		return dbControlFlow;
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public void setScenarioInstance(ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	public AbstractControlNodeInstance getControlNodeInstance() {
		return controlNodeInstance;
	}

	public void setControlNodeInstance(AbstractControlNodeInstance controlNodeInstance) {
		this.controlNodeInstance = controlNodeInstance;
	}

	public AbstractStateMachine getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(AbstractStateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
}
