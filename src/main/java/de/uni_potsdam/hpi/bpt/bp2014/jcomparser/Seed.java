package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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


public class Seed {

    public void getDBcontent() {
    }

    public void setDBcontent() {
    }

    /**
     * @param id some id..
     * @return
     */
    public ArrayList<Integer> getAllActivityIDByFragmentID(int id) {

        Connector jHandler = new Connector();

        de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector connect;
        Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();

        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<Integer> results = new ArrayList<Integer>();
        if (conn == null) return results;

        try {
            //Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT id FROM ProcessElement WHERE type = 'Activity' AND fragment_id = " + id + " ORDER BY id";

            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(rs.getInt("id"));
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
            } catch (SQLException ignored) {
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
