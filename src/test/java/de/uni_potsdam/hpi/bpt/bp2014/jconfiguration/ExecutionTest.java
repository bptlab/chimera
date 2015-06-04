package de.uni_potsdam.hpi.bpt.bp2014.jconfiguration;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class ExecutionTest {
    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/test/resources/JEngineV2_AcceptanceTests.sql";
    @Test
    public void testDeletion() throws IOException, SQLException, Exception{
        String insertScenarios = "INSERT INTO `scenario` (`id`, `name`, `deleted`, `modelid`, `modelversion`, `datamodelid`, `datamodelversion`) VALUES " +
                "(4, 'Testszenario', 0, 123, 0, 456, 0), " +
                "(5, 'Testszenario', 1, 123, 0, 456, 0), " +
                "(6, 'Testszenario', 0, 123, 0, 456, 0);";
        String insertScenarioInstances ="INSERT INTO `scenarioinstance` (`id`, `terminated`, `scenario_id`) VALUES " +
                "(1, 1, 4), " +
                "(2, 0, 4), " +
                "(3, 1, 5), " +
                "(4, 1, 6), " +
                "(5, 1, 6);";
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new StringReader(insertScenarios));
        runner.runScript(new StringReader(insertScenarioInstances));
        Execution exec = new Execution();
        exec.deleteScenario(4);
        exec.deleteScenario(5);
        exec.deleteScenario(6);
        DbObject dbObject = new DbObject();
        String select = "SELECT deleted FROM scenario WHERE id = 4";
        List<Integer> deleted = dbObject.executeStatementReturnsListInt(select, "deleted");
        Assert.assertEquals("Scenario deleted even though there are still running instances", 0, deleted.get(0).intValue());
        select = "SELECT deleted FROM scenario WHERE id = 5";
        deleted = dbObject.executeStatementReturnsListInt(select, "deleted");
        Assert.assertEquals("Scenario not deleted", 1, deleted.get(0).intValue());
        select = "SELECT deleted FROM scenario WHERE id = 6";
        deleted = dbObject.executeStatementReturnsListInt(select, "deleted");
        Assert.assertEquals("Scenario not deleted", 1, deleted.get(0).intValue());
    }
    /**
     * Refill database from DEVELOPMENT_SQL_SEED_FILE after clearing it.
     * @throws IOException java.io.Exception
     * @throws SQLException java.sql.Exception
     */
    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
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
            conn.close();
        }
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }

}
