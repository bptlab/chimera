package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataAttributeInstance;

/**
 *
 */
public class DataAttributeInstance {
	private final int dataAttributeInstanceId;
	private final int dataAttributeId;
	private final int dataObjectInstanceId;


    private final String editorId = "toImplement";
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
		if (dbDataAttributeInstance
				.existDataAttributeInstance(
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
	 * Sets the value of the data attribute instance. It get also written in the database.
	 *
	 * @param value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
		dbDataAttributeInstance.setValue(dataAttributeInstanceId, value);
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


    public String getEditorId() {
        return editorId;
    }
}
