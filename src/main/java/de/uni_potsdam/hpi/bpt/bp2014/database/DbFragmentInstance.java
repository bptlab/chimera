package de.uni_potsdam.hpi.bpt.bp2014.database;


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


public class DbFragmentInstance extends DbObject {
    public Boolean existFragment(int fragment_id, int scenarioInstance_id) {
        String sql = "SELECT id FROM fragmentinstance WHERE fragmentinstance.terminated = 0 AND scenarioinstance_id = " + scenarioInstance_id + " AND fragment_id = " + fragment_id;
        return this.executeExistStatement(sql);
    }

    public int createNewFragmentInstance(int fragment_id, int scenarioInstance_id) {
        String sql = "INSERT INTO fragmentinstance (fragment_id, scenarioinstance_id) VALUES (" + fragment_id + ", " + scenarioInstance_id + ")";
        return this.executeInsertStatement(sql);
    }

    public int getFragmentInstanceID(int fragment_id, int scenarioInstance_id) {
        String sql = "SELECT id FROM fragmentinstance WHERE fragmentinstance.terminated = 0 AND scenarioinstance_id = " + scenarioInstance_id + " AND fragment_id = " + fragment_id;
        return this.executeStatementReturnsInt(sql, "id");

    }

    public void terminateFragmentInstance(int fragmentInstance_id) {
        String sql = "UPDATE fragmentinstance SET fragmentinstance.terminated = 1 WHERE id = " + fragmentInstance_id;
        this.executeUpdateStatement(sql);
    }
}
