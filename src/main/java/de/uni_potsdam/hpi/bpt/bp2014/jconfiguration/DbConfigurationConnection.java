package de.uni_potsdam.hpi.bpt.bp2014.jconfiguration;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;

import java.util.List;


/**
 * This class provides the necessary sql-Statements for supporting the configurations.
 */
public class DbConfigurationConnection extends DbObject {
    /**
     * Set deleted-flag in table 'scenario' to true.
     *
     * @param scenarioID DatabaseID of the scenario that gets deleted
     */
    public void deleteScenario(int scenarioID) {
        DbObject dbObject = new DbObject();
        String sql = "UPDATE scenario " +
                "SET deleted = 1 " +
                "WHERE id = " + scenarioID;
        dbObject.executeUpdateStatement(sql);
    }

    /**
     * Get all running scenarioInstances of a specified scenario
     *
     * @param scenarioID DatabaseID of the scenario whose instances are returned
     * @return List of databaseIDS of the running scenarioInstances
     */
    public List<Integer> getRunningScenarioInstances(int scenarioID) {
        DbObject dbObject = new DbObject();
        String select = "SELECT id " +
                "FROM scenarioinstance " +
                "WHERE scenarioinstance.terminated = 0 " +
                "AND scenario_id = " + scenarioID;
        return dbObject.executeStatementReturnsListInt(select, "id");
    }
}
