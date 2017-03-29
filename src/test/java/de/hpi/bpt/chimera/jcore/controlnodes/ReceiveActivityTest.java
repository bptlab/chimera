package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.database.data.DbState;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class ReceiveActivityTest extends JerseyTest {
    WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(EventDispatcher.class);
    }

    @Before
    public void setUpBase() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
        base = target("eventdispatcher");
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        AbstractDatabaseDependentTest.resetDatabase();
    }


    /**
     * The tested scenario is a linear sequence of the activities "before event" and "receive event"
     * receive event is a message receive task which changes the state of the
     * data object with the name aClass from init to received.
     */
    @Test
    public void testReceiveActivityTask() throws IOException {
        String path = "src/test/resources/EventScenarios/ReceiveTaskScenario.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("before event", scenarioInstance);
        DataManager dataManager = scenarioInstance.getDataManager();
        // Since only one data object is present in the scenario
        DataObject dataObject = dataManager.getDataObjects().get(0);
        Assert.assertEquals("init", new DbState().getStateName(dataObject.getStateId()));

        ScenarioTestHelper.triggerEventInScenario(scenarioInstance, base);

        dataManager.loadFromDatabase();
        dataObject = dataManager.getDataObjects().get(0);

        assertEquals("received", new DbState().getStateName(dataObject.getStateId()));
    }
}