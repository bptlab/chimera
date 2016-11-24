package de.hpi.bpt.chimera.database.data;

import de.hpi.bpt.chimera.database.DbObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates and executes a sql statement to get the name of a corresponding state ID.
 */
public class DbState extends DbObject {
	/**
	 * This creates and executes a sql statement to get the state name of a dataObject.
	 *
	 * @param id This is the database ID of a state.
	 * @return the name of the state as a String.
	 */
	public String getStateName(int id) {
		String sql = "SELECT name FROM state WHERE id = " + id;
		return this.executeStatementReturnsString(sql, "name");
	}

	/**
	 * Load a Map of all data states, linking their Ids to their names.
	 *
	 * @return A Map of data state Ids to names.
	 */
	public Map<String, Integer> getStateToIdMap() {
		String sql = "SELECT * FROM state;";
		Map<Integer, String> idToName = this.executeStatementReturnsMap(sql, "id", "name");
		Map<String, Integer> nameToId = new HashMap<>();

		for (Map.Entry<Integer, String> pair : idToName.entrySet()) {
			nameToId.put(pair.getValue(), pair.getKey());
		}

		return nameToId;
	}

	/**
	 * Retrieve the Id of a state of a data class, specified by its name.
	 *
	 * @param dataClassId Id of the belonging data class.
	 * @param name        Name of the state.
	 * @return Id of the state.
	 */
	public int getStateId(int dataClassId, String name) {
		String sql = "SELECT * FROM state, datanode WHERE " + "state.id = datanode.state_id AND datanode.dataclass_id = %d " + "AND state.name = '%s'";
		sql = String.format(sql, dataClassId, name);
		return this.executeStatementReturnsInt(sql, "state.id");
	}

}
