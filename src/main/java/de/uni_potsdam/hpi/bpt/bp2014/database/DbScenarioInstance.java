package de.uni_potsdam.hpi.bpt.bp2014.database;


import java.util.LinkedList;
import java.util.Map;


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
 * This class administrates the scenario instances in the databases and provides check methods to make sure the database is correct.
 * Moreover it is possible to mark a scenario instance as terminated.
 */
public class DbScenarioInstance extends DbObject {
    /**
     * Checks if a scenario instance belongs to the right scenario.
     *
     * @param scenario_id         This is the database ID of a scenario.
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @return if the check was positive(true) or not(false).
     */
    public Boolean existScenario(int scenario_id, int scenarioInstance_id) {
        String sql = "SELECT id FROM scenarioinstance WHERE scenario_id = " + scenario_id + " AND id = " + scenarioInstance_id;
        return this.executeExistStatement(sql);
    }

    /**
     * Checks if a given scenario instance ID is present in the database.
     *
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @return if the check was positive(true) or not(false).
     */
    public Boolean existScenario(int scenarioInstance_id) {
        String sql = "SELECT id FROM scenarioinstance WHERE id = " + scenarioInstance_id;
        return this.executeExistStatement(sql);
    }

    /**
     * Creates a new scenario instance from a scenario.
     *
     * @param id This is the database Id of a scenario.
     * @return -1 if something went wrong else it returns the database ID of the newly created scenario instance.
     */
    public int createNewScenarioInstance(int id) {
        String sql = "INSERT INTO scenarioinstance (scenario_id, name) VALUES (" + id + ", (SELECT name FROM scenario WHERE id = " + id + "))";
        return this.executeInsertStatement(sql);
    }

    /**
     * Creates a new scenario instance from a scenario.
     * The given name will be assigned.
     *
     * @param id The id of the scenario.
     * @param name The name, which has to be assigned to the instance.
     * @return -1 if something went wrong else it returns the database ID of the newly created scenario instance.
     */
    public int createNewScenarioInstance(int id, String name) {
        String sql = "INSERT INTO scenarioinstance (scenario_id, name) " +
                "VALUES (" + id + "," + "'" + name + "'" + ")";
        return this.executeInsertStatement(sql);

    }

    /**
     * Returns the highest ID of all scenario instances of a scenario.
     *
     * @param scenario_id This is the database ID of a scenario.
     * @return -1 if something went wrong else it returns the database ID of the latest scenario instance.
     */
    public int getScenarioInstanceID(int scenario_id) {
        String sql = "SELECT id FROM scenarioinstance WHERE scenario_id = " + scenario_id + " ORDER BY id DESC";
        return this.executeStatementReturnsInt(sql, "id");
    }

    /**
     * returns a list of all scenario instance ID's belonging to a scenario.
     *
     * @param scenario_id This is the database ID of a scenario.
     * @return a list of database ID's of all scenario instances belonging to this scenario.
     */
    public LinkedList<Integer> getScenarioInstances(int scenario_id) {
        String sql = "SELECT id FROM scenarioinstance WHERE scenario_id = " + scenario_id;
        return this.executeStatementReturnsListInt(sql, "id");
    }

    /**
     * returns the corresponding scenario ID for a scenario instance.
     *
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @return the database ID of the corresponding scenario.
     */
    public int getScenarioID(int scenarioInstance_id) {
        String sql = "SELECT scenario_id FROM scenarioinstance WHERE id = " + scenarioInstance_id;
        return this.executeStatementReturnsInt(sql, "scenario_id");
    }

    /**
     * returns if a scenario instance is terminated or not.
     *
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @return if the scenario instance is terminated(1) or not(0) as an Integer.
     */
    public int getTerminated(int scenarioInstance_id) {
        String sql = "SELECT scenarioinstance.terminated FROM scenarioinstance WHERE id = " + scenarioInstance_id;
        return this.executeStatementReturnsInt(sql, "terminated");
    }

    /**
     * This sets the terminated status of a scenario instance.
     *
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @param terminated          This is the changed status of the scenario instance.
     */
    public void setTerminated(int scenarioInstance_id, boolean terminated) {
        //TODO: history log
        int terminatedAsInt;
        if (terminated) {
            terminatedAsInt = 1;
        } else {
            terminatedAsInt = 0;
        }
        String sql = "UPDATE scenarioinstance SET scenarioinstance.terminated = " + terminatedAsInt + " WHERE id = " + scenarioInstance_id;
        this.executeUpdateStatement(sql);
    }


    public Map<Integer, String> getScenarioInstancesLike(int scenarioID, String filterString) {
        String sql = "SELECT id, name FROM scenarioinstance WHERE scenario_id = " + scenarioID;
        if (null != filterString && !filterString.equals("")) {
            sql = sql + " AND name LIKE '%" + filterString + "%'";
        }
        return this.executeStatementReturnsMap(sql, "id", "name");
    }

    public Map<String, Object> getInstanceMap(int instanceId) {
        String sql = "SELECT * FROM scenarioinstance WHERE id = " + instanceId;
        return this.executeStatementReturnsMapWithKeys(sql, "id", "name", "terminated", "scenario_id");
    }
}
