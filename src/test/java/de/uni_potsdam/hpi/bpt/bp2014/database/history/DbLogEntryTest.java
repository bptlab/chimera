package de.uni_potsdam.hpi.bpt.bp2014.database.history;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.LogEntry;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
        logEntry.logActivity(0, "running", 0);
        String sql = "SELECT COUNT(*) as count FROM logentry;";
        int logCount = new DbObject().executeStatementReturnsInt(sql, "count");
        assertEquals(1, logCount);
    }

    @Test
    public void testLogDataAttributeTransition()  {
        DbLogEntry logEntry = new DbLogEntry();
        // Make log entry with dummy values
        logEntry.logDataAttributeTransition(0, "testValue", 0, 0);
        String sql = "SELECT COUNT(*) as count FROM logentry;";
        int logCount = new DbObject().executeStatementReturnsInt(sql, "count");
        assertEquals(1, logCount);
    }

    @Test
    public void testCreateEventLogEntry() {
        DbLogEntry logEntry = new DbLogEntry();
        logEntry.logEvent(0, 0, "received");
        String sql = "SELECT COUNT(*) as count FROM logentry;";
        int logCount = new DbObject().executeStatementReturnsInt(sql, "count");
        assertEquals(1, logCount);
    }

    @Test
    public void testGetLogEntriesForScenarioInstance() {
        DbLogEntry logEntry = new DbLogEntry();
        logEntry.logDataObjectTransition(0,0,0,0);
        String sql = "SELECT COUNT(*) as count FROM logentry;";
        int logCount = new DbObject().executeStatementReturnsInt(sql, "count");
        assertEquals(1, logCount);
    }

    @Test
    public void testGetCreationLogs() {
        DbLogEntry logEntry = new DbLogEntry();
        int dummyActivityId = 1;
        int scenarioInstanceId = 1;
        int dummyDataClassId = 1;
        logEntry.logActivity(dummyActivityId, "init", scenarioInstanceId);
        logEntry.logActivity(dummyActivityId, "running", scenarioInstanceId);

        int dummyDataobjectId = 2;
        int stateId = new Connector().insertStateIntoDatabase("received", dummyDataClassId);
        logEntry.logDataObjectCreation(dummyDataobjectId, stateId, scenarioInstanceId);

        int dummyAttributeId = 3;
        logEntry.logDataAttributeCreation(dummyAttributeId, "foo", scenarioInstanceId);
        logEntry.logDataAttributeTransition(dummyAttributeId, "bar", 1, scenarioInstanceId);
        logEntry.logDataAttributeTransition(dummyAttributeId, "val", 4, scenarioInstanceId);

        List<LogEntry> creationLogEntries = logEntry.getCreationLogEntries(scenarioInstanceId);
        assertEquals(3, creationLogEntries.size());
        assertEquals("init", creationLogEntries.get(0).getNewValue());
        assertEquals("received", creationLogEntries.get(1).getNewValue());
        assertEquals("foo", creationLogEntries.get(2).getNewValue());
    }
}