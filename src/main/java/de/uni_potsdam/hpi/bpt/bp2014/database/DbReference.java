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
 * This class holds the functionality to get all referenced activities for a known activity.
 */
public class DbReference extends DbObject {
    /**
     * This method gives you for a known activity all referenced activities ID's.
     *
     * @param activity_id This is the database ID of an activity.
     * @return a list of all database ID's of all referenced activities for the given activity.
     */
    public LinkedList<Integer> getReferenceActivitiesForActivity(int activity_id) {
        String sql = "SELECT controlnode_id2 FROM reference WHERE controlnode_id1 = " + activity_id;
        return this.executeStatementReturnsListInt(sql, "controlnode_id2");
    }
}
