package de.hpi.bpt.chimera.jcore.data;

import de.hpi.bpt.chimera.database.data.DbDataAttributeInstance;
import de.hpi.bpt.chimera.database.data.DbDataClass;
import de.hpi.bpt.chimera.database.data.DbDataObject;
import de.hpi.bpt.chimera.database.data.DbState;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents data object instances.
 */
public class DataObject {

	private final int id;
	private final DbDataObject dbDataObject = new DbDataObject();
	private int dataClassId;
	private int scenarioId;
	private int scenarioInstanceId;
	private String name;
	private ScenarioInstance scenarioInstance;
	private int stateId;
	private Map<Integer, DataAttributeInstance> dataAttributeInstanceMap = new HashMap<>();
	private boolean isLocked = false;

	/**
	 * Constructor to create a new DataObject.
	 *
	 * @param dataClassId      This is the database id from the data class.
	 * @param stateId          This is the database id from the scenario.
	 * @param scenarioInstance This is an instance from the  class ScenarioInstance.
	 */
	public DataObject(int dataClassId, ScenarioInstance scenarioInstance, int stateId) {
		this.scenarioInstance = scenarioInstance;
		this.dataClassId = dataClassId;
		this.scenarioId = scenarioInstance.getScenarioId();
		this.scenarioInstanceId = scenarioInstance.getId();

		this.id = dbDataObject.createDataObject(scenarioId, scenarioInstanceId, stateId, dataClassId);
		DbDataClass dataClass = new DbDataClass();

		this.name = dataClass.getName(dataClassId);
		this.initializeAttributes();
		this.stateId = stateId;
	}

	/**
	 * Use this constructor when initializing a data object which is already present in the
	 * database.
	 *
	 * @param dataObjectId database id of data object.
	 */
	public DataObject(int dataObjectId, ScenarioInstance scenarioInstance) {
		this.id = dataObjectId;
		this.scenarioInstance = scenarioInstance;
		dbDataObject.loadFromDb(this);
		// Load data attribute instances from database
		DbDataAttributeInstance dbDataAttributeInstance = new DbDataAttributeInstance();
		List<Integer> datattributeInstances = dbDataAttributeInstance.getAttributeInstanceIdsForDataObject(dataObjectId);
		for (Integer dataAttributeInstanceId : datattributeInstances) {
			this.dataAttributeInstanceMap.put(dataAttributeInstanceId, new DataAttributeInstance(dataAttributeInstanceId, this));
		}
	}

	private void initializeAttributes() {
		DbDataClass dataClass = new DbDataClass();
		List<Integer> dataAttributeIds = dataClass.getDataAttributes(dataClassId);

		for (int dataAttributeId : dataAttributeIds) {
			DataAttributeInstance dataAttributeInstance = new DataAttributeInstance(dataAttributeId, id, this);
			dataAttributeInstanceMap.put(dataAttributeInstance.getId(), dataAttributeInstance);
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
	}

	/**
	 * Getter.
	 *
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

	public void setDataClassId(int dataClassId) {
		this.dataClassId = dataClassId;
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

	public String getStateName() {
		return new DbState().getStateName(this.stateId);
	}

	/**
	 * @return the Name of this Data Object Instance.
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return a list containing the Data Attribute Instances belonging
	 * to this Data Object Instance.
	 */
	public List<DataAttributeInstance> getDataAttributeInstances() {
		return new ArrayList<>(dataAttributeInstanceMap.values());
	}

	public Map<Integer, DataAttributeInstance> getDataAttributeInstanceMap() {
		return this.dataAttributeInstanceMap;
	}

	public void lock() {
		this.dbDataObject.setLocked(this.id, true);
		this.isLocked = true;
	}

	public void unlock() {
		this.dbDataObject.setLocked(this.id, false);
		this.isLocked = false;
	}

	public boolean isLocked() {
		return this.isLocked;
	}
}
