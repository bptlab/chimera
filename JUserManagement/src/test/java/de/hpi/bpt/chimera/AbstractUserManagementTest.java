package de.hpi.bpt.chimera;

import com.ibatis.common.jdbc.ScriptRunner;

import de.hpi.bpt.chimera.database.ConnectionWrapper;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * An Abstract class for Rest Tests
 */
public abstract class AbstractUserManagementTest extends JerseyTest {

    protected static final String JUSERMANAGEMENT_SQL_FILE =
            "src/main/resources/JUserManagement_schema.sql";
    protected static final String TEST_SQL_SEED_FILE =
            "src/test/resources/JUserManagement_RESTTest.sql";

    /**
     * Sets up the database for RestTests.
     *
     * @throws IOException  An Error while reading the SQL-File occurred.
     * @throws SQLException An Error while executing the SQL-Script occurred.
     */
    @Before
    public void setUpDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(ConnectionWrapper.getInstance().connect(), false, false);
        runner.runScript(new FileReader(JUSERMANAGEMENT_SQL_FILE));
        runner.runScript(new FileReader(TEST_SQL_SEED_FILE));
    }

    /**
     * Drops and recreates the database.
     */
    protected static void clearDatabase() {
        java.sql.Connection conn = ConnectionWrapper.getInstance().connect();
        Statement stmt = null;
        if (conn == null) {
            return;
        }
        try {
            //Execute a querystmt = conn.createStatement();
            stmt = conn.createStatement();
            stmt.execute("DROP DATABASE IF EXISTS JUserManagement");
            stmt.execute("CREATE DATABASE JUserManagement");
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}