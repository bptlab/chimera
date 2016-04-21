package de.uni_potsdam.hpi.bpt.bp2014.database.history;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 *
 */
public class DbLogEntryTest {

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testLogActivity() throws Exception {
        DbLogEntry logEntry = new DbLogEntry();
        int testActivityId = 1;
        int testScenarioInstanceId = 1;
        logEntry.logActivity(testActivityId, "running", testScenarioInstanceId);
        String sql = "SELECT COUNT(*) as count FROM logentry;";
        int logCount = new DbObject().executeStatementReturnsInt(sql, "count");
        assertEquals(1, logCount);
    }

    @Test
    public void testLogDataAttributeTransition()  {
        DbLogEntry logEntry = new DbLogEntry();
        int testDataAttributeInstanceId = 1;
        String testValue = "testValue";
        int testNodeInstanceId = 1;
        int testScenarioInstanceId = 1;
        logEntry.logDataAttributeTransition(testDataAttributeInstanceId,
                testValue, testNodeInstanceId, testScenarioInstanceId);
        String sql = "SELECT COUNT(*) as count FROM logentry;";
        int logCount = new DbObject().executeStatementReturnsInt(sql, "count");
        assertEquals(1, logCount);
    }

    @Test
    public void testGetLogEntriesForScenarioInstance() {

    }
}