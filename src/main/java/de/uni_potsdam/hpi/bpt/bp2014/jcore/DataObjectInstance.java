package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObjectInstance;

public class DataObjectInstance {
    public int state_id;
    public int dataObjectInstance_id;
    public int dataObject_id;
    public int scenario_id;
    public int scenarioInstance_id;
    private DbDataObjectInstance dbDataObjectInstance = new DbDataObjectInstance();
    private DbDataObject dbDataObject = new DbDataObject();

    public DataObjectInstance(int dataObject_id, int scenario_id, int scenarioInstance_id){
        this.dataObject_id = dataObject_id;
        this.scenario_id = scenario_id;
        this.scenarioInstance_id = scenarioInstance_id;
        if(dbDataObjectInstance.existDataObjectInstance(scenarioInstance_id, dataObject_id)){
            dataObjectInstance_id = dbDataObjectInstance.getDataObjectInstanceID(scenarioInstance_id, dataObject_id);
            state_id = dbDataObjectInstance.getStateID(dataObjectInstance_id);
        }else{
            state_id = dbDataObject.getStartStateID(dataObject_id);
            dbDataObjectInstance.createNewDataObjectInstance(scenarioInstance_id, state_id, dataObject_id);
            dataObjectInstance_id = dbDataObjectInstance.getDataObjectInstanceID(scenarioInstance_id, dataObject_id);
        }
    }

    public void setState(int state_id){
        this.state_id = state_id;
        dbDataObjectInstance.setState(dataObjectInstance_id, state_id);
    }
}
