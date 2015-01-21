package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import java.sql.*;

/*
As a part of the JComparser we need to seed the parsed information's into the JEngine Database.
 */

public class Connector {

    public void insertScenarioIntoDatabase(String name, String ter) {
    }

    public void insertGatewayIntoDatabase(String textContent) {
    }

    public void insertEventIntoDatabase(String textContent) {
    }

    public void insertActivityIntoDatabase(String name, int fragmentID) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        if (conn == null) return;

        try {

            String sql = "INSERT INTO controlnodeinstance (Label, type, fragment_id) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, "Activity");
            stmt.setInt(3, fragmentID);
            stmt.executeUpdate();
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
}
