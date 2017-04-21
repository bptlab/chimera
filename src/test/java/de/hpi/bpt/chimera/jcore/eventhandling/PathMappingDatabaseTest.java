package de.hpi.bpt.chimera.jcore.eventhandling;

import static junit.framework.TestCase.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.database.DbPathMapping;
import de.hpi.bpt.chimera.database.data.DbDataAttributeInstance;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractEvent;

/**
 *
 */
public class PathMappingDatabaseTest extends AbstractTest {

    WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(EventDispatcher.class);
    }

    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
    }

    @Test
    public void testSavingOfPathMapping() throws IOException {
        // TODO also use EventLoggingScenario
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(
                "src/test/resources/EventScenarios/EventDataSavingScenario.json");

        int eventNodeId = ScenarioTestHelper.triggerEventInScenario(
                instance, base, "{ \"foo\": \"bar\"}");

        DbPathMapping dbPathMapping = new DbPathMapping();
        Map<Integer, String> paths = dbPathMapping
                .getPathsForAttributesOfControlNode(eventNodeId);

        assertTrue(paths.values().contains("$.foo"));
    }

    @Test
    public void testSavingOfEventData() throws IOException {
        String path = "src/test/resources/EventScenarios/EventLoggingScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Before Event", instance);
        List<AbstractEvent> events =  instance.getEventsForScenarioInstance();
        assert (events.size() == 1): "Event was not registered properly";
        String json = FileUtils.readFileToString(
                new File("src/test/resources/history/exampleWebserviceJson.json"));
        int eventNodeId = ScenarioTestHelper.triggerEventInScenario(instance, base, json);

        DbDataAttributeInstance dbDataAttributeInstance = new DbDataAttributeInstance();
        DbPathMapping dbPathMapping = new DbPathMapping();
        List<Integer> attributeIds = dbPathMapping
                .getAttributeIdsForControlNode(eventNodeId);
        boolean isValueSaved = false;
        for (Integer attributeId : attributeIds) {
            if ("aName".equals(dbDataAttributeInstance.getValue(attributeId))) {
                isValueSaved = true;
                break;
            }
        }
        assertTrue(isValueSaved);
    }
}
