package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


public class DbScenarioInstance extends DbObject {
    public Boolean existScenario(int scenario_id, int scenarioInstance_id) {
        String sql = "SELECT id FROM scenarioinstance WHERE scenario_id = " + scenario_id + " AND id = " + scenarioInstance_id;
        return this.executeExistStatement(sql);
    }

    public Boolean existScenario(int scenarioInstance_id) {
        String sql = "SELECT id FROM scenarioinstance WHERE id = " + scenarioInstance_id;
        return this.executeExistStatement(sql);
    }

    public int createNewScenarioInstance(int id) {
        String sql = "INSERT INTO scenarioinstance (scenario_id) VALUES (" + id + ")";
        return this.executeInsertStatement(sql);
    }

    public int getScenarioInstanceID(int scenario_id) {
        String sql = "SELECT id FROM scenarioinstance WHERE scenario_id = " + scenario_id + " ORDER BY id DESC";
        return this.executeStatementReturnsInt(sql, "id");
    }

    public LinkedList<Integer> getScenarioInstances(int scenario_id) {
        String sql = "SELECT id FROM scenarioinstance WHERE scenario_id = " + scenario_id;
        return this.executeStatementReturnsListInt(sql, "id");
    }

    public int getScenarioID(int scenarioInstance_id) {
        String sql = "SELECT scenario_id FROM scenarioinstance WHERE id = " + scenarioInstance_id;
        return this.executeStatementReturnsInt(sql, "scenario_id");
    }

    public int getTerminated(int scenarioInstance_id) {
        String sql = "SELECT scenarioinstance.terminated FROM scenarioinstance WHERE id = " + scenarioInstance_id;
        return this.executeStatementReturnsInt(sql, "terminated");
    }

    public void setTerminated(int scenarioInstance_id, boolean terminated) {
        int terminatedAsInt;
        if (terminated) {
            terminatedAsInt = 1;
        } else {
            terminatedAsInt = 0;
        }
        String sql = "UPDATE scenarioinstance SET scenarioinstance.terminated = " + terminatedAsInt + " WHERE id = " + scenarioInstance_id;
        this.executeUpdateStatement(sql);
    }


}
