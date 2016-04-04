package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 * This class is the representation of a dataObject instance in the database.
 * It provides the functionality to check for existing instances or create new ones.
 * Moreover you can get the state of the dataObject instance
 * as well as prevent concurrent modifying of the same instance.
 */
public class DbDataObjectInstance extends DbObject {
	/**
	 * This method checks if the instance of a dataObject is existing.
	 *
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @param dataObjectId       This is the database ID of a dataObject.
	 * @return true if DataObject is existing else false.
	 */
	public Boolean existDataObjectInstance(int scenarioInstanceId, int dataObjectId) {
		String sql = "SELECT id FROM dataobjectinstance WHERE scenarioinstance_id = "
				+ scenarioInstanceId + " AND dataobject_id = " + dataObjectId;
		return this.executeExistStatement(sql);
	}

	/**
	 * This method sets the state of a dataObject instance and saves a log entry.
	 *
	 * @param id    This is the database ID of a dataObject instance.
	 * @param state This is the desirable state of a dataObject instance.
	 */
	public void setState(int id, int state) {
		String sql = "UPDATE dataobjectinstance SET state_id = " + state
				+ " WHERE id = " + id;
		this.executeUpdateStatement(sql);
	}

	/**
	 * This method creates a dataObject instance and saves a log entry into the database.
	 *
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @param stateId            This is the initial state of a dataObject instance.
	 * @param dataObjectId       This is the database ID of a dataObject.
	 * @return the database ID of the newly created dataObject instance (Error: -1).
	 */
	public int createNewDataObjectInstance(int scenarioInstanceId, int stateId,
			int dataObjectId) {
		String sql =
				"INSERT INTO dataobjectinstance ("
						+ "scenarioinstance_id, state_id, dataobject_id) "
						+ "VALUES (" + scenarioInstanceId + ", "
						+ stateId + ", " + dataObjectId + ")";
		return this.executeInsertStatement(sql);
	}

	/**
	 * This method returns the dataObject instance ID of a corresponding dataObject.
	 *
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @param dataObjectId       This is the database ID of a dataObject.
	 * @return -1 if something went wrong else the database ID of a dataObject instance.
	 */
	public int getDataObjectInstanceID(int scenarioInstanceId, int dataObjectId) {
		String sql = "SELECT id FROM dataobjectinstance "
				+ "WHERE dataobject_id = " + dataObjectId
				+ " AND scenarioinstance_id = " + scenarioInstanceId;
		return this.executeStatementReturnsInt(sql, "id");
	}

	/**
	 * This method returns the state ID of a given dataObject instance.
	 *
	 * @param dataObjectInstanceId This is the database ID of a dataObject instance.
	 * @return the database ID of the state of the dataObject instance (Error: -1).
	 */
	public int getStateID(int dataObjectInstanceId) {
		String sql = "SELECT state_id FROM dataobjectinstance "
				+ "WHERE id = " + dataObjectInstanceId;
		return this.executeStatementReturnsInt(sql, "state_id");
	}

	/**
	 * This method checks if the dataObject instance
	 * is in a modified state to prevent concurrency.
	 *
	 * @param dataObjectInstanceId This is the database ID of a dataObject instance.
	 * @return true if another activity is using this dataObject instance else false.
	 */
	public boolean getOnChange(int dataObjectInstanceId) {
		String sql = "SELECT onchange FROM dataobjectinstance "
				+ "WHERE id = " + dataObjectInstanceId;
		return this.executeStatementReturnsBoolean(sql, "onchange");
	}

	/**
	 * This method sets the dataObject instance to a modified state
	 * so that no other activity can work with it.
	 *
	 * @param id       This is the database ID of a dataObject instance.
	 * @param onChange This is the flag set to indicate if
	 *                    the dataObject instance is being modified or not.
	 */
	public void setOnChange(int id, Boolean onChange) {
		int onChangeAsInt;
		if (onChange) {
			onChangeAsInt = 1;
		} else {
			onChangeAsInt = 0;
		}
		String sql =
				"UPDATE dataobjectinstance SET onchange = " + onChangeAsInt
						+ " WHERE id = " + id;
		this.executeUpdateStatement(sql);
	}

	/**
	 * This method returns the dataobject_id of a dataObjectInstance.
	 *
	 * @param dataObjectInstanceID Id of the dataIbjectInstance.
	 * @return Dataobject_id.
	 */
	public int getDataObjectID(int dataObjectInstanceID) {
		String sql =
				"SELECT dataobject_id FROM dataobjectinstance "
						+ "WHERE id = " + dataObjectInstanceID;
		return this.executeStatementReturnsInt(sql, "dataobject_id");
	}
}
