package de.hpi.bpt.chimera.events;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractEvent;
import de.hpi.bpt.chimera.jcore.controlnodes.BoundaryEvent;

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
        String path = "src/test/resources/Scenarios/BoundaryEvent.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
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
        String path = "src/test/resources/Scenarios/BoundaryEvent.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            ActivityInstance activity = (ActivityInstance)
                    scenarioInstance.getEnabledControlNodeInstances().get(0);
            List<AbstractEvent> registeredEvents = scenarioInstance.getEventsForScenarioInstance();
            assertEquals("Boundary event should only be active after beginning activity",
                    0, registeredEvents.size());
            activity.begin();
            registeredEvents = scenarioInstance.getEventsForScenarioInstance();
            BoundaryEvent boundaryEvent = (BoundaryEvent) registeredEvents.get(0);
            boundaryEvent.terminate();
            assertEquals(State.CANCEL, activity.getState());
            assertEquals(scenarioInstance.getEnabledControlNodeInstances().size(), 1);

        } catch (IOException e) {
            assert(false);
            e.printStackTrace();
        }
    }
}
