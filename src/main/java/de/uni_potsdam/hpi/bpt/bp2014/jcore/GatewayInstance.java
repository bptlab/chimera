package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbGatewayInstance;

/**
 * Represents a gateway instance.
 */
public class GatewayInstance extends AbstractControlNodeInstance {
	/**
	 * Database Connection objects.
	 */
	private final DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
	private final DbControlNode dbControlNode = new DbControlNode();
	private final DbGatewayInstance dbGatewayInstance = new DbGatewayInstance();
	private boolean isXOR;
	private boolean isAND;
	private ScenarioInstance scenarioInstance;
	private boolean automaticExecution;
	private AbstractOutgoingBehavior outgoingBehavior;
	private AbstractIncomingBehavior incomingBehavior;
	private AbstractStateMachine stateMachine;
	private int fragmentInstanceId;
	private int controlNodeInstanceId;
	private int controlNodeId;

	/**
	 * Creates and initializes a new gateway instance.
	 * Creates a new entry in the database for the new gateway instance.
	 *
	 * @param controlNodeId      This is the id of the control node.
	 * @param fragmentInstanceId This is the id of the fragment instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 */
	public GatewayInstance(int controlNodeId, int fragmentInstanceId,
			ScenarioInstance scenarioInstance) {
		//looks if the Gateway Instance has already been initialized
		for (AbstractControlNodeInstance controlNodeInstance : scenarioInstance
				.getControlFlowEnabledControlNodeInstances()) {
			if (this.fragmentInstanceId == controlNodeInstanceId
					&& this.controlNodeId == controlNodeId) {
				//if it exist, only checks the control flow
				controlNodeInstance.enableControlFlow();
				return;
			}
		}
		this.automaticExecution = true;
		this.scenarioInstance = scenarioInstance;
		this.controlNodeId = controlNodeId;
		this.fragmentInstanceId = fragmentInstanceId;
		//scenarioInstance.getControlNodeInstances().add(this);
		switch (dbControlNode.getType(controlNodeId)) {
		case "AND":
			this.isAND = true;
			this.isXOR = false;
			break;
		case "XOR":
			this.isAND = false;
			this.isXOR = true;
			break;
		default:
		}
		//creates a new Gateway Instance also in database
		if (isAND) {
			this.controlNodeInstanceId = dbControlNodeInstance
					.createNewControlNodeInstance(
							controlNodeId, "AND", fragmentInstanceId);
		} else if (isXOR) {
			this.controlNodeInstanceId = dbControlNodeInstance
					.createNewControlNodeInstance(
							controlNodeId, "XOR", fragmentInstanceId);
		}
		if (isAND) {
			dbGatewayInstance.createNewGatewayInstance(
					controlNodeInstanceId, "AND", "init");
		} else if (isXOR) {
			dbGatewayInstance.createNewGatewayInstance(
					controlNodeInstanceId, "XOR", "init");
		}
		this.initGatewayInstance();
	}

	/**
	 * Creates and initializes a new gateway instance.
	 * Reads the information for an existing gateway instance from the database.
	 *
	 * @param controlNodeId      This is the id of the control node.
	 * @param fragmentInstanceId This is the id of the fragment instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param instanceId         This is an id of the gateway instance.
	 */
	public GatewayInstance(int controlNodeId, int fragmentInstanceId,
			ScenarioInstance scenarioInstance, int instanceId) {
		this.automaticExecution = true;
		this.scenarioInstance = scenarioInstance;
		this.controlNodeId = controlNodeId;
		this.fragmentInstanceId = fragmentInstanceId;
		//scenarioInstance.getControlNodeInstances().add(this);
		switch (dbControlNode.getType(controlNodeId)) {
		case "AND":
			this.isAND = true;
			this.isXOR = false;
			break;
		case "XOR":
			this.isAND = false;
			this.isXOR = true;
			break;
		default:
		}
		this.controlNodeInstanceId = instanceId;
		this.initGatewayInstance();
	}

	/**
	 * Initialize other information for the instance.
	 */
	private void initGatewayInstance() {
		this.stateMachine = new GatewayStateMachine(controlNodeId, scenarioInstance, this);
		if (isAND) {
			this.outgoingBehavior = new ParallelGatewaySplitBehavior(controlNodeId,
					scenarioInstance, fragmentInstanceId, this);
			this.incomingBehavior = new ParallelGatewayJoinBehavior(
					this, scenarioInstance);
		} else if (isXOR) {
			this.outgoingBehavior = new ExclusiveGatewaySplitBehavior(controlNodeId,
					scenarioInstance, fragmentInstanceId);
			this.incomingBehavior = new ExclusiveGatewayJoinBehavior(
					this, scenarioInstance, stateMachine);
		}
	}

	/**
	 * Checks if the gateway can terminate.
	 *
	 * @param controlNodeId A control node id.
	 * @return true if the gateway can terminate
	 */
	public boolean checkTermination(int controlNodeId) {
		return ((ExclusiveGatewaySplitBehavior) outgoingBehavior)
				.checkTermination(controlNodeId);
	}

	@Override public boolean terminate() {
		stateMachine.terminate();
		outgoingBehavior.terminate();
		return true;
	}

	@Override public boolean skip() {
		return stateMachine.skip();
	}

	// ******************************* Getter & Setter ***************************//

	/**
	 * @return boolean isXOR.
	 */
	public Boolean getIsXOR() {
		return isXOR;
	}

	/**
	 * @return boolean isAND.
	 */
	public Boolean getIsAND() {
		return isAND;
	}

	/**
	 * @return ScenarioInstance.
	 */
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	/**
	 * @return boolean isAutomaticExecution.
	 */
	public boolean isAutomaticExecution() {
		return automaticExecution;
	}

	/**
	 * @param automaticExecution a automatic execution state.
	 */
	public void setAutomaticExecution(boolean automaticExecution) {
		this.automaticExecution = automaticExecution;
	}
}
