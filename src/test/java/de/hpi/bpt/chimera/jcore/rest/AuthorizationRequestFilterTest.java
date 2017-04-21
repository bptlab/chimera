package de.hpi.bpt.chimera.jcore.rest;


import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.database.DbScenarioInstance;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcore.rest.filters.AuthorizationRequestFilter;

public class AuthorizationRequestFilterTest extends AbstractTest {

    private WebTarget base;
    private static Connector connector;

    private static int scenarioId;
    private static int instanceId;
    private static int fragmentId;
    private static int controlNodeId;

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig(DataDependencyRestService.class, ActivityRestService.class);
        config.register(AuthorizationRequestFilter.class);
        return config;
    }

    @Before
    public void setUpBaseAndInitialize() {
        base = target("interface/v2");
        connector = new Connector();
        scenarioId = connector.insertScenario("scenario1", 1);
        instanceId = new DbScenarioInstance().createNewScenarioInstance(scenarioId);
        fragmentId = connector.insertFragment("fragment1", scenarioId, 1);
        controlNodeId = connector.insertControlNode("activity", "Activity", fragmentId, "aModelId");
    }

    @Test
    public void testInvalidScenarioIdGet() {
        Response response = base.path("scenario/9999/instance/1/activity/1/availableInput").request().get();
        assertEquals(404, response.getStatus());
        assertEquals("{\"error\":\"There is no scenario with id 9999\"}", response.readEntity(String.class));
    }

    @Test
    public void testInvalidScenarioIdPost() {
        Response response = base.path("scenario/9999/instance/1/activityinstance/1/begin")
                .request().post(Entity.json("{}"));
        assertEquals(400, response.getStatus());
        assertEquals("{\"error\":\"There is no scenario with id 9999\"}", response.readEntity(String.class));
    }

    @Test
    public void testInvalidScenarioInstanceIdGet() {
        Response response = base.path("scenario/" + scenarioId + "/instance/9999/activity/1/availableInput").request().get();
        assertEquals(404, response.getStatus());
        assertEquals("{\"error\":\"There is no scenario instance with id 9999\"}", response.readEntity(String.class));
    }

    @Test
    public void testInvalidScenarioInstanceIdPost() {
        Response response = base.path("scenario/" + scenarioId + "/instance/9999/activityinstance/1/begin")
                .request().post(Entity.json("{}"));
        assertEquals(400, response.getStatus());
        assertEquals("{\"error\":\"There is no scenario instance with id 9999\"}", response.readEntity(String.class));
    }

    @Test
    public void testInvalidActivityIdGet() {
        Response response = base.path("scenario/" + scenarioId + "/instance/" + instanceId
                + "/activity/9999/availableInput").request().get();
        assertEquals(404, response.getStatus());
        assertEquals("{\"error\":\"There is no activity with id 9999\"}", response.readEntity(String.class));
    }

    @Test
    public void testInvalidActivityInstanceIdGet() {
        Response response = base.path("scenario/" + scenarioId + "/instance/" + instanceId
                + "/activityinstance/9999/input").request().get();
        assertEquals(404, response.getStatus());
        assertEquals("{\"error\":\"There is no activity instance with id 9999\"}", response.readEntity(String.class));
    }

    @Test
    public void testInvalidActivityInstanceIdPost() {
        Response response = base.path("scenario/" + scenarioId + "/instance/" + instanceId
                + "/activityinstance/9999/begin").request().post(Entity.json("{}"));
        assertEquals(400, response.getStatus());
        assertEquals("{\"error\":\"There is no activity instance with id 9999\"}", response.readEntity(String.class));
    }



}
