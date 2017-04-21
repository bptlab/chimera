package de.hpi.bpt.chimera.jcore.eventhandling;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataObject;

/**
 *
 */
public class EventDataCreationTest extends AbstractTest {
    WebTarget base;


    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
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
