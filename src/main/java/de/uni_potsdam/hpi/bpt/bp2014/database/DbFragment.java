package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;


/**
 * This class is the representation of a fragment in the database.
 * It provides the functionality to get all fragments for a scenario.
 */
public class DbFragment extends DbObject {
    /**
     * This method returns all database ID's of all fragments which belong to a scenario.
     *
     * @param scenario_id This is the database ID of a scenario.
     * @return a list of all ID's of all fragments belonging to the scenario.
     */
    public LinkedList<Integer> getFragmentsForScenario(int scenario_id) {
        String sql = "SELECT id FROM fragment WHERE scenario_id = " + scenario_id;
        return this.executeStatementReturnsListInt(sql, "id");
    }
}
