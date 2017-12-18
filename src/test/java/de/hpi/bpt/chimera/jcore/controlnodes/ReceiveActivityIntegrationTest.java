package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.rest.ActivityRestService;
import de.hpi.bpt.chimera.rest.DataObjectRestService;
import de.hpi.bpt.chimera.rest.ScenarioInstanceRestService;
import de.hpi.bpt.chimera.rest.ScenarioRestService;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ReceiveActivityIntegrationTest extends JerseyTest {

    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(EventDispatcher.class, ScenarioInstanceRestService.class,
                ActivityRestService.class, ScenarioRestService.class, DataObjectRestService.class);
    }

    /**
     * Tests whether receive activities can properly be
     */
    @Test
    public void testReceiveActivityViaRest() throws IOException {
        WebTarget base = target();
        String path = "src/test/resources/EventScenarios/ReceiveTaskScenario.json";
        String jsonString = FileUtils.readFileToString(new File(path));
        Response createScenario =
                base.path("interface/v2/scenario").request().post(Entity.json(jsonString));
        assertEquals(201, createScenario.getStatus());

        // Start scenario instance
        Response startScenario = base.path("interface/v2/scenario/1/instance").request().post(null);
        assertEquals(201, startScenario.getStatus());

        Response startFirstActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/2/begin")
                .request().post(Entity.json("{}"));
        assertEquals(202, startFirstActivity.getStatus());

        Response terminateFirstActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/2/terminate")
                .request().post(Entity.json("{}"));
        assertEquals(202, terminateFirstActivity.getStatus());

        Response getEventKeys = base.path(
                "eventdispatcher/scenario/1/instance/1/events").request().get();
        assertEquals(200, getEventKeys.getStatus());
        JSONArray jsonArray = new JSONArray(getEventKeys.readEntity(String.class));
        String eventKey = jsonArray.getString(0);

        // Before activating the receive task the state should be init
        Response checkDataObjectBeforeTrigger = base.path(
                "interface/v2/scenario/1/instance/1/dataobject/1").request().get();
        assertEquals(200, checkDataObjectBeforeTrigger.getStatus());
        JSONObject dataObject = new JSONObject(checkDataObjectBeforeTrigger.readEntity(String.class));
        assertEquals("init", dataObject.getString("state"));

        String route = String.format("eventdispatcher/scenario/%d/instance/%d/events/%s", 1,
                1, eventKey);
        Response triggerEvent = base.path(route).request().post(null);
        assertEquals(202, triggerEvent.getStatus());

        Response checkAfterTriggerDataObject = base.path(
                "interface/v2/scenario/1/instance/1/dataobject/1").request().get();
        assertEquals(200, checkAfterTriggerDataObject .getStatus());
        dataObject = new JSONObject(checkAfterTriggerDataObject .readEntity(String.class));
        assertEquals("received", dataObject.getString("state"));
    }
}
