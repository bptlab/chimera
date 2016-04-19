package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryLogger;

import javax.transaction.NotSupportedException;
import java.util.Date;

/**
 *
 */
public class DataAttributeInstance {
	private final int dataAttributeInstanceId;
	private final int dataAttributeId;
	private final int dataObjectInstanceId;

	private final DataObjectInstance dataObjectInstance;
	private final String type;
	private final String name;
	private Object value;
	private DbDataAttributeInstance dbDataAttributeInstance = new DbDataAttributeInstance();

	/**
	 * @param dataAttributeId		The ID of the Data Attribute belonging to this
	 *                              instance.
	 * @param dataObjectInstanceId	The ID of the Data Object Instance belonging to
	 *                              this instance.
	 * @param dataObjectInstance	The Data Object Instance belonging to this instance.
	 */
	public DataAttributeInstance(int dataAttributeId, int dataObjectInstanceId,
			DataObjectInstance dataObjectInstance) {
		this.dataAttributeId = dataAttributeId;
		this.dataObjectInstanceId = dataObjectInstanceId;
		this.dataObjectInstance = dataObjectInstance;
		this.type = dbDataAttributeInstance.getType(dataAttributeId);
        HistoryLogger attributeLogger = new HistoryLogger();
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
            attributeLogger.logDataAttributeCreation(this.dataAttributeInstanceId);
        }
		this.value = dbDataAttributeInstance.getValue(dataAttributeInstanceId);
		this.name = dbDataAttributeInstance.getName(dataAttributeId);
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
	public void setValue(Object value) {
		validateValueType(value);
		this.value = value;
		dbDataAttributeInstance.setValue(dataAttributeInstanceId, value);
	}

	/**
	 * Checks if a given value is allowed for this attribute instance,
	 * e.g. if it fits to the data type.
	 * @param value
     */
	public boolean isValueAllowed(Object value) {
		try {
			validateValueType(value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private void validateValueType(Object value) {
		String excp = "Could not set data attribute value "
				+ "because it did not have the correct data type.";
		switch(this.getType()) {
			case "Integer":
				if (!(value instanceof Integer))
					throw new IllegalArgumentException(excp);
				break;
			case "Double":
				if (!(value instanceof Double))
					throw new IllegalArgumentException(excp);
				break;
			case "Boolean":
				if (!(value instanceof Boolean))
					throw new IllegalArgumentException(excp);
				break;
			case "Date":
				if (!(value instanceof String) && !(value instanceof Date))
					throw new IllegalArgumentException(excp);
				break;
			case "String":
			case "Enum":
			case "Class":
				if (!(value instanceof String))
					throw new IllegalArgumentException(excp);
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
	public DataObjectInstance getDataObjectInstance() {
		return dataObjectInstance;
	}
}
