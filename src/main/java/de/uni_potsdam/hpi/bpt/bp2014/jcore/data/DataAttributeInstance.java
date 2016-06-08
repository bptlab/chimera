package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class DataAttributeInstance {
	private final int dataAttributeInstanceId;
	private final int dataAttributeId;

	private final DataObject dataObject;
	private final String type;
	private final String name;
	private Object value;
	private DbDataAttributeInstance dbDataAttributeInstance = new DbDataAttributeInstance();

	/**
	 * @param dataAttributeId		The ID of the Data Attribute belonging to this
	 *                              instance.
	 * @param dataObjectInstanceId	The ID of the Data Object Instance belonging to
	 *                              this instance.
	 * @param dataObject	The Data Object Instance belonging to this instance.
	 */
	public DataAttributeInstance(int dataAttributeId, int dataObjectInstanceId,
			DataObject dataObject) {
		this.dataAttributeId = dataAttributeId;
		this.dataObject = dataObject;
		this.type = dbDataAttributeInstance.getType(dataAttributeId);
		if (dbDataAttributeInstance.existDataAttributeInstance(
						dataAttributeId, dataObjectInstanceId)) {
			//creates an existing Attribute Instance using the database information
			this.dataAttributeInstanceId = dbDataAttributeInstance
					.getDataAttributeInstanceID(
							dataAttributeId, dataObjectInstanceId);
        } else {
			//creates a new Attribute Instance also in database
			this.dataAttributeInstanceId = dbDataAttributeInstance
					.createNewDataAttributeInstance(
							dataAttributeId, dataObjectInstanceId);
            new DbLogEntry().logDataattributeCreation(dataObjectInstanceId, this.getValue(),
                    dataObject.getScenarioInstanceId());
        }
		this.value = dbDataAttributeInstance.getValue(dataAttributeInstanceId);
		this.name = dbDataAttributeInstance.getName(dataAttributeId);
	}

    /**
     * This constructor loads a data attribute instance from database.
     *
     * @param dataAttributeInstanceId Id of the instance to load.
     */
    public DataAttributeInstance (int dataAttributeInstanceId, DataObject dataObject) {
        if (!dbDataAttributeInstance.existDataAttributeInstance(
                dataAttributeInstanceId)) {
            throw new IllegalArgumentException("Instance Id not present in the database");
        }
        this.dataAttributeInstanceId = dataAttributeInstanceId;
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
	public DataAttributeInstance (int dataAttributeInstanceId) {
		if (!dbDataAttributeInstance.existDataAttributeInstance(
				dataAttributeInstanceId)) {
			throw new IllegalArgumentException("Instance Id not present in the database");
		}
		this.dataAttributeInstanceId = dataAttributeInstanceId;
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
	 * Checks if the value has the correct type.
	 * If so, sets the value of the data attribute instance. It get also written in the database.
	 *
	 * @param value to set.
	 */
	public void setValue(String value) {
		validateValueType(value);
		this.value = value;
		dbDataAttributeInstance.setValue(dataAttributeInstanceId, value);
	}

	/**
	 * Checks if a given value is allowed for this attribute instance,
	 * e.g. if it fits to the data type.
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
		// TODO notify frontend when value is wrong
		String excp = "Could not set data attribute value "
				+ "because it did not have the correct data type.";
		switch(this.getType()) {
			case "Integer":
				try {
					Integer.valueOf(value);
				} catch (Exception e) {
					throw new IllegalArgumentException(excp);
				}
				break;
			case "Double":
				try {
					Double.valueOf(value);
				} catch (Exception e) {
					throw new IllegalArgumentException(excp);
				}
				break;
			case "Boolean":
				try {
					Boolean.valueOf(value);
				} catch (Exception e) {
					throw new IllegalArgumentException(excp);
				}
				break;
			case "Date":
				try {
					new SimpleDateFormat("dd.MM.yyyy").parse(value);
				} catch (Exception e) {
					throw new IllegalArgumentException(excp);
				}
				break;
			case "String":
			case "Enum":
			case "Class":
				break;
			default:
				throw new IllegalArgumentException("Attribute data type is not supported.");
		}
	}

	/**
	 * @return the ID of the Data Attribute Instance
	 */
	public int getDataAttributeInstanceId() {
		return dataAttributeInstanceId;
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
