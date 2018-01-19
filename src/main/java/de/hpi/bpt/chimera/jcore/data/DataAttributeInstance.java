package de.hpi.bpt.chimera.jcore.data;

import de.hpi.bpt.chimera.database.data.DbDataAttributeInstance;
import de.hpi.bpt.chimera.database.history.DbLogEntry;
import de.hpi.bpt.chimera.jcore.eventhandling.SseNotifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

/**
 *
 */
public class DataAttributeInstance {
	private final int id;
	private final int dataAttributeId;

	private final DataObject dataObject;
	private final String type;
	private final String name;
	private Object value;
	private DbDataAttributeInstance dbDataAttributeInstance = new DbDataAttributeInstance();
	private Logger log = Logger.getLogger(DataAttributeInstance.class);

	/**
	 * @param dataAttributeId The ID of the Data Attribute belonging to this
	 *                        instance.
	 * @param dataObjectId    The ID of the Data Object Instance belonging to
	 *                        this instance.
	 * @param dataObject      The Data Object Instance belonging to this instance.
	 */
	public DataAttributeInstance(int dataAttributeId, int dataObjectId, DataObject dataObject) {
		this.dataAttributeId = dataAttributeId;
		this.dataObject = dataObject;
		this.type = dbDataAttributeInstance.getType(dataAttributeId);
		if (dbDataAttributeInstance.existDataAttributeInstance(dataAttributeId, dataObjectId)) {
			//creates an existing Attribute Instance using the database information
			this.id = dbDataAttributeInstance.getDataAttributeInstanceID(dataAttributeId, dataObjectId);
		} else {
			//creates a new Attribute Instance also in database
			this.id = dbDataAttributeInstance.createNewDataAttributeInstance(dataAttributeId, dataObjectId);
			new DbLogEntry().logDataAttributeCreation(dataObjectId, this.getValue(), dataObject.getScenarioInstanceId());
		}
		this.value = dbDataAttributeInstance.getValue(id);
		this.name = dbDataAttributeInstance.getName(dataAttributeId);
	}

	/**
	 * This constructor loads a data attribute instance from database.
	 *
	 * @param dataAttributeInstanceId Id of the instance to load.
	 */
	public DataAttributeInstance(int dataAttributeInstanceId, DataObject dataObject) {
		if (!dbDataAttributeInstance.existDataAttributeInstance(dataAttributeInstanceId)) {
			throw new IllegalArgumentException("Instance Id not present in the database");
		}
		this.id = dataAttributeInstanceId;
		this.dataAttributeId = dbDataAttributeInstance.getDataAttributeID(dataAttributeInstanceId);
		this.type = dbDataAttributeInstance.getType(dataAttributeId);
		this.dataObject = dataObject;
		this.value = dbDataAttributeInstance.getValue(dataAttributeInstanceId);
		this.name = dbDataAttributeInstance.getName(dataAttributeId);
	}

	/**
	 * This is used for changing attribute instance values.
	 * This constructor loads a data attribute instance from database.
	 * It does not have a link to the dataobject.
	 *
	 * @param dataAttributeInstanceId Id of the instance to load.
	 */
	public DataAttributeInstance(int dataAttributeInstanceId) {
		if (!dbDataAttributeInstance.existDataAttributeInstance(dataAttributeInstanceId)) {
			throw new IllegalArgumentException("Instance Id not present in the database");
		}
		this.id = dataAttributeInstanceId;
		this.dataAttributeId = dbDataAttributeInstance.getDataAttributeID(dataAttributeInstanceId);
		this.type = dbDataAttributeInstance.getType(dataAttributeId);
		this.value = dbDataAttributeInstance.getValue(dataAttributeInstanceId);
		this.name = dbDataAttributeInstance.getName(dataAttributeId);

		this.dataObject = null;
	}

	/**
	 * @return the type of the Data Attribute Instance
	 */
	public String getType() {
		return type;
	}

	// ****************************************** Getter *********************************//

	/**
	 * @return the value of the Data Attribute Instance
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Tries to set this DataAttributeInstance to the provided value and update the database.
	 * This can fail, if the value has not the correct data type, in which case the frontend is notified.
	 *
	 * @param value to set.
	 */
	public void setValue(String value) {
		if (isValueAllowed(value)) {
			this.value = value;
			dbDataAttributeInstance.setValue(id, value);
		} else {
			String errorMessage = String.format("Data attribute %s of data type %s could not be set to %s" + 
					" because value has wrong data type.", getName(), getType(), value);
			SseNotifier.notifyWarning(errorMessage);
			log.error(errorMessage);
		}
	}

	/**
	 * Checks if a given value is allowed for this attribute instance,
	 * e.g. if it fits to the data type.
	 *
	 * @param value
	 */
	public boolean isValueAllowed(String value) {
		try {
			validateValueType(value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private void validateValueType(String value) {
		switch (this.getType()) {
		case "Integer":
			Integer.valueOf(value);
			break;
		case "Double":
			Double.valueOf(value);
			break;
		case "Boolean":
			if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
				throw new IllegalArgumentException();
			}
			break;
		case "Date":
			try {
				new SimpleDateFormat("dd.MM.yyyy").parse(value);
			} catch (ParseException e) {
				throw new IllegalArgumentException();
			}
			break;
		case "String":
		case "Enum":
		case "Class":
			break;
		default:
			throw new IllegalArgumentException();
		} 
	}

	/**
	 * @return the ID of the Data Attribute Instance
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the ID of the Data Attribute.
	 */
	public int getDataAttributeId() {
		return dataAttributeId;
	}

	/**
	 * @return the Name of the Data Attribute Instance.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the Data Object Instance.
	 */
	public DataObject getDataObject() {
		return dataObject;
	}
}
