package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataClass;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents data object instances.
 */
public class DataObject {

	private final int id;
    private int dataClassId;
	private int scenarioId;
	private int scenarioInstanceId;
	private String name;

	private ScenarioInstance scenarioInstance;
	private final DbDataObject dbDataObject = new DbDataObject();
	private int stateId;
	private List<DataAttributeInstance> dataAttributeInstances = new ArrayList<>();
    private boolean isLocked = false;

    /**
	 * Constructor to create a new DataObject.
	 *
	 * @param dataClassId       This is the database id from the data object instance.
	 * @param scenarioId         This is the database id from the scenario.
	 * @param scenarioInstanceId This is the database id from the scenario instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 */
	public DataObject(int dataClassId, int scenarioId, int scenarioInstanceId,
                      ScenarioInstance scenarioInstance) {
        this.id = dbDataObject.createDataObject(scenarioId, scenarioInstanceId, stateId, dataClassId);
        initialize(dataClassId, scenarioInstance);

	}

    /**
     * Use this constructor when initializing a data object which is already present in the
     * database.
     * @param dataObjectId database id of data object.
     */
    public DataObject (int dataObjectId, ScenarioInstance scenarioInstance) {
        DbDataObject dbDataObject = new DbDataObject();
        this.id = dataObjectId;
        this.scenarioInstance = scenarioInstance;
        dbDataObject.loadFromDb(this);
    }

    /**
     * Use this method to set values to a DataObject. This can either be done when
     * creating a new data object or when loading one from the database.
     * @param dataClassId Database Id of the dataclass, of which data object is instance
     */
    public void initialize(int dataClassId, ScenarioInstance scenarioInstance) {
        DbDataClass dataClass = new DbDataClass();
        this.name = dataClass.getName(dataClassId);
        this.scenarioInstance = new ScenarioInstance(scenarioId, scenarioInstanceId);
        this.dataClassId = dataClassId;
        this.scenarioId = scenarioInstance.getScenarioId();
        this.scenarioInstanceId = scenarioInstance.getScenarioInstanceId();
        this.initializeAttributes();
    }


	private void initializeAttributes() {
        DbDataClass dataClass = new DbDataClass();
        List<Integer> dataAttributeIds = dataClass
				.getDataAttributesForDataObject(dataClassId);
        for (int dataAttributeId : dataAttributeIds) {
			DataAttributeInstance dataAttributeInstance = new DataAttributeInstance(
					dataAttributeId, id, this);
            dataAttributeInstances.add(dataAttributeInstance);
            // TODO remove this method
			scenarioInstance.getDataAttributeInstances()
					.put(dataAttributeInstance.getDataAttributeInstanceId(),
							dataAttributeInstance);
		}
	}

	/**
	 * Sets the state for the data object instance in database and attribute.
	 *
	 * @param stateId This is the new state id.
	 */
	public void setState(int stateId) {
		this.stateId = stateId;
		dbDataObject.setState(id, stateId);
		scenarioInstance.checkTerminationCondition();
	}

	/**
	 * Getter.
	 * @return the Scenario Instance.
	 */
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	/**
	 * @return the ID of the Scenario Instance.
	 */
	public int getScenarioInstanceId() {
		return scenarioInstanceId;
	}

	/**
	 * @return the ID of the Scenario.
	 */
	public int getScenarioId() {
		return scenarioId;
	}

	/**
	 * @return the ID of the Data Object.
	 */
	public int getDataClassId() {
		return dataClassId;
	}

	/**
	 * @return the ID of this Data Object Instance.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the ID of the state.
	 */
	public int getStateId() {
		return stateId;
	}

	/**
	 * @return the Name of this Data Object Instance.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return a list containing the Data Attribute Instances belonging
	 * to this Data Object Instance.
	 */
	public List<DataAttributeInstance> getDataAttributeInstances() {
		return dataAttributeInstances;
	}

    public void lock() {
        this.dbDataObject.setLocked(this.id, true);
        this.isLocked = true;
    }

    public void unlock() {
        this.dbDataObject.setLocked(this.id, false);
        this.isLocked = false;
    }

    public boolean isLocked () {
        return this.isLocked;
    }


    public void setDataClassId(int dataClassId) {
        this.dataClassId = dataClassId;
    }
}
