package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;

/**
 * This class is the representation of a dataObject in the database.
 * It provides the functionality to retrieve all dataObjects
 * of a scenario as well as their names and startStates.
 */
public class DbDataObject extends DbObject {
	/**
	 * This method returns all database ID's for all dataObjects belonging to a scenario.
	 *
	 * @param scenarioId This is the database ID of a scenario.
	 * @return a list of database ID's of dataObjects belonging to this scenario.
	 */
	public LinkedList<Integer> getDataObjectsForScenario(int scenarioId) {
		String sql = "SELECT id FROM dataobject WHERE scenario_id = " + scenarioId;
		return this.executeStatementReturnsListInt(sql, "id");
	}

	/**
	 * This method returns the startState of a specific dataObject used for initialization.
	 *
	 * @param dataObjectId This is the database ID of a dataObject.
	 * @return the database ID of the startState of the dataObject.
	 */
	public int getStartStateID(int dataObjectId) {
		String sql = "SELECT start_state_id FROM dataobject WHERE id = " + dataObjectId;
		return this.executeStatementReturnsInt(sql, "start_state_id");
	}

	/**
	 * This method returns the name of a dataObject corresponding to the database ID.
	 *
	 * @param dataObjectId This is the database ID of a dataObject.
	 * @return the name of a dataObject as a String.
	 */
	public String getName(int dataObjectId) {
		String sql = "SELECT name FROM dataobject WHERE id = " + dataObjectId;
		return this.executeStatementReturnsString(sql, "name");
	}

	/**
	 *
	 * @param dataObjectId This is the database ID of a dataObject.
	 * @return a list of database ID's of dataAttributes belonging to this DataObject.
	 */
	public LinkedList<Integer> getAllDataAttributesForDataObject(int dataObjectId) {
		String sql =
				"SELECT dataattribute.id AS id FROM "
						+ "`dataobject`, `dataclass`,  `dataattribute` "
						+ "WHERE dataobject.dataclass_id = dataclass.id "
						+ "AND dataclass.id = dataattribute.dataclass_id "
						+ "AND dataobject.id = " + dataObjectId;
		return this.executeStatementReturnsListInt(sql, "id");
	}
}
