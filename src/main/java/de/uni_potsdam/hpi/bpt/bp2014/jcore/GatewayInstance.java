package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbGatewayInstance;


public class GatewayInstance extends ControlNodeInstance {
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

    /**
     * Creates and initializes a new gateway instance.
     * Creates a new entry in the database for the new gateway instance.
     *
     * @param controlNode_id      This is the id of the control node.
     * @param fragmentInstance_id This is the id of the fragment instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     */
    public GatewayInstance(int controlNode_id, int fragmentInstance_id, ScenarioInstance scenarioInstance) {
        //looks if the Gateway Instance has already been initialized
        for (ControlNodeInstance controlNodeInstance : scenarioInstance.getControlFlowEnabledControlNodeInstances()) {
            if (controlNodeInstance.fragmentInstance_id == controlNodeInstance_id && controlNodeInstance.controlNode_id == controlNode_id) {
                //if it exist, only checks the control flow
                controlNodeInstance.enableControlFlow();
                return;
            }
        }
        this.automaticExecution = true;
        this.scenarioInstance = scenarioInstance;
        this.controlNode_id = controlNode_id;
        this.fragmentInstance_id = fragmentInstance_id;
        //scenarioInstance.getControlNodeInstances().add(this);
        switch (dbControlNode.getType(controlNode_id)) {
            case "AND":
                this.isAND = true;
                this.isXOR = false;
                break;
            case "XOR":
                this.isAND = false;
                this.isXOR = true;
                break;
        }
        //creates a new Gateway Instance also in database
        if (isAND) {
            this.controlNodeInstance_id = dbControlNodeInstance.createNewControlNodeInstance(controlNode_id, "AND", fragmentInstance_id);
        } else if (isXOR) {
            this.controlNodeInstance_id = dbControlNodeInstance.createNewControlNodeInstance(controlNode_id, "XOR", fragmentInstance_id);
        }
        if (isAND) {
            dbGatewayInstance.createNewGatewayInstance(controlNodeInstance_id, "AND", "init");
        } else if (isXOR) {
            dbGatewayInstance.createNewGatewayInstance(controlNodeInstance_id, "XOR", "init");
        }
        this.initGatewayInstance();
    }

    /**
     * Creates and initializes a new gateway instance.
     * Reads the information for an existing gateway instance from the database.
     *
     * @param controlNode_id      This is the id of the control node.
     * @param fragmentInstance_id This is the id of the fragment instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param instance_id         This is an id of the gateway instance.
     */
    public GatewayInstance(int controlNode_id, int fragmentInstance_id, ScenarioInstance scenarioInstance, int instance_id) {
        this.automaticExecution = true;
        this.scenarioInstance = scenarioInstance;
        this.controlNode_id = controlNode_id;
        this.fragmentInstance_id = fragmentInstance_id;
        //scenarioInstance.getControlNodeInstances().add(this);
        switch (dbControlNode.getType(controlNode_id)) {
            case "AND":
                this.isAND = true;
                this.isXOR = false;
                break;
            case "XOR":
                this.isAND = false;
                this.isXOR = true;
                break;
        }
        this.controlNodeInstance_id = instance_id;
        this.initGatewayInstance();
    }

    /**
     * Initialize other information for the instance.
     */
    private void initGatewayInstance() {
        this.stateMachine = new GatewayStateMachine(controlNode_id, scenarioInstance, this);
        if (isAND) {
            this.outgoingBehavior = new ParallelGatewaySplitBehavior(controlNode_id, scenarioInstance, fragmentInstance_id, this);
            this.incomingBehavior = new ParallelGatewayJoinBehavior(this, scenarioInstance);
        } else if (isXOR) {
            this.outgoingBehavior = new ExclusiveGatewaySplitBehavior(controlNode_id, scenarioInstance, fragmentInstance_id);
            this.incomingBehavior = new ExclusiveGatewayJoinBehavior(this, scenarioInstance, stateMachine);
        }
    }

    /**
     * Checks if the gateway can terminate.
     *
     * @param controlNode_id A control node id.
     * @return true if the gateway can terminate
     */
    public boolean checkTermination(int controlNode_id) {
        return ((ExclusiveGatewaySplitBehavior) outgoingBehavior).checkTermination(controlNode_id);
    }

    @Override
    public boolean terminate() {
        stateMachine.terminate();
        outgoingBehavior.terminate();
        return true;
    }

    @Override
    public boolean skip() {
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
     * @param automaticExecution
     */
    public void setAutomaticExecution(boolean automaticExecution) {
        this.automaticExecution = automaticExecution;
    }
}
