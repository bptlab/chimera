package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class StateTransitionLogTest {
    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testTransitionLog() {
        DbLogEntry logEntry = new DbLogEntry();
        int testActivityInstanceId = 1;
        int testScenarioInstanceId = 1;
        logEntry.logActivity(testActivityInstanceId, "init", testScenarioInstanceId);
        logEntry.logActivity(testActivityInstanceId, "running", testScenarioInstanceId);
        logEntry.logActivity(testActivityInstanceId, "terminated", testScenarioInstanceId);

        List<StateTransitionLog> logs =
                StateTransitionLog.getStateTransitons(testScenarioInstanceId, LogEntry.LogType.ACTIVITY);
        assertEquals(2, logs.size());
    }
}