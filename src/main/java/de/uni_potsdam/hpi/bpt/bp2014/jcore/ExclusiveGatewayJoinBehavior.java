package de.uni_potsdam.hpi.bpt.bp2014.jcore;


/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */


public class ExclusiveGatewayJoinBehavior extends IncomingBehavior {

    /**
     * Initializes and creates an ExclusiveGatewayJoinBehavior
     *
     * @param gatewayInstance  An instance from the class GatewayInstance.
     * @param scenarioInstance An instance from the class ScenarioInstance.
     * @param stateMachine     An instance from the class StateMachine.
     */
    public ExclusiveGatewayJoinBehavior(GatewayInstance gatewayInstance, ScenarioInstance scenarioInstance, StateMachine stateMachine) {
        this.scenarioInstance = scenarioInstance;
        this.controlNodeInstance = gatewayInstance;
        this.stateMachine = stateMachine;
    }


    @Override
    public void enableControlFlow() {
        //TODO: check for conditions, if true -> terminate
        boolean conditions = true;
        if (conditions) {
            ((GatewayStateMachine) stateMachine).execute();
            ((ExclusiveGatewaySplitBehavior) controlNodeInstance.getOutgoingBehavior()).execute();
        }

    }
}
