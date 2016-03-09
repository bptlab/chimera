package de.uni_potsdam.hpi.bpt.bp2014.database;

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.Map;

/**
 * This class represents the ScenarioData in the database.
 * It provides the functionality to retrieve all existing scenarios as well as their name.
 */
public class DbScenario extends DbObject {
	private static Logger log = Logger.getLogger(DbScenario.class);

	private String name = null;

	/**
	 * Returns you all the database ID's of all scenarios stored in the database.
	 *
	 * @return a list of database ID's of all scenarios.
	 */
	public LinkedList<Integer> getScenarioIDs() {
		String sql = "SELECT id FROM scenario WHERE deleted = 0";
		log.info(sql);
		return this.executeStatementReturnsListInt(sql, "id");
	}

	/**
	 * checks if the scenario is stored in the database.
	 *
	 * @param scenarioId This is the database ID of a scenario.
	 * @return a boolean which indicates if the scenario is present(true) or not(false).
	 */
	public Boolean existScenario(int scenarioId) {
		String sql = "SELECT id FROM scenario WHERE id = " + scenarioId;
		log.info(sql);
		return this.executeExistStatement(sql);
	}

	/**
	 * This method gives you the corresponding name to a scenario ID.
	 *
	 * @param id This is the database ID of a scenario.
	 * @return the name of the scenario as a String.
	 */
	public String getScenarioName(int id) {
		if (name == null) {
			String sql = "SELECT name FROM scenario WHERE id = " + id;
			log.info(sql);
			name = this.executeStatementReturnsString(sql, "name");
		}
		return name;
	}

	/**
	 * This method provides access to the default information of a scenario.
	 *
	 * @return a HashMap of the Database ids and their label.
	 */
	public Map<Integer, String> getScenarios() {
		String sql = "SELECT id, name FROM scenario WHERE deleted = 0";
		log.info(sql);
		return this.executeStatementReturnsMap(sql, "id", "name");
	}

	/**
	 * This method provides access to the default information of a scenario.
	 * The name of the scenario must contain the filterString.
	 *
	 * @param filterString This is a string to filter the query.
	 * @return a HashMap of the Database ids and their label.
	 */
	public Map<Integer, String> getScenariosLike(String filterString) {
		String sql =
				"SELECT id, name FROM scenario WHERE deleted = 0 "
						+ "AND name LIKE '%" + filterString	+ "%'";
		log.info(sql);
		return this.executeStatementReturnsMap(sql, "id", "name");
	}

	/**
	 * Provides information abut one ScenarioData.
	 * The Information consists of the id, name and version.
	 *
	 * @param id This is the database ID for a scenario.
	 * @return a Map with the scenario details.
	 */
	public Map<String, Object> getScenarioDetails(int id) {
		String sql =
				"SELECT id, name, modelid, modelversion FROM scenario "
						+ "WHERE deleted = 0 AND id = "	+ id;
		log.info(sql);
		return this.executeStatementReturnsMapWithKeys(sql, "id", "name", "modelversion",
				"modelid");
	}
}
