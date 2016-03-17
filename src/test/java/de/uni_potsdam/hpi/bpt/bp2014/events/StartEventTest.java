package de.uni_potsdam.hpi.bpt.bp2014.events;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.StartEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 */
public class StartEventTest {
    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
	public void testStartEventRegistration() {
        String path = "src/test/resources/EventScenarios/StartEventWithQuery.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            List<AbstractControlNodeInstance> controlNodeInstances =
                    scenarioInstance.getEnabledControlNodeInstances();
            assert (controlNodeInstances.size() == 0);
            List<AbstractEvent> events = scenarioInstance.getEventsForScenarioInstance();
            assert (events.get(0) instanceof StartEvent);
        } catch (IOException e) {
            Assert.fail();
        }
    }


    // If the start event has no event query, the next control node should be activated
    // from the beginning.
    @Test
    public void testStartQueryWithoutEvent() {
        String path = "src/test/resources/EventScenarios/StartEventWithoutQuery.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            List<AbstractControlNodeInstance> controlNodeInstances =
                    scenarioInstance.getEnabledControlNodeInstances();
            ActivityInstance firstActivity = (ActivityInstance) controlNodeInstances.get(0);
            assert (firstActivity.getLabel().equals("Do something"));
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
