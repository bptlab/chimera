package de.uni_potsdam.hpi.bpt.bp2014.database;


import java.util.LinkedList;

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
 * This class represents the Scenario in the database.
 * It provides the functionality to retrieve all existing scenarios as well as their name.
 */
public class DbScenario extends DbObject {
    /**
     * Returns you all the database ID's of all scenarios stored in the database.
     *
     * @return a list of database ID's of all scenarios.
     */
    public LinkedList<Integer> getScenarioIDs() {
        String sql = "SELECT id FROM scenario";
        return this.executeStatementReturnsListInt(sql, "id");
    }

    /**
     * checks if the scenario is stored in the database.
     *
     * @param scenario_id This is the database ID of a scenario.
     * @return a boolean which indicates if the scenario is present(true) or not(false).
     */
    public Boolean existScenario(int scenario_id) {
        String sql = "SELECT id FROM scenario WHERE id = " + scenario_id;
        return this.executeExistStatement(sql);
    }

    /**
     * This method gives you the corresponding name to a scenario ID.
     *
     * @param id This is the database ID of a scenario.
     * @return the name of the scenario as a String.
     */
    public String getScenarioName(int id) {
        String sql = "SELECT name FROM scenario WHERE id = " + id;
        return this.executeStatementReturnsString(sql, "name");
    }
}
