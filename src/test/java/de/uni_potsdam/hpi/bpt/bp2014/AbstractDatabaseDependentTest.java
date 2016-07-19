package de.uni_potsdam.hpi.bpt.bp2014;

//import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.database.ConnectionWrapper;
import de.uni_potsdam.hpi.bpt.bp2014.settings.PropertyLoader;

import org.junit.AfterClass;
import org.junit.Before;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class AbstractDatabaseDependentTest {
    /**
     * The Database Seed file.
     */
    private static final String TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2_AcceptanceTests.sql";

    private static final String DEVELOPMENT_SQL_SEED_FILE = PropertyLoader.getProperty("database.schema.file");
    /**
     * TODO: The same database is used for testing and running (cf. CM-429 in Jira)
     */
    private static final String TEST_DB_SCHEMA = PropertyLoader.getProperty("mysql.schema");

    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(ConnectionWrapper.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }

    /**
     * Sets up the database for RestTests.
     *
     * @throws java.io.IOException  An Error while reading the SQL-File occurred.
     * @throws java.sql.SQLException An Error while executing the SQL-Script occurred.
     */
    @Before
    public void setUpDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(ConnectionWrapper.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
        runner.runScript(new FileReader(TEST_SQL_SEED_FILE));
    }

    /**
     * Drops and recreates the database.
     */
    protected static void clearDatabase() {
      // connect() uses the url of the normal db, not the test db
        java.sql.Connection conn = ConnectionWrapper.getInstance().connect();
        Statement stmt = null;
        if (conn == null) {
            return;
        }
        try {
            //Execute a querystmt = conn.createStatement();
            stmt = conn.createStatement();
            stmt.execute("DROP DATABASE IF EXISTS " + TEST_DB_SCHEMA);
            stmt.execute("CREATE DATABASE " + TEST_DB_SCHEMA);
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
