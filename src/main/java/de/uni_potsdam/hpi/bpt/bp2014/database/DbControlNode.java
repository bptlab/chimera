package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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


public class DbControlNode {
    public int getStartEventID(int fragment_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        int results = -1;
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT id FROM controlnode WHERE type = 'Startevent' AND fragment_id = " + fragment_id;
            rs = stmt.executeQuery(sql);
            rs.next();
            results = rs.getInt("id");
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
    public String getType(int controlNode_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        String results = "";
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT type FROM controlnode WHERE id = " + controlNode_id;
            rs = stmt.executeQuery(sql);
            rs.next();
            results = rs.getString("type");
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
    public String getLabel(int controlNode_id) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        String results = "";
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT label FROM controlnode WHERE id = " + controlNode_id;
            rs = stmt.executeQuery(sql);
            rs.next();
            results = rs.getString("label");
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
