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
        logEntry.logDataobjectTransition(0,0,0,0);
        String sql = "SELECT COUNT(*) as count FROM logentry;";
        int logCount = new DbObject().executeStatementReturnsInt(sql, "count");
        assertEquals(1, logCount);
    }
}