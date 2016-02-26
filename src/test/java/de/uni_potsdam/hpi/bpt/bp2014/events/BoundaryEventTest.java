package de.uni_potsdam.hpi.bpt.bp2014.events;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.*;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class BoundaryEventTest {
    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    //
    @Test
    public void testEventDisablementWhenTerminatingActivity() {
        String path = "src/test/resources/EventScenarios/BoundaryEvent.json";
        try {
            ScenarioInstance scenarioInstance = EventTestHelper.createScenarioInstance(path);
            ActivityInstance activity = (ActivityInstance)
                    scenarioInstance.getEnabledControlNodeInstances().get(0);
            activity.begin();
            activity.terminate();
            List<AbstractEvent> registeredEvents = scenarioInstance.getEventsForScenarioInstance();
            assertEquals(0, registeredEvents.size());
        } catch (IOException e) {
            assert(false);
            e.printStackTrace();
        }
    }

    @Test
    public void testActivityCancellation() {
        String path = "src/test/resources/EventScenarios/BoundaryEvent.json";
        try {
            ScenarioInstance scenarioInstance = EventTestHelper.createScenarioInstance(path);
            ActivityInstance activity = (ActivityInstance)
                    scenarioInstance.getEnabledControlNodeInstances().get(0);
            List<AbstractEvent> registeredEvents = scenarioInstance.getEventsForScenarioInstance();
            assertEquals("Boundary event should only be active after beginning activity",
                    0, registeredEvents.size());
            activity.begin();
            registeredEvents = scenarioInstance.getEventsForScenarioInstance();
            BoundaryEvent boundaryEvent = (BoundaryEvent) registeredEvents.get(0);
            boundaryEvent.terminate();
            assertEquals(AbstractStateMachine.STATE.CANCEL, activity.getStateMachine().getState());
        } catch (IOException e) {
            assert(false);
            e.printStackTrace();
        }
    }
}
