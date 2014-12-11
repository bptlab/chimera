package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
As a part of the JComparser we need to seed the parsed information's into the JEngine Database.
 */

public class Connector {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/JEngine";

    // Database credentials
    static final String USER = "jengine_user";
    static final String PASS = "wAgeDqkbW3rOawiYYmQz";

    public static Connection connect() {
        Connection conn = null;
        try {
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();

        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return conn;
    }

    public void insertScenarioIntoDatabase(String name, String ter) {
    }

    public void insertGatewayIntoDatabase(String textContent) {
    }

    public void insertEventIntoDatabase(String textContent) {
    }

    public void insertActivityIntoDatabase(String name) {
    }
}
