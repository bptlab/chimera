package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import java.util.Map;

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


public class HumanTaskExecutionBehavior extends TaskExecutionBehavior {

    public HumanTaskExecutionBehavior(int activityInstance_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance) {
        super(activityInstance_id, scenarioInstance, controlNodeInstance);
    }

    //diese Methode wird immer aufgerufen wenn eine Aktivität gestartet wird.
    //Hier können eventuell sonderfälle behandelt werden.
    @Override
    public void execute() {
        //darf erst true werden wenn alle attribute richtig gesetzt wurden.
        //denn erst dann kann die aktivität terminieren
        ((ActivityInstance)controlNodeInstance).setCanTerminate(true);
    }




    //TODO: dataAttributes
    //methoden zum setzen der attribute für die jeweilige Aktivität.
    //Wenn alle nötigen Attribute gesetzt wurden -> ((ActivityInstance)controlNodeInstance).setCanTerminate(true);((ActivityInstance)controlNodeInstance).setCanTerminate(true);
    //Dadurch ist Aktivität zum Terminieren aktiviert und kann auch erst dann(!!) terminieren.

    @Override
    public void setDataAttributeValues(Map<Integer, String> values){
        //diese Methode wird von der REST aufgerufen. Man bekommt eine Map
        //Key ist die ID der daten attribute instance und die value ist die value des daten attributes
    }
    public void setValue(int dataAttributeInstance_id, Object value){
        DataAttributeInstance dataAttributeInstance = scenarioInstance.getDataAttributeInstances().get(dataAttributeInstance_id);
        //nun kann auf das daten attribute zugegriffen werden ;)
    }


}
