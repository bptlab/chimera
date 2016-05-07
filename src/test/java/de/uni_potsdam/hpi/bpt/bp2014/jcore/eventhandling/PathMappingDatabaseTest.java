package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbEventMapping;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbPathMapping;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.EventFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
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
    public void testSavingOfPathMapping() {
        try {
            ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(
                    "src/test/resources/EventScenarios/EventDataSavingScenario.json");

            int eventNodeId = ScenarioTestHelper.triggerEventInScenario(
                    instance, base, "{ \"foo\": \"bar\"}");

            DbPathMapping dbPathMapping = new DbPathMapping();
            Map<Integer, String> paths = dbPathMapping
                    .getPathsForAttributesOfControlNode(eventNodeId);

            assertTrue(paths.values().contains("$.foo"));

        } catch (IOException e) {
            fail("Failed to read scenario json: " + e.getMessage());
        }
    }

    @Test
    public void testSavingOfEventData() {
        try {
            ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(
                    "src/test/resources/EventScenarios/EventDataSavingScenario.json");

            int eventNodeId = ScenarioTestHelper.triggerEventInScenario(
                    instance, base, "{ \"foo\": \"bar\"}");
            DbDataAttributeInstance dbDataAttributeInstance = new DbDataAttributeInstance();

            DbPathMapping dbPathMapping = new DbPathMapping();
            List<Integer> attributeIds = dbPathMapping
                    .getAttributeIdsForControlNode(eventNodeId);
            boolean isValueSaved = false;
            for (Integer attributeId : attributeIds) {
                if ("bar".equals(dbDataAttributeInstance.getValue(attributeId))) {
                    isValueSaved = true;
                    break;
                }
            }
            assertTrue(isValueSaved);
        } catch (IOException e) {
            fail("Failed to read scenario json: " + e.getMessage());
        }
    }


}
