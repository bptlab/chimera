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


public class DbScenario extends DbObject {
    public LinkedList<Integer> getScenarioIDs() {
        String sql = "SELECT id FROM scenario";
        return this.executeStatementReturnsListInt(sql, "id");
    }

    public Boolean existScenario(int scenario_id) {
        String sql = "SELECT id FROM scenario WHERE id = " + scenario_id;
        return this.executeExistStatement(sql);
    }

    public String getScenarioName(int id) {
        String sql = "SELECT name FROM scenario WHERE id = " + id;
        return this.executeStatementReturnsString(sql, "name");
    }
}
