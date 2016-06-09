package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbPathMapping;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractEvent;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 */
public class PathMappingDatabaseTest extends JerseyTest {

    WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(EventDispatcher.class);
    }

    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
    }

    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
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
