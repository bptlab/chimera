package de.uni_potsdam.hpi.bpt.bp2014;

//import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.settings.PropertyLoader;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.AfterClass;
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
    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2_schema.sql";
    /**
     * TODO: The same database is used for testing and running (cf. CM-429 in Jira)
     */
    private static final String TEST_DB_SCHEMA = PropertyLoader.getProperty("mysql.schema");
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
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
        runner.runScript(new FileReader(TEST_SQL_SEED_FILE));
    }
    
    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
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
