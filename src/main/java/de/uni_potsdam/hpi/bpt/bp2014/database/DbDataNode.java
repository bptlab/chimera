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


public class DbDataNode extends DbObject {

    public LinkedList<Integer> getDataObjectIdsForDataSets(int dataSet_id) {
        String sql = "Select dataobject_id FROM datanode, datasetconsistsofdatanode WHERE datanode.id = datasetconsistsofdatanode.datanode_id AND dataset_id = " + dataSet_id + " ORDER BY dataobject_id";
        return this.executeStatementReturnsListInt(sql, "dataobject_id");
    }

    public LinkedList<Integer> getDataStatesForDataSets(int dataSet_id) {
        String sql = "Select state_id FROM datanode, datasetconsistsofdatanode WHERE datanode.id = datasetconsistsofdatanode.datanode_id AND dataset_id = " + dataSet_id + " ORDER BY dataobject_id";
        return this.executeStatementReturnsListInt(sql, "state_id");
    }
}
