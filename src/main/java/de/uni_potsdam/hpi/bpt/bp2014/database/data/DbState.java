package de.uni_potsdam.hpi.bpt.bp2014.database.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;

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

    @Deprecated
	public int getStateId(String name) {
		String sql = "SELECT id FROM state WHERE name = '" + name + "'";
		return this.executeStatementReturnsInt(sql, "id");
	}

    public Map<String, Integer> getStateToIdMap() {
        String sql = "SELECT * FROM state;";
        Map<Integer, String> idToName = this.executeStatementReturnsMap(sql, "id", "name");
        Map<String, Integer> nameToId = new HashMap<>();

        for (Map.Entry<Integer, String> pair : idToName.entrySet()) {
            nameToId.put(pair.getValue(), pair.getKey());
        }

        return nameToId;
    }

    public Integer getStateId(int dataclassId, String name) {
        String sql = "SELECT * FROM state, datanode, dataclass WHERE " +
                "state.id = datanode.state_id AND datanode.dataclass_id = %d" +
                "AND state.name = '%s';";
        sql = String.format(sql, dataclassId, name);
        return this.executeStatementReturnsInt(sql, "state.id");
    }

}
