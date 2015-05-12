package de.uni_potsdam.hpi.bpt.bp2014.jconfiguration;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;

import java.util.List;
/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

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
