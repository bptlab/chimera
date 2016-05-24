package de.uni_potsdam.hpi.bpt.bp2014.database.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * This class creates and executes sql statements to get the terminationCondition for a scenario.
 */
public class DbTerminationCondition extends DbObject {
	private static Logger log = Logger.getLogger(DbTerminationCondition.class);

	public Map<Integer, Integer> getDataClassToState(String conditionSetId) {
		String sql = "SELECT dataclass_id, state_id FROM terminationcondition WHERE conditionset_id = %s";
		return this.executeStatementReturnsMapIntInt(
				String.format(sql, conditionSetId),
				"dataclass_id", "state_id");
	}

    public List<String> getConditionSetKeysForScenario(int scenarioId) {
        String sql = "SELECT * FROM terminationcondition WHERE scenario_id = %d;";
        String retrieveMappingKeys = String.format(sql, scenarioId);
        return this.executeStatementReturnsListString(retrieveMappingKeys, "conditionset_id");
    }

	/**
	 * Returns a list of Hashmap with all the information
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
	 *
	 * @param sql	This is the sql Statement which shall be executed.
	 * @param keys	These strings specify what to return
	 * @return a Map with a List of Maps with Keys
	 */
    @Deprecated
	public Map<String, List<Map<String, Object>>>
		executeStatementReturnsMapOfListOfMapsWithKeys(String sql, String... keys) {
		java.sql.Connection conn = Connection.getInstance().connect();
		ResultSet results = null;
		Map<String, List<Map<String, Object>>> conditionSets = new HashMap<>();
		try {
			results = conn.prepareStatement(sql).executeQuery();
			while (results.next()) {
				String conditionSetId = results.getString("set_id");
				if (!conditionSets.containsKey(conditionSetId)) {
					conditionSets.put(conditionSetId,
							new LinkedList<Map<String, Object>>());
				}
				Map<String, Object> condition = new HashMap<>();
				for (String key : keys) {
					condition.put(key, results.getObject(key));
				}
				conditionSets.get(conditionSetId).add(condition);
			}
		} catch (SQLException e) {
			log.error("SQL Error!: ", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error("SQL Error!: ", e);
			}
			try {
				if (results != null) {
					results.close();
				}
			} catch (SQLException e) {
				log.error("SQL Error!: ", e);
			}
		}
		return conditionSets;
	}
}
