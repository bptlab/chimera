package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObjectInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryLogger;

import java.util.LinkedList;

/**
 * Represents data object instances.
 */
public class DataObjectInstance {

	private final int dataObjectInstanceId;
	private final int dataObjectId;
	private final int scenarioId;
	private final int scenarioInstanceId;
	private final String name;
	/**
	 * Database Connection objects.
	 */
	private final ScenarioInstance scenarioInstance;
	private final DbDataObjectInstance dbDataObjectInstance = new DbDataObjectInstance();
	private final DbDataObject dbDataObject = new DbDataObject();
	private int stateId;
	private LinkedList<DataAttributeInstance> dataAttributeInstances = new LinkedList<>();

	/**
	 * Creates and initializes a new data object instance.
	 * Reads the information for an existing data object instance from the database
	 * or creates a new one if no one exist in the database.
	 *
	 * @param dataObjectId       This is the database id from the data object instance.
	 * @param scenarioId         This is the database id from the scenario.
	 * @param scenarioInstanceId This is the database id from the scenario instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 */
	public DataObjectInstance(int dataObjectId, int scenarioId, int scenarioInstanceId,
			ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		this.dataObjectId = dataObjectId;
		this.scenarioId = scenarioId;
		this.scenarioInstanceId = scenarioInstanceId;
		this.name = dbDataObject.getName(dataObjectId);
		if (dbDataObjectInstance
				.existDataObjectInstance(scenarioInstanceId, dataObjectId)) {
			//creates an existing DataObject Instance using the database information
			dataObjectInstanceId = dbDataObjectInstance
					.getDataObjectInstanceID(scenarioInstanceId, dataObjectId);
			stateId = dbDataObjectInstance.getStateID(dataObjectInstanceId);
		} else {
			//creates a new DataObject Instance also in database
			stateId = dbDataObject.getStartStateID(dataObjectId);
			this.dataObjectInstanceId = dbDataObjectInstance
					.createNewDataObjectInstance(
							scenarioInstanceId,
							stateId,
							dataObjectId);
		}
		this.initializeAttributes();
	}

	private void initializeAttributes() {
		LinkedList<Integer> dataAttributeIds = dbDataObject
				.getAllDataAttributesForDataObject(dataObjectId);
        HistoryLogger logger = new HistoryLogger();
        for (int dataAttributeId : dataAttributeIds) {
			DataAttributeInstance dataAttributeInstance = new DataAttributeInstance(
					dataAttributeId, dataObjectInstanceId, this);
            logger.logDataAttributeCreation(dataAttributeInstance.getDataAttributeInstanceId());

            dataAttributeInstances.add(dataAttributeInstance);
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
		dbDataObjectInstance.setState(dataObjectInstanceId, stateId);
		scenarioInstance.checkTerminationCondition();
	}

	/**
	 * Checks if the data object get edited right now.
	 *
	 * @return true if the data object get edited right now. false if not.
	 */
	public boolean getOnChange() {
		return dbDataObjectInstance.getOnChange(dataObjectInstanceId);
	}

	/**
	 * Sets on change from the data object instance.
	 *
	 * @param onChange This the value on change get set to.
	 */
	public void setOnChange(boolean onChange) {
		dbDataObjectInstance.setOnChange(dataObjectInstanceId, onChange);
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
	public int getDataObjectId() {
		return dataObjectId;
	}

	/**
	 * @return the ID of this Data Object Instance.
	 */
	public int getDataObjectInstanceId() {
		return dataObjectInstanceId;
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
	public LinkedList<DataAttributeInstance> getDataAttributeInstances() {
		return dataAttributeInstances;
	}

}
