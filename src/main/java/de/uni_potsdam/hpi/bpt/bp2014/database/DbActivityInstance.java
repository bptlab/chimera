package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * Represents the activity instance of the database
 * Methods are mostly used by the HistoryService so you can get all terminated activities or start new ones
 */

public class DbActivityInstance {
    /**
     * @param id This is the database Id of an activity instance
     * @return the state of the activity as a String
     */
    public String getState(int id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        String results = "";
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT activity_state FROM activityinstance WHERE id = " + id;
            rs = stmt.executeQuery(sql);
            rs.next();
            results = rs.getString("activity_state");
            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return results;
    }

    /**
     * @param id    This is the ID of an activity instance which is found in the database
     * @param state This is the state in which an activity should be after executing setState
     */
    public void setState(int id, String state) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "UPDATE activityinstance SET activity_state = '" + state + "' WHERE id = " + id;
            stmt.executeUpdate(sql);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * @param controlNodeInstance_id This is the ID of a controlNode instance which is found in the database
     * @param ActivityType           This is the type of an activity
     * @param ActivityState          this is the state of an activity
     * @return an Integer which is -1 if something went wrong else it is the database ID of the newly created activity instance
     */
    public int createNewActivityInstance(int controlNodeInstance_id, String ActivityType, String ActivityState) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "INSERT INTO activityinstance (id, type, role_id, activity_state, workitem_state) VALUES (" + controlNodeInstance_id + ", '" + ActivityType + "', 1,'" + ActivityState + "', 'init')";
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @param fragmentInstance_id This is the database ID of a fragment instance
     * @return a list of activity ID's which belong to the fragment instance and are terminated
     */
    public LinkedList<Integer> getTerminatedActivitiesForFragmentInstance(int fragmentInstance_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Integer> results = new LinkedList<Integer>();
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT controlnode_id FROM activityinstance, controlnodeinstance WHERE activityinstance.id = controlnodeinstance.id AND controlnodeinstance.Type = 'Activity' AND activity_state = 'terminated' AND fragmentinstance_id = " + fragmentInstance_id;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(rs.getInt("controlnode_id"));
            }

            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return results;
    }

    /**
     * @param scenarioInstance_id This is the database ID of a scenario instance
     * @return a list of activity ID's which belong to the scenario instance and are terminated
     */
    public LinkedList<Integer> getTerminatedActivitiesForScenarioInstance(int scenarioInstance_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Integer> results = new LinkedList<Integer>();
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT controlnode_id FROM activityinstance, controlnodeinstance WHERE activityinstance.id = controlnodeinstance.id AND controlnodeinstance.Type = 'Activity' AND activity_state = 'terminated' AND fragmentinstance_id IN (Select id FROM fragmentinstance WHERE scenarioinstance_id =" + scenarioInstance_id + ")";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(rs.getInt("controlnode_id"));
            }

            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return results;
    }

}
