package de.uni_potsdam.hpi.bpt.bp2014.database;

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.Map;

/**
 * This class administrates the scenario instances in the databases
 * and provides check methods to make sure the database is correct.
 * Moreover it is possible to mark a scenario instance as terminated.
 */
public class DbScenarioInstance extends DbObject {
	private static Logger log = Logger.getLogger(DbScenarioInstance.class.getName());
	private boolean exist = false;
	private int scenarioId = -1;
	private int scenarioInstanceId = -1;

	/**
	 * Checks if a scenario instance belongs to the right scenario.
	 *
	 * @param scenarioId         This is the database ID of a scenario.
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @return if the check was positive(true) or not(false).
	 */
	public Boolean existScenario(int scenarioId, int scenarioInstanceId) {
		if (!exist || this.scenarioId != scenarioId
				|| this.scenarioInstanceId != scenarioInstanceId) {
			String sql = "SELECT id FROM scenarioinstance "
					+ "WHERE scenario_id = " + scenarioId + " "
					+ "AND id = " + scenarioInstanceId;
			log.info(sql);
			exist = this.executeExistStatement(sql);
			this.scenarioId = scenarioId;
			this.scenarioInstanceId = scenarioInstanceId;
		}
		return exist;
	}

	/**
	 * Checks if a given scenario instance ID is present in the database.
	 *
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @return if the check was positive(true) or not(false).
	 */
	public Boolean existScenario(int scenarioInstanceId) {
		if (!exist || this.scenarioInstanceId != scenarioInstanceId) {
			String sql = "SELECT id FROM scenarioinstance "
					+ "WHERE id = " + scenarioInstanceId;
			log.info(sql);
			exist = this.executeExistStatement(sql);
			this.scenarioInstanceId = scenarioInstanceId;
		}
		return exist;
	}

	/**
	 * Creates a new scenario instance from a scenario.
	 *
	 * @param id This is the database Id of a scenario.
	 * @return the database ID of the newly created scenario instance (Error: -1).
	 */
	public int createNewScenarioInstance(int id) {
		String sql = "INSERT INTO scenarioinstance (scenario_id, name) VALUES (" + id
				+ ", (SELECT name FROM scenario WHERE id = " + id + "))";
		log.info(sql);
		return this.executeInsertStatement(sql);
	}

	/**
	 * Creates a new scenario instance from a scenario.
	 * The given name will be assigned.
	 *
	 * @param id   The id of the scenario.
	 * @param name The name, which has to be assigned to the instance.
	 * @return the database ID of the newly created scenario instance (Error: -1).
	 */
	public int createNewScenarioInstance(int id, String name) {
		String sql = "INSERT INTO scenarioinstance (scenario_id, name) "
				+ "VALUES (" + id + "," + "'" + name + "'" + ")";
		log.info(sql);
		return this.executeInsertStatement(sql);

	}

	/**
	 * Returns the highest ID of all scenario instances of a scenario.
	 *
	 * @param scenarioId This is the database ID of a scenario.
	 * @return the database ID of the latest scenario instance (Error: -1).
	 */
	public int getScenarioInstanceID(int scenarioId) {
		String sql = "SELECT id FROM scenarioinstance WHERE scenario_id = " + scenarioId
				+ " ORDER BY id DESC";
		log.info(sql);
		return this.executeStatementReturnsInt(sql, "id");
	}

	/**
	 * returns a list of all scenario instance ID's belonging to a scenario.
	 *
	 * @param scenarioId This is the database ID of a scenario.
	 * @return a list of database ID's of all scenario instances belonging to this scenario.
	 */
	public LinkedList<Integer> getScenarioInstances(int scenarioId) {
		String sql =
				"SELECT * FROM scenarioinstance "
						+ "WHERE scenarioinstance.terminated = 0 "
						+ "AND scenario_id = " + scenarioId;
		log.info(sql);
		return this.executeStatementReturnsListInt(sql, "id");
	}

	/**
	 * returns the corresponding scenario ID for a scenario instance.
	 *
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @return the database ID of the corresponding scenario.
	 */
	public int getScenarioID(int scenarioInstanceId) {
		String sql = "SELECT scenario_id FROM scenarioinstance "
				+ "WHERE id = " + scenarioInstanceId;
		log.info(sql);
		return this.executeStatementReturnsInt(sql, "scenario_id");
	}

	/**
	 * returns if a scenario instance is terminated or not.
	 *
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @return if the scenario instance is terminated(1) or not(0) as an Integer.
	 */
	public int getTerminated(int scenarioInstanceId) {
		String sql = "SELECT scenarioinstance.terminated FROM scenarioinstance WHERE id = "
				+ scenarioInstanceId;
		log.info(sql);
		return this.executeStatementReturnsInt(sql, "terminated");
	}

	/**
	 * This sets the terminated status of a scenario instance.
	 *
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @param terminated          This is the changed status of the scenario instance.
	 */
	public void setTerminated(int scenarioInstanceId, boolean terminated) {
		int terminatedAsInt;
		if (terminated) {
			terminatedAsInt = 1;
		} else {
			terminatedAsInt = 0;
		}
		String sql = "UPDATE scenarioinstance "
				+ "SET scenarioinstance.terminated = " + terminatedAsInt
				+ " WHERE id = " + scenarioInstanceId;
		log.info(sql);
		this.executeUpdateStatement(sql);
	}

	/**
	 *
	 * @param scenarioID	This is the database ID of a scenario.
	 * @param filterString	This is a filter for the query (may not be void)
	 * @return a Map of scenario instances matching the query.
	 */
	public Map<Integer, String> getScenarioInstancesLike(int scenarioID, String filterString) {
		String sql = "SELECT * FROM scenarioinstance "
				+ "WHERE scenarioinstance.terminated = 0 "
				+ "AND scenario_id = " + scenarioID;
		if (null != filterString && !filterString.isEmpty()) {
			sql = sql + " AND name LIKE '%" + filterString + "%'";
		}
		log.info(sql);
		return this.executeStatementReturnsMap(sql, "id", "name");
	}

	/**
	 *
	 * @param instanceId This is the database ID of a scenario instance.
	 * @return a Map with detailed information about the scenario instance.
	 */
	public Map<String, Object> getInstanceMap(int instanceId) {
		String sql = "SELECT * FROM scenarioinstance WHERE id = " + instanceId;
		log.info(sql);
		return this.executeStatementReturnsMapWithKeys(
						sql, "id", "name", "terminated", "scenario_id");
	}
}
