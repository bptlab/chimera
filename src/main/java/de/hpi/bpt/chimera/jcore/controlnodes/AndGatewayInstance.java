package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.executionbehaviors.GatewayStateMachine;
import de.hpi.bpt.chimera.jcore.flowbehaviors.ParallelGatewayJoinBehavior;
import de.hpi.bpt.chimera.jcore.flowbehaviors.ParallelGatewaySplitBehavior;

/**
 * Represents AND join and AND merge, by having a parallel join incoming behaviour and
 * an parallel split outgoing behaviour.
 */
public class AndGatewayInstance extends GatewayInstance {
    /**
     * Creates a new AndGatewayInstance and saves it to the database.
     * @param controlNodeId the id of the GatewayModel.
     * @param fragmentInstanceId the id of the fragment the gateway belongs to.
     * @param scenarioInstance the ScenarioInstance object to which the instance belongs to.
     */
    public AndGatewayInstance(int controlNodeId, int fragmentInstanceId,
                              ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
        this.type = GatewayType.AND;
        this.setControlNodeInstanceId(dbControlNodeInstance
                .createNewControlNodeInstance(controlNodeId, "AND", fragmentInstanceId));
        this.dbGatewayInstance.createNewGatewayInstance(
                getControlNodeInstanceId(), "AND", "init");
        this.setStateMachine(new GatewayStateMachine(this.getControlNodeId(),
                this.scenarioInstance, this));
        this.initGatewayInstance();
    }

    /**
     * Loads an existing AndGatewayInstance from the database.
     *
     * @param controlNodeId the id of the GatewayModel.
     * @param fragmentInstanceId the id of the fragment the gateway belongs to.
     * @param scenarioInstance the ScenarioInstance object to which the instance belongs to.
     * @param instanceId the id of the existing AndGatewayInstance in the `contronodeinstance`
     *                   table.
     */
    public AndGatewayInstance(int controlNodeId, int fragmentInstanceId,
                              ScenarioInstance scenarioInstance, int instanceId) {
        super(controlNodeId, fragmentInstanceId,scenarioInstance, instanceId);
        this.type = GatewayType.AND;
        this.initGatewayInstance();
    }

    private void initGatewayInstance() {
        this.setOutgoingBehavior(new ParallelGatewaySplitBehavior(
                getControlNodeId(), scenarioInstance,
                getFragmentInstanceId(), this));
        this.setIncomingBehavior(new ParallelGatewayJoinBehavior(
                this, scenarioInstance));
    }
}
