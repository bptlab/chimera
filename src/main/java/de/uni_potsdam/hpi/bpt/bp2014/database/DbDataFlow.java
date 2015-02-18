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


public class DbDataFlow extends DbObject {
    public LinkedList<Integer> getInputSetsForControlNode(int controlNode_id) {
        String sql = "Select dataset_id FROM dataflow WHERE dataflow.input = 1 AND controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "dataset_id");

    }

    public LinkedList<Integer> getOutputSetsForControlNode(int controlNode_id) {
        String sql = "Select dataset_id FROM dataflow WHERE dataflow.input = 0 AND controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "dataset_id");
    }
}
