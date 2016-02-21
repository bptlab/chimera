package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbGatewayInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.ExclusiveGatewayJoinBehavior;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.ExclusiveGatewaySplitBehavior;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.ParallelGatewayJoinBehavior;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.ParallelGatewaySplitBehavior;

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
	private boolean automaticExecution;

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
			if (this.getFragmentInstanceId() == getControlNodeInstanceId()
					&& this.getControlNodeId() == controlNodeId) {
				//if it exist, only checks the control flow
				controlNodeInstance.enableControlFlow();
				return;
			}
		}
		this.automaticExecution = true;
		this.scenarioInstance = scenarioInstance;
		this.setControlNodeId(controlNodeId);
		this.setFragmentInstanceId(fragmentInstanceId);
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
			break;
		}
		//creates a new Gateway Instance also in database
		if (isAND) {
			this.setControlNodeInstanceId(dbControlNodeInstance
					.createNewControlNodeInstance(
							controlNodeId, "AND", fragmentInstanceId));
		} else if (isXOR) {
			this.setControlNodeInstanceId(dbControlNodeInstance
					.createNewControlNodeInstance(
							controlNodeId, "XOR", fragmentInstanceId));
		}
		if (isAND) {
			dbGatewayInstance.createNewGatewayInstance(
					getControlNodeInstanceId(), "AND", "init");
		} else if (isXOR) {
			dbGatewayInstance.createNewGatewayInstance(
					getControlNodeInstanceId(), "XOR", "init");
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
		this.setControlNodeId(controlNodeId);
		this.setFragmentInstanceId(fragmentInstanceId);
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
		default: break;
		}
		this.setControlNodeInstanceId(instanceId);
		this.initGatewayInstance();
	}

	/**
	 * Initialize other information for the instance.
	 */
	private void initGatewayInstance() {
		this.setStateMachine(new GatewayStateMachine(
				getControlNodeId(), scenarioInstance, this));
		if (isAND) {
			this.setOutgoingBehavior(new ParallelGatewaySplitBehavior(
					getControlNodeId(), scenarioInstance,
					getFragmentInstanceId(), this));
			this.setIncomingBehavior(new ParallelGatewayJoinBehavior(
					this, scenarioInstance));
		} else if (isXOR) {
			this.setOutgoingBehavior(new ExclusiveGatewaySplitBehavior(
					getControlNodeId(), scenarioInstance,
					getFragmentInstanceId()));
			this.setIncomingBehavior(new ExclusiveGatewayJoinBehavior(
					this, scenarioInstance, getStateMachine()));
		}
	}

	/**
	 * Checks if the gateway can terminate.
	 *
	 * @param controlNodeId A control node id.
	 * @return true if the gateway can terminate
	 */
	public boolean checkTermination(int controlNodeId) {
		return ((ExclusiveGatewaySplitBehavior) getOutgoingBehavior())
				.checkTermination(controlNodeId);
	}

	@Override public boolean terminate() {
		getStateMachine().terminate();
		getOutgoingBehavior().terminate();
		return true;
	}

	@Override public boolean skip() {
		return getStateMachine().skip();
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
