package de.hpi.bpt.chimera.database.data;

import de.hpi.bpt.chimera.database.DbObject;

import java.util.List;

/**
 * This class is the representation of a DataAttribute instance in the database.
 * It provides the functionality to check for existing instances as well as creating new ones.
 */
public class DbDataAttributeInstance extends DbObject {

	/**
	 * This method creates and saves a new DataAttributeInstance to the database
	 * into the context of a DataObject and saves a log entry into the database.
	 *
	 * @param dataAttributeId      This is the database ID of the DataAttribute.
	 * @param dataObjectId This is the database ID of the DataObject
	 *                             the AttributeInstance belongs to.
	 * @return the database ID of the newly created DataAttributeInstance (Error: -1).
	 */
	public int createNewDataAttributeInstance(int dataAttributeId, int dataObjectId) {
		String sql = "INSERT INTO dataattributeinstance ("
				+ "value, dataobject_id, dataattribute_id) "
				+ "VALUES ((SELECT dataattribute.default "
				+ "FROM dataattribute WHERE id = "
				+ dataAttributeId + "), " + dataObjectId + ", "
				+ dataAttributeId + ")";
		return this.executeInsertStatement(sql);
	}

	/**
	 * Checks the database for the existence of an attribute instance for a given
	 * data attribute and data object.
	 * @param dataAttributeId		This is the database ID of the DataAttribute.
	 * @param dataObjectId	This is the database ID of the DataObject
	 *                              the AttributeInstance belongs to.
	 * @return as a boolean whether the given dataAttributeInstance exists
	 */
	public Boolean existDataAttributeInstance(int dataAttributeId, int dataObjectId) {
		String sql = "SELECT id FROM dataattributeinstance "
				+ "WHERE dataobject_id = " + dataObjectId + " "
				+ "AND dataattribute_id = " + dataAttributeId;
		return this.executeExistStatement(sql);
	}

	/**
	 * Checks the database for the existence of an attribute instance for a given id.
	 * @param id Id of the data attribute instance.
     * @return as a boolean whether the given data attribute instance exists.
     */
	public Boolean existDataAttributeInstance(int id) {
		String sql = "SELECT id FROM dataattributeinstance "
				+ "WHERE id = %d";
		return this.executeExistStatement(String.format(sql, id));
	}

	/**
	 * Retrieves the id of a data attribute instance specified by a
	 * data attribute and a data object.
	 * @param dataAttributeId		This is the database ID of the DataAttribute.
	 * @param dataObjectId	This is the database ID of the DataObject
	 *                              the AttributeInstance belongs to.
	 * @return the database ID of a DataAttributeInstance (Error: -1).
	 */
	public int getDataAttributeInstanceID(int dataAttributeId, int dataObjectId) {
		String sql = "SELECT id FROM dataattributeinstance "
				+ "WHERE dataobject_id = " + dataObjectId + " "
				+ "AND dataattribute_id = " + dataAttributeId;
		return this.executeStatementReturnsInt(sql, "id");
	}

	/**
	 * Retrieve the data type of a data attribute.
	 * @param dataAttributeId	This is the database ID of the DataAttribute.
	 * @return the type of the DataAttribute as a String.
	 */
	public String getType(int dataAttributeId) {
		String sql = "SELECT type FROM dataattribute WHERE id = " + dataAttributeId;
		return this.executeStatementReturnsString(sql, "type");
	}

	/**
	 * Retrieve the current data value of a data attribute instance.
	 * @param id This is the database ID of the DataAttribute instance.
	 * @return the Value of the DataAttribute instance.
	 */
	public Object getValue(int id) {
		String sql =
				"SELECT value FROM dataattributeinstance "
						+ "WHERE id = " + id;
		return this.executeStatementReturnsObject(sql, "value");
	}

	/**
	 * Retrieve the name of a data attribute.
	 * @param dataAttributeId	This is the database ID of the DataAttribute.
	 * @return the name of the DataAttribute as a String.
	 */
	public String getName(int dataAttributeId) {
		String sql = "SELECT name FROM dataattribute WHERE id = " + dataAttributeId;
		return this.executeStatementReturnsString(sql, "name");
	}

	/**
	 * Retrieve the id of the data object holding this data attribute instance.
	 * @param id A data attribute instance id.
	 * @return The Id of the belonging data object.
     */
	public int getDataObjectId(int id) {
		String sql = "SELECT dataobject_id FROM dataattributeinstance "
				+ "WHERE id = %d";
		return this.executeStatementReturnsInt(String.format(sql, id),
				"dataobject_id");
	}

	/**
	 * This method sets the DataAttributeinstance to a desired value
	 * and saves a log entry into the database.
	 *
	 * @param id This is the database ID of the DataAttributeinstance.
	 * @param value                    This is the desired value for the DataAttributeinstance.
	 */
	public void setValue(int id, Object value) {
        String sql = "UPDATE dataattributeinstance SET value = '" + value + "' WHERE id = "
				+ id;
		executeUpdateStatement(sql);
	}

	/**
	 *
	 * @param id This is the database ID of the DataAttribute instance.
	 * @return the database ID of the DataAttribute for the DataAttribute instance.
	 */
	public int getDataAttributeID(int id) {
		String sql = "SELECT dataattribute_id FROM dataattributeinstance WHERE id = "
				+ id;
		return this.executeStatementReturnsInt(sql, "dataattribute_id");
	}

	/**
	 * Retrieves all data attribute instance ids that are held by a specific data object.
	 * @param dataObjectId The Id of the data object.
	 * @return All Ids of data attribute instances held by the object.
     */
    public List<Integer> getAttributeInstanceIdsForDataObject(int dataObjectId) {
        String getDataAttributes = "SELECT id FROM dataattributeinstance " +
                "WHERE dataobject_id = %d;";
        getDataAttributes = String.format(getDataAttributes, dataObjectId);
        return new DbObject().executeStatementReturnsListInt(getDataAttributes, "id");
    }
}
