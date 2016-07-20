package de.hpi.bpt.chimera.database.data;

import de.hpi.bpt.chimera.database.ConnectionWrapper;
import de.hpi.bpt.chimera.database.DbObject;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * This class creates and executes sql statements to get the terminationCondition for a scenario.
 */
public class DbTerminationCondition extends DbObject {

	public Map<Integer, Integer> getDataClassToState(String conditionSetId) {
		String sql = "SELECT dataclass_id, state_id FROM terminationcondition WHERE conditionset_id = '%s'";
		return this.executeStatementReturnsMapIntInt(
				String.format(sql, conditionSetId),
				"dataclass_id", "state_id");
	}

    public Set<String> getConditionSetKeysForScenario(int scenarioId) {
        String sql = "SELECT * FROM terminationcondition WHERE scenario_id = %d;";
        String retrieveMappingKeys = String.format(sql, scenarioId);
        return new HashSet<>(this.executeStatementReturnsListString(
                retrieveMappingKeys, "conditionset_id"));
    }

	/**
	 * Returns a list of hash map with all the information
	 * of a Termination Condition of one specific Scenario.
	 * The Scenario is specified by the given id.
	 *
	 * @param scenarioID the id of the given Scenario
	 * @return a Map with detailed information about the scenario
	 */
    @Deprecated
	public Map<String, List<Map<String, Object>>> getDetailedConditionsForScenario(
			int scenarioID) {
		String sql = "SELECT t.conditionset_id AS set_id, "
				+ "s.name AS state, c.name AS data_object "
				+ "FROM terminationcondition AS t, dataclass AS c, state AS s "
				+ "WHERE t.dataclass_id = c.id AND t.state_id = s.id "
				+ "AND t.scenario_id = " + scenarioID;
		return this.executeStatementReturnsMapOfListOfMapsWithKeys(
				sql, "state", "data_object", "set_id");
	}

	/**
	 * @param sql	This is the sql Statement which shall be executed.
	 * @param keys	These strings specify what to return
	 * @return a Map with a List of Maps with Keys
	 */
    @Deprecated
	public Map<String, List<Map<String, Object>>>
		executeStatementReturnsMapOfListOfMapsWithKeys(String sql, String... keys) {

		Map<String, List<Map<String, Object>>> conditionSets = new HashMap<>();
		try (Connection conn = ConnectionWrapper.getInstance().connect();
			 Statement stmt = conn.createStatement()){
			ResultSet results = stmt.executeQuery(sql);
			while (results.next()) {
				String conditionSetId = results.getString("set_id");
				if (!conditionSets.containsKey(conditionSetId)) {
					conditionSets.put(conditionSetId,
							new ArrayList<>());
				}
				Map<String, Object> condition = new HashMap<>();
				for (String key : keys) {
					condition.put(key, results.getObject(key));
				}
				conditionSets.get(conditionSetId).add(condition);
			}
			results.close();
		} catch (SQLException e) {
			log.error("SQL Error!: ", e);
		}
		return conditionSets;
	}
}
