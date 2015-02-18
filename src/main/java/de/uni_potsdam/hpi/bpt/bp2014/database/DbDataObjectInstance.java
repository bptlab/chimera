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


public class DbDataObjectInstance extends DbObject {

    public Boolean existDataObjectInstance(int scenarioInstance_id, int dataObject_id) {
        String sql = "SELECT id FROM dataobjectinstance WHERE scenarioinstance_id = " + scenarioInstance_id + " AND dataobject_id = " + dataObject_id;
        return this.executeExistStatement(sql);
    }

    public void setState(int id, int state) {
        String sql = "UPDATE dataobjectinstance SET state_id = " + state + " WHERE id = " + id;
        this.executeUpdateStatement(sql);
    }

    public int createNewDataObjectInstance(int scenarioInstance_id, int state_id, int dataObject_id) {
        String sql = "INSERT INTO dataobjectinstance (scenarioinstance_id, state_id, dataobject_id) VALUES (" + scenarioInstance_id + ", " + state_id + ", " + dataObject_id + ")";
        return this.executeInsertStatement(sql);
    }

    public int getDataObjectInstanceID(int scenarioInstance_id, int dataObject_id) {
        String sql = "SELECT id FROM dataobjectinstance WHERE dataobject_id = " + dataObject_id + " AND scenarioinstance_id = " + scenarioInstance_id;
        return this.executeStatementReturnsInt(sql, "id");
    }

    public int getStateID(int dataObjectInstance_id) {
        String sql = "SELECT state_id FROM dataobjectinstance WHERE id = " + dataObjectInstance_id;
        return this.executeStatementReturnsInt(sql, "state_id");
    }

    public boolean getOnChange(int dataObjectInstance_id) {
        String sql = "SELECT onchange FROM dataobjectinstance WHERE id = " + dataObjectInstance_id;
        return this.executeStatementReturnsBoolean(sql, "onchange");
    }

    public void setOnChange(int id, Boolean onChange) {
        int onChangeAsInt;
        if (onChange) {
            onChangeAsInt = 1;
        } else {
            onChangeAsInt = 0;
        }
        String sql = "UPDATE dataobjectinstance SET onchange = " + onChangeAsInt + " WHERE id = " + id;
        this.executeUpdateStatement(sql);
    }

}
