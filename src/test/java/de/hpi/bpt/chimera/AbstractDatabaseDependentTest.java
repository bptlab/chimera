package de.hpi.bpt.chimera;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import de.hpi.bpt.chimera.database.ConnectionWrapper;
import de.hpi.bpt.chimera.util.PropertyLoader;
import de.hpi.bpt.chimera.util.ScriptRunner;

/**
 * This is the parent class for all test classes that require access to the database
 * to work. 
 */
public class AbstractDatabaseDependentTest {
    
	// TODO: find a better way to access the schema file
    private final String SCHEMA_FILE = "src/main/resources/" + PropertyLoader.getProperty("database.schema.file");
    private final String TEST_DB_SCHEMA = PropertyLoader.getProperty("mysql.test.schema");
    
    /**
     * Turns on the test mode of the {@link ConnectionWrapper}. It now will connect to
     * the test database.  
     */
    @BeforeClass
    public static void setUpConnection() {
    	ConnectionWrapper.getInstance().setTestMode(true);
    }

    /**
     * Clears the database by dropping and recreating the test schema.
     *
     * @throws java.io.IOException  An Error while reading the SQL-File occurred.
     * @throws java.sql.SQLException An Error while executing the SQL-Script occurred.
     */
    @Before
    public void clearDatabase() {
    	try (java.sql.Connection conn = ConnectionWrapper.getInstance().connect();
        		Statement stmt = conn.createStatement()) {
    		stmt.execute("DROP DATABASE IF EXISTS " + TEST_DB_SCHEMA);
            stmt.execute("CREATE DATABASE " + TEST_DB_SCHEMA);
            stmt.execute("USE " + TEST_DB_SCHEMA);
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            runner.runScript(new FileReader(SCHEMA_FILE));
        } catch (SQLException | IOException se) {
            // TODO: log errors
            se.printStackTrace();
        }
    }

    /**
     * Turn off the test mode of the {@link ConnectionWrapper}. It now will connect to
     * the production database again.  
     */
    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
    	ConnectionWrapper.getInstance().setTestMode(false);
    }
}
