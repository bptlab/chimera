package de.hpi.bpt.chimera.database;

import java.util.List;

/**
 * This class is the representation of a fragment in the database.
 * It provides the functionality to get all fragments for a scenario.
 */
public class DbFragment extends DbObject {
	/**
	 * This method returns all database ID's of all fragments which belong to a scenario.
	 *
	 * @param scenarioId This is the database ID of a scenario.
	 * @return a list of all ID's of all fragments belonging to the scenario.
	 */
	public List<Integer> getFragmentsForScenario(int scenarioId) {
		String sql = "SELECT id FROM fragment WHERE scenario_id = " + scenarioId;
		return this.executeStatementReturnsListInt(sql, "id");
	}

	public List<String> getXmlStringsForScenario(int scenarioId) {
		String sql = "SELECT xml FROM fragment, fragmentxml "
				+ "WHERE fragment.scenario_id = %d "
				+ "AND fragmentxml.fragment_id = fragment.id";
		return this.executeStatementReturnsListString(
				String.format(sql, scenarioId), "xml");
	}
}
