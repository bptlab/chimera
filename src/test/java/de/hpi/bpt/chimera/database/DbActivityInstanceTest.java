package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.controlnodes.DbActivityInstance;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class DbActivityInstanceTest {
    int scenarioInstanceId = 1;
    int nonExistingScenarioInstanceId = 100;
    int activityInstanceId = 1;
    int nonExistingActivityInstanceId = 100;
    int fragmentId = 1;
    int controlNodeId = 1;


    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testGetTerminatedActivitiesForScenarioInstance(){
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        dbControlNodeInstance.createNewControlNodeInstance(controlNodeId, "Activity",
                fragmentId, State.TERMINATED);
        dbActivityInstance.createNewActivityInstance(activityInstanceId, "HumanTask");

        DbFragmentInstance dbFragmentInstance = new DbFragmentInstance();
        dbFragmentInstance.createNewFragmentInstance(fragmentId, scenarioInstanceId);
        List<Integer> terminatedActivityInstances = dbActivityInstance.
                getTerminatedActivitiesForScenarioInstance(scenarioInstanceId);
        assertEquals(1, terminatedActivityInstances.size());
    }

    @Test
    public void testExistActivityInstance(){
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        dbControlNodeInstance.createNewControlNodeInstance(controlNodeId, "Activity",
                fragmentId, State.INIT);
        DbFragmentInstance dbFragmentInstance = new DbFragmentInstance();
        dbFragmentInstance.createNewFragmentInstance(fragmentId, scenarioInstanceId);
        dbActivityInstance.createNewActivityInstance(activityInstanceId, "HumanTask");

        assertTrue(dbActivityInstance.existsActivityInstance(activityInstanceId,
                scenarioInstanceId));
        assertFalse(dbActivityInstance.existsActivityInstance(activityInstanceId,
                nonExistingScenarioInstanceId));
        assertFalse(dbActivityInstance.existsActivityInstance(nonExistingActivityInstanceId,
                nonExistingScenarioInstanceId));
    }

}
