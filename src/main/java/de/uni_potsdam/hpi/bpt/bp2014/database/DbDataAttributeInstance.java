package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 * This class is the representation of a DataAttribute instance in the database.
 * It provides the functionality to check for existing instances as well as creating new ones.
 */
public class DbDataAttributeInstance extends DbObject {

	/**
	 * This method creates and saves a new DataAttributeInstance to the database
	 * into the context of a DataObjectInstance and saves a log entry into the database.
	 *
	 * @param dataAttributeId      This is the database ID of the DataAttribute.
	 * @param dataObjectInstanceId This is the database ID of the DataObjectInstance
	 *                             the AttributeInstance belongs to.
	 * @return the database ID of the newly created DataAttributeInstance (Error: -1).
	 */
	public int createNewDataAttributeInstance(int dataAttributeId, int dataObjectInstanceId) {
		String sql = "INSERT INTO dataattributeinstance ("
				+ "value, dataobjectinstance_id, dataattribute_id) "
				+ "VALUES ((SELECT dataattribute.default "
				+ "FROM dataattribute WHERE id = "
				+ dataAttributeId + "), " + dataObjectInstanceId + ", "
				+ dataAttributeId + ")";
		return this.executeInsertStatement(sql);
	}

	/**
	 *
	 * @param dataAttributeId		This is the database ID of the DataAttribute.
	 * @param dataObjectInstanceId	This is the database ID of the DataObjectInstance
	 *                              the AttributeInstance belongs to.
	 * @return as a boolean whether the given dataAttribute exists
	 */
	public Boolean existDataAttributeInstance(int dataAttributeId, int dataObjectInstanceId) {
		String sql = "SELECT id FROM dataattributeinstance "
				+ "WHERE dataobjectinstance_id = " + dataObjectInstanceId + " "
				+ "AND dataattribute_id = " + dataAttributeId;
		return this.executeExistStatement(sql);
	}

	/**
	 *
	 * @param dataAttributeId		This is the database ID of the DataAttribute.
	 * @param dataObjectInstanceId	This is the database ID of the DataObjectInstance
	 *                              the AttributeInstance belongs to.
	 * @return the database ID of a DataAttributeInstance (Error: -1).
	 */
	public int getDataAttributeInstanceID(int dataAttributeId, int dataObjectInstanceId) {
		String sql = "SELECT id FROM dataattributeinstance "
				+ "WHERE dataobjectinstance_id = " + dataObjectInstanceId + " "
				+ "AND dataattribute_id = " + dataAttributeId;
		return this.executeStatementReturnsInt(sql, "id");
	}

	/**
	 *
	 * @param dataAttributeId	This is the database ID of the DataAttribute.
	 * @return the type of the DataAttribute as a String.
	 */
	public String getType(int dataAttributeId) {
		String sql = "SELECT type FROM dataattribute WHERE id = " + dataAttributeId;
		return this.executeStatementReturnsString(sql, "type");
	}

	/**
	 *
	 * @param dataAttributeInstanceId This is the database ID of the DataAttribute instance.
	 * @return the Value of the DataAttribute instance.
	 */
	public Object getValue(int dataAttributeInstanceId) {
		String sql =
				"SELECT value FROM dataattributeinstance "
						+ "WHERE id = " + dataAttributeInstanceId;
		return this.executeStatementReturnsObject(sql, "value");
	}

	/**
	 *
	 * @param dataAttributeId	This is the database ID of the DataAttribute.
	 * @return the name of the DataAttribute as a String.
	 */
	public String getName(int dataAttributeId) {
		String sql = "SELECT name FROM dataattribute WHERE id = " + dataAttributeId;
		return this.executeStatementReturnsString(sql, "name");
	}

	/**
	 * This method sets the DataAttributeinstance to a desired value
	 * and saves a log entry into the database.
	 *
	 * @param dataAttributeInstanceId This is the database ID of the DataAttributeinstance.
	 * @param value                    This is the desired value for the DataAttributeinstance.
	 */
	public void setValue(int dataAttributeInstanceId, Object value) {
        String sql = "UPDATE dataattributeinstance SET value = '" + value + "' WHERE id = "
				+ dataAttributeInstanceId;
		executeUpdateStatement(sql);
	}

	/**
	 *
	 * @param dataAttributeInstanceId This is the database ID of the DataAttribute instance.
	 * @return the database ID of the DataAttribute for the DataAttribute instance.
	 */
	public int getDataAttributeID(int dataAttributeInstanceId) {
		String sql = "SELECT dataattribute_id FROM dataattributeinstance WHERE id = "
				+ dataAttributeInstanceId;
		return this.executeStatementReturnsInt(sql, "dataattribute_id");
	}
}
