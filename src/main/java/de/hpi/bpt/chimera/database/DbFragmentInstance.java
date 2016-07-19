package de.hpi.bpt.chimera.database;

/**
 * This class is the representation of a fragment instance in the database.
 * It provides the functionality to create and terminate a fragment instance
 * as well as checking the existence of a fragment instance.
 */
public class DbFragmentInstance extends DbObject {
	/**
	 * This method checks if a fragment is present in the database.
	 *
	 * @param fragmentId         This is the database ID of a fragment.
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @return true if the fragment exists else false.
	 */
	public Boolean existFragment(int fragmentId, int scenarioInstanceId) {
		String sql =
				"SELECT id FROM fragmentinstance "
						+ "WHERE fragmentinstance.terminated = 0 "
						+ "AND scenarioinstance_id = "
						+ scenarioInstanceId
						+ " AND fragment_id =" + " " + fragmentId;
		return this.executeExistStatement(sql);
	}

	/**
	 * This method creates and saves a new fragment instance to the database.
	 *
	 * @param fragmentId         This is the database ID of a fragment.
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @return the database ID of the newly created fragment instance (Error: -1).
	 */
	public int createNewFragmentInstance(int fragmentId, int scenarioInstanceId) {
		String sql = "INSERT INTO fragmentinstance (fragment_id, scenarioinstance_id) "
				+ "VALUES (" + fragmentId + ", " + scenarioInstanceId + ")";
		return this.executeInsertStatement(sql);
	}

	/**
	 * This method gives you the fragment instance ID
	 * of a fragment from a specific scenario instance.
	 *
	 * @param fragmentId         This is the database ID of a fragment.
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @return the database ID of the fragment instance if existing else -1.
	 */
	public int getFragmentInstanceID(int fragmentId, int scenarioInstanceId) {
		String sql =
				"SELECT id FROM fragmentinstance "
						+ "WHERE fragmentinstance.terminated = 0 "
						+ "AND scenarioinstance_id = " + scenarioInstanceId
						+ " AND fragment_id =" + " " + fragmentId;
		return this.executeStatementReturnsInt(sql, "id");

	}

	/**
	 * This method terminates a fragment instance.
	 *
	 * @param fragmentInstanceId This is the database ID of a fragment instance.
	 */
	public void terminateFragmentInstance(int fragmentInstanceId) {
		String sql = "UPDATE fragmentinstance SET fragmentinstance.terminated = 1 "
				+ "WHERE id = "	+ fragmentInstanceId;
		this.executeUpdateStatement(sql);
	}

	/**
	 * This method returns the ID of the fragment the fragmentinstance belons to.
	 *
	 * @param fragmentinstanceId ID of the fragmentinstance
	 * @return FragmentID.
	 */
	public int getFragmentID(int fragmentinstanceId) {
		String sql = "SELECT fragment_id FROM fragmentinstance "
				+ "WHERE id = " + fragmentinstanceId;
		return this.executeStatementReturnsInt(sql, "fragment_id");
	}
}
