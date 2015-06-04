package de.uni_potsdam.hpi.bpt.bp2014;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * An Abstract class for Rest Tests
 */
public abstract class AbstractTest extends JerseyTest {
    /**
     * The Database Seed file.
     */
    public static String TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2_AcceptanceTests.sql";

    /**
     * Sets up the database for RestTests.
     *
     * @throws IOException  An Error while reading the SQL-File occurred.
     * @throws SQLException An Error while executing the SQL-Script occurred.
     */
    @Before
    public void setUpDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(TEST_SQL_SEED_FILE));
    }

    /**
     * Drops and recreates the database.
     */
    protected static void clearDatabase() {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        if (conn == null) {
            return;
        }
        try {
            //Execute a querystmt = conn.createStatement();
            stmt = conn.createStatement();
            stmt.execute("DROP DATABASE JEngineV2");
            stmt.execute("CREATE DATABASE JEngineV2");
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
