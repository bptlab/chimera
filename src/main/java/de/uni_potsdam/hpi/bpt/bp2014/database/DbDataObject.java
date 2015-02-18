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


public class DbDataObject extends DbObject {
    public LinkedList<Integer> getDataObjectsForScenario(int scenario_id) {
        String sql = "SELECT id FROM dataobject WHERE scenario_id = " + scenario_id;
        return this.executeStatementReturnsListInt(sql, "id");
    }

    public int getStartStateID(int dataObject_id) {
        String sql = "SELECT start_state_id FROM dataobject WHERE id = " + dataObject_id;
        return this.executeStatementReturnsInt(sql, "start_state_id");
    }

    public String getName(int dataObject_id) {
        String sql = "SELECT name FROM dataobject WHERE id = " + dataObject_id;
        return this.executeStatementReturnsString(sql, "name");
    }
}
