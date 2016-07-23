package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.database.DbControlFlow;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;

/**
 * This class represents generic incoming behavior.
 */
public abstract class AbstractIncomingBehavior {
	private DbControlFlow dbControlFlow = new DbControlFlow();
	private ScenarioInstance scenarioInstance;
	private AbstractControlNodeInstance controlNodeInstance;

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
}
