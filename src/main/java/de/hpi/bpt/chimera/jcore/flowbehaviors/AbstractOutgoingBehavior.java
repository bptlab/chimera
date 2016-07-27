package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.*;
import de.hpi.bpt.chimera.database.DbControlFlow;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNode;
import de.hpi.bpt.chimera.database.history.DbLogEntry;

/**
 * This class represents generic outgoing behavior.
 */
public abstract class AbstractOutgoingBehavior {
	private final DbControlFlow dbControlFlow = new DbControlFlow();
	private final DbControlNode dbControlNode = new DbControlNode();
	private ScenarioInstance scenarioInstance;
	private int controlNodeId;
	private int fragmentInstanceId;

	/**
	 * Terminates the control node instance.
	 */
	public abstract void terminate();


    public abstract void skip();

	/**
	 * Runs other methods after terminate a control node instance.
	 */
	public void runAutomaticTasks() {
		scenarioInstance.startAutomaticControlNodes();
	}

	public DbControlFlow getDbControlFlow() {
		return dbControlFlow;
	}

	public DbControlNode getDbControlNode() {
		return dbControlNode;
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public int getControlNodeId() {
		return controlNodeId;
	}

	public int getFragmentInstanceId() {
		return fragmentInstanceId;
	}

	public void setScenarioInstance(ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	public void setControlNodeId(int controlNodeId) {
		this.controlNodeId = controlNodeId;
	}

	public void setFragmentInstanceId(int fragmentInstanceId) {
		this.fragmentInstanceId = fragmentInstanceId;
	}
}
