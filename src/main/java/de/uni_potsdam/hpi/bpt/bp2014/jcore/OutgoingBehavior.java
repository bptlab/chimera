package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;

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


public abstract class OutgoingBehavior {
    protected final DbControlFlow dbControlFlow = new DbControlFlow();
    protected final DbControlNode dbControlNode = new DbControlNode();
    protected ScenarioInstance scenarioInstance;
    protected int controlNode_id;
    protected int fragmentInstance_id;

    /**
     * Terminates the control node instance.
     */
    public abstract void terminate();

    /**
     * Checks conditions after terminate a control node instance
     */
    public void checkAfterTermination() {
        scenarioInstance.checkDataFlowEnabled();
        scenarioInstance.checkExecutingGateways(controlNode_id);
    }

    /**
     * Runs other methods after terminate a control node instance
     */
    public void runAfterTermination() {
        scenarioInstance.startAutomaticControlNodes();
    }

    /**
     *
     * @param type
     * @param id
     * @return
     */
    protected ControlNodeInstance createControlNode(String type, int id) {
        ControlNodeInstance controlNodeInstance = null;
        //TODO: type
        switch (type) {
            case "Activity":
            case "EmailTask":
            case "WebServiceTask":
                controlNodeInstance = new ActivityInstance(id, fragmentInstance_id, scenarioInstance, -1);
                break;
            case "Endevent":
                controlNodeInstance = new EventInstance(fragmentInstance_id, scenarioInstance, "Endevent");
                break;
            case "XOR":
            case "AND":
                controlNodeInstance = new GatewayInstance(id, fragmentInstance_id, scenarioInstance);
                break;
        }
        return controlNodeInstance;
    }

}
