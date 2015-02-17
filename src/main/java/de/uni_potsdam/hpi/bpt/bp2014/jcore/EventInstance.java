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
 * This class represents events.
 * This engine supports only end and starts event. But only end events get represented as instance.
 * So this class represents an end event.
 */

public class EventInstance extends ControlNodeInstance {
    private ScenarioInstance scenarioInstance;
    private String type;
    //Only support Event is an End Event
    //Don't writes anything in the database

    /**
     * Creates and initializes a new event instance.
     *
     * @param type                This is the type of the event.
     * @param fragmentInstance_id This is the database id from the fragment instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     */
    public EventInstance(int fragmentInstance_id, ScenarioInstance scenarioInstance, String type) {
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        this.type = type;
        this.incomingBehavior = new EventIncomingControlFlowBehavior(this, scenarioInstance, type);
    }
}
