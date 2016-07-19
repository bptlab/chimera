package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.database.data.DbState;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class ReceiveActivityTest {

    @After
    public void resetDatabase() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    /**
     * TODO remodel scenario
     * The tested scenario is a linear sequence of the activities BeforeReceiveTask, reveiceEvent
     * and AfterReceiveTask. receiveEvent is a message receive task which changes the state of the
     * data object with the name Data from init to changed.
     */
    @Test
    public void testReceiveActivityTask() throws IOException {
        String path = "src/test/resources/Scenarios/ReceiveTaskScenario.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.beginActivityByName("BeforeReceiveTask", scenarioInstance);
        ScenarioTestHelper.terminateActivityByName("BeforeReceiveTask", scenarioInstance);
        DataManager dataManager = scenarioInstance.getDataManager();
        // Since only one data object is present in the scenario
        DataObject dataObject = dataManager.getDataObjects().get(0);
        Assert.assertEquals("init", new DbState().getStateName(dataObject.getStateId()));

        List<AbstractEvent> events = scenarioInstance.getEventsForScenarioInstance();
        assertEquals(1, events.size());
        events.get(0).terminate();

        dataObject = dataManager.getDataObjects().get(0);
        assertEquals("changed", new DbState().getStateName(dataObject.getStateId()));
    }
}