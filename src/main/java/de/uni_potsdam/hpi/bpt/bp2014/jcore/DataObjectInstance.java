package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObjectInstance;

import java.util.LinkedList;


/**
 * Represents data object instances.
 */
public class DataObjectInstance {

    private final int dataObjectInstance_id;
    private final int dataObject_id;
    private final int scenario_id;
    private final int scenarioInstance_id;
    private final String name;
    /**
     * Database Connection objects.
     */
    private final ScenarioInstance scenarioInstance;
    private final DbDataObjectInstance dbDataObjectInstance = new DbDataObjectInstance();
    private final DbDataObject dbDataObject = new DbDataObject();
    private int state_id;
    private LinkedList<DataAttributeInstance> dataAttributeInstances = new LinkedList<>();

    /**
     * Creates and initializes a new data object instance.
     * Reads the information for an existing data object instance from the database or creates a new one if no one
     * exist in the database.
     *
     * @param dataObject_id       This is the database id from the data object instance.
     * @param scenario_id         This is the database id from the scenario.
     * @param scenarioInstance_id This is the database id from the scenario instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     */
    public DataObjectInstance(int dataObject_id, int scenario_id, int scenarioInstance_id, ScenarioInstance scenarioInstance) {
        this.scenarioInstance = scenarioInstance;
        this.dataObject_id = dataObject_id;
        this.scenario_id = scenario_id;
        this.scenarioInstance_id = scenarioInstance_id;
        this.name = dbDataObject.getName(dataObject_id);
        if (dbDataObjectInstance.existDataObjectInstance(scenarioInstance_id, dataObject_id)) {
            //creates an existing DataObject Instance using the database information
            dataObjectInstance_id = dbDataObjectInstance.getDataObjectInstanceID(scenarioInstance_id, dataObject_id);
            state_id = dbDataObjectInstance.getStateID(dataObjectInstance_id);
        } else {
            //creates a new DataObject Instance also in database
            state_id = dbDataObject.getStartStateID(dataObject_id);
            this.dataObjectInstance_id = dbDataObjectInstance.createNewDataObjectInstance(scenarioInstance_id, state_id, dataObject_id);
        }
        this.initializeAttributes();
    }

    private void initializeAttributes() {
        LinkedList<Integer> dataAttribute_ids = dbDataObject.getAllDataAttributesForDataObject(dataObject_id);
        for (int dataAttribute_id : dataAttribute_ids) {
            DataAttributeInstance dataAttributeInstance = new DataAttributeInstance(dataAttribute_id, dataObjectInstance_id, this);
            dataAttributeInstances.add(dataAttributeInstance);
            scenarioInstance.getDataAttributeInstances().put(dataAttributeInstance.getDataAttributeInstance_id(), dataAttributeInstance);
        }
    }

    /**
     * Sets the state for the data object instance in database and attribute.
     *
     * @param state_id This is the new state id.
     */
    public void setState(int state_id) {
        this.state_id = state_id;
        dbDataObjectInstance.setState(dataObjectInstance_id, state_id);
        scenarioInstance.checkTerminationCondition();
    }

    /**
     * Checks if the data object get edited right now.
     *
     * @return true if the data object get edited right now. false if not.
     */
    public boolean getOnChange() {
        return dbDataObjectInstance.getOnChange(dataObjectInstance_id);
    }

    /**
     * Sets on change from the data object instance.
     *
     * @param onChange This the value on change get set to.
     */
    public void setOnChange(boolean onChange) {
        dbDataObjectInstance.setOnChange(dataObjectInstance_id, onChange);
    }

    /**
     * Getter
     */
    public ScenarioInstance getScenarioInstance() {
        return scenarioInstance;
    }

    /**
     * @return
     */
    public int getScenarioInstance_id() {
        return scenarioInstance_id;
    }

    /**
     * @return
     */
    public int getScenario_id() {
        return scenario_id;
    }

    /**
     * @return
     */
    public int getDataObject_id() {
        return dataObject_id;
    }

    /**
     * @return
     */
    public int getDataObjectInstance_id() {
        return dataObjectInstance_id;
    }

    /**
     * @return
     */
    public int getState_id() {
        return state_id;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public LinkedList<DataAttributeInstance> getDataAttributeInstances() {
        return dataAttributeInstances;
    }

}
