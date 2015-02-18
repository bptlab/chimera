package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;

/***********************************************************************************
 *
 *   _________ _______  _        _______ _________ _        _______
 *   \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 *      )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 *      |  |  | (__    |   \ | || |         | |   |   \ | || (__
 *      |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 *      |  |  | (      | | \   || | \_  )   | |   | | \   || (
 *   |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 *   (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 *
 *******************************************************************
 *
 *   Copyright © All Rights Reserved 2014 - 2015
 *
 *   Please be aware of the License. You may found it in the root directory.
 *
 ************************************************************************************/

/**
 * Represents the ControlFlow in the database
 * Mostly used to get predecessors and successors of a controlNode
 */

public class DbControlFlow extends DbObject {
    /**
     * @param controlNode_id This is the database ID of a controlNode
     * @return -1 if something went wrong else it returns the database ID of a controlNode which is right after a startEvent
     */
    public int getNextControlNodeAfterStartEvent(int controlNode_id) {
        String sql = "SELECT controlnode_id2 FROM controlflow WHERE controlnode_id1 = " + controlNode_id;
        return this.executeStatementReturnsInt(sql, "controlnode_id2");
    }

    /**
     * @param controlNode_id This is the database ID of a controlNode
     * @return a list of database ID's of controlNodes which success the given controlNode
     */
    public LinkedList<Integer> getFollowingControlNodes(int controlNode_id) {
        String sql = "SELECT controlnode_id2 FROM controlflow WHERE controlnode_id1 = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "controlnode_id2");
    }

    /**
     * @param controlNode_id This is the database ID of a controlNode
     * @return a list of database ID's of controlNodes which precede the given controlNode
     */
    public LinkedList<Integer> getPredecessorControlNodes(int controlNode_id) {
        String sql = "SELECT controlnode_id1 FROM controlflow WHERE controlnode_id2 = " + controlNode_id;
        return this.executeStatementReturnsListInt(sql, "controlnode_id1");
    }
}
