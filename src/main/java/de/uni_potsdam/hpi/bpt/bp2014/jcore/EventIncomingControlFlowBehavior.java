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

/**
 * Represents the incoming behavior of end events.
 */
public class EventIncomingControlFlowBehavior extends IncomingBehavior {
    private String type;

    /**
     * Creates and initializes an event incoming control flow behavior.
     * This behavior is only for an end event.
     *
     * @param controlNodeInstance This is an instance of the class ControlNodeInstance.
     * @param scenarioInstance    This is an instance of the class ScenarioInstance.
     * @param type                This is the type of the event.
     */
    public EventIncomingControlFlowBehavior(ControlNodeInstance controlNodeInstance, ScenarioInstance scenarioInstance, String type) {
        this.controlNodeInstance = controlNodeInstance;
        this.scenarioInstance = scenarioInstance;
        this.type = type;
    }

    @Override
    public void enableControlFlow() {
        scenarioInstance.restartFragment(this.controlNodeInstance.fragmentInstance_id);
    }

    /*
     * Getter
     */

    public String getType() {
        return type;
    }
}
