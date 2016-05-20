package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.junit.After;
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
     * The tested scenario is a linear sequence of the activities BeforeReceiveTask, reveiceEvent
     * and AfterReceiveTask. receiveEvent is a message receive task which changes the state of the
     * data object with the name Data from init to changed.
     */
    @Test
    public void testReceiveActivityTask() throws IOException {
        String path = "src/test/resources/Scenarios/ReceiveTaskScenario.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.beginActivityByName("BeforeReceiveTask", scenarioInstance);
        ScenarioTestHelper.terminateActivityInstanceByName("BeforeReceiveTask", scenarioInstance);
        DataManager dataManager = scenarioInstance.getDataManager();
        // Since only one data object is present in the scenario
        DataObject dataObject = dataManager.getDataObjects().get(0);
        assertEquals("init", new DbState().getStateName(dataObject.getStateId()));

        List<AbstractEvent> events = scenarioInstance.getEventsForScenarioInstance();
        assertEquals(1, events.size());
        events.get(0).terminate();

        dataObject = dataManager.getDataObjects().get(0);
        assertEquals("changed", new DbState().getStateName(dataObject.getStateId()));
    }
}