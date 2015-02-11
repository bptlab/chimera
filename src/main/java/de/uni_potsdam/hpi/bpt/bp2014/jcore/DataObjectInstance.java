package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObjectInstance;


/***********************************************************************************
*   
*   _________ _______  _        _______ _________ _        _______ 
*   \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
*      )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
*      |  |  | (__    |   \ | || |         | |   |   \ | || (__    
*      |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)   
*      |  |  | (      | | \   || | \_  )   | |   | | \   || (      
*   |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
*   (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
*
*******************************************************************
*
*   Copyright © All Rights Reserved 2014 - 2015
*
*   Please be aware of the License. You may found it in the root directory.
*
************************************************************************************/



public class DataObjectInstance {
    private int state_id;
    private int dataObjectInstance_id;
    private int dataObject_id;
    private int scenario_id;
    private int scenarioInstance_id;
    //Database Connection objects
    private ScenarioInstance scenarioInstance;
    private DbDataObjectInstance dbDataObjectInstance = new DbDataObjectInstance();
    private DbDataObject dbDataObject = new DbDataObject();

    public DataObjectInstance(int dataObject_id, int scenario_id, int scenarioInstance_id, ScenarioInstance scenarioInstance){
        this.scenarioInstance = scenarioInstance;
        this.dataObject_id = dataObject_id;
        this.scenario_id = scenario_id;
        this.scenarioInstance_id = scenarioInstance_id;
        if(dbDataObjectInstance.existDataObjectInstance(scenarioInstance_id, dataObject_id)){
            //creates an existing DataObject Instance using the database information
            dataObjectInstance_id = dbDataObjectInstance.getDataObjectInstanceID(scenarioInstance_id, dataObject_id);
            state_id = dbDataObjectInstance.getStateID(dataObjectInstance_id);
        }else{
            //creates a new DataObject Instance also in database
            state_id = dbDataObject.getStartStateID(dataObject_id);
            this. dataObjectInstance_id = dbDataObjectInstance.createNewDataObjectInstance(scenarioInstance_id, state_id, dataObject_id);
        }
    }

    public void setState(int state_id){
        this.state_id = state_id;
        dbDataObjectInstance.setState(dataObjectInstance_id, state_id);
        scenarioInstance.checkTerminationCondition();
    }
    public Boolean getOnChange(){
        return dbDataObjectInstance.getOnChange(dataObjectInstance_id);
    }

    public void setOnChange(Boolean onChange){
        dbDataObjectInstance.setOnChange(dataObjectInstance_id, onChange);
    }

    /*
     * Getter
     */
    public ScenarioInstance getScenarioInstance() {
        return scenarioInstance;
    }

    public int getScenarioInstance_id() {
        return scenarioInstance_id;
    }

    public int getScenario_id() {
        return scenario_id;
    }

    public int getDataObject_id() {
        return dataObject_id;
    }

    public int getDataObjectInstance_id() {
        return dataObjectInstance_id;
    }

    public int getState_id() {
        return state_id;
    }
}
