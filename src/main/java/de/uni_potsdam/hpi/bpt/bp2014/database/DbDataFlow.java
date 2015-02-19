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
 * This class is the representation of the dataFlow in the database.
 * It handles the connection between controlNodes and dataSets.
 * It provides the functionality to get all dataSets belonging to the input- or outputSet of a controlNode.
 */
public class DbDataFlow extends DbObject {
    /**
     * This method returns all database ID's of all dataSets belonging to the inputSet of a controlNode.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return a list of database ID's of dataSets belonging to the inputSet of this controlNode.
     */
    public LinkedList<Integer> getInputSetsForControlNode(int controlNode_id) {
        String sql = "Select dataset_id FROM dataflow WHERE dataflow.input = 1 AND controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "dataset_id");

    }

    /**
     * This method returns all database ID's of all dataSets belonging to the outputSet of a controlNode.
     *
     * @param controlNode_id This is the database ID of a controlNode.
     * @return a list of database ID's of dataSets belonging to the outputSet of this controlNode.
     */
    public LinkedList<Integer> getOutputSetsForControlNode(int controlNode_id) {
        String sql = "Select dataset_id FROM dataflow WHERE dataflow.input = 0 AND controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "dataset_id");
    }
}
