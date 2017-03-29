package de.hpi.bpt.chimera.jcore.eventhandling;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class EventDataCreationTest extends JerseyTest {
    WebTarget base;


    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
    }


    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(EventDispatcher.class);
    }


    @Test
    public void testCreateDataObject() throws IOException {
        String path = "src/test/resources/core/EventWrite.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Before Event", scenarioInstance);
        ScenarioTestHelper.triggerEventInScenario(scenarioInstance, base, getExampleJson());
        scenarioInstance = new ScenarioInstance(scenarioInstance.getScenarioId(),
                scenarioInstance.getId());
        List<DataObject> dataObjects = scenarioInstance.getDataManager().getDataObjects();
        assertEquals(1, dataObjects.size());
        assertEquals("1", dataObjects.get(0).getDataAttributeInstances().get(0).getValue());

        ScenarioTestHelper.executeActivityByName("Before Event", scenarioInstance);
        ScenarioTestHelper.triggerEventInScenario(scenarioInstance, base, getExampleJson());
        scenarioInstance = new ScenarioInstance(scenarioInstance.getScenarioId(),
                scenarioInstance.getId());
        dataObjects = scenarioInstance.getDataManager().getDataObjects();
        assertEquals(2, dataObjects.size());
    }

    private String getExampleJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("age", 1);
        return jsonObject.toString();
    }
}
