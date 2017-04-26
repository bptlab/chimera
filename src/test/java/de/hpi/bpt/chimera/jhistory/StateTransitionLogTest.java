package de.hpi.bpt.chimera.jhistory;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.history.DbLogEntry;

/**
 *
 */
public class StateTransitionLogTest extends AbstractDatabaseDependentTest {

    @Test
    public void testTransitionLog() {
        DbLogEntry logEntry = new DbLogEntry();
        int testActivityInstanceId = 1;
        int testScenarioInstanceId = 1;
        logEntry.logActivity(testActivityInstanceId, "init", testScenarioInstanceId);
        logEntry.logActivity(testActivityInstanceId, "running", testScenarioInstanceId);
        logEntry.logActivity(testActivityInstanceId, "terminated", testScenarioInstanceId);

        List<StateTransitionLog> logs =
                StateTransitionLog.getStateTransitions(testScenarioInstanceId, LogEntry.LogType.ACTIVITY);
        assertEquals(3, logs.size());
    }

    @Test
    public void testInitialLog() {
        DbLogEntry logEntry = new DbLogEntry();
        int testActivityInstanceId = 1;
        int testScenarioInstanceId = 1;
        logEntry.logActivity(testActivityInstanceId, "init", testScenarioInstanceId);
        logEntry.logActivity(testActivityInstanceId, "running", testScenarioInstanceId);
        logEntry.logActivity(testActivityInstanceId, "terminated", testScenarioInstanceId);
        List<StateTransitionLog> stateTransitions = StateTransitionLog.getStateTransitions(
                testScenarioInstanceId);
        StateTransitionLog first = stateTransitions.get(0);
        assertEquals(JSONObject.NULL, first.getOldValue());
        assertEquals("init", first.getNewValue());
    }
}