package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.database.DbScenarioInstance;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.rest.ScenarioInstanceRestService;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.NamedJaxBean;
import de.hpi.bpt.chimera.rest.filters.AuthorizationRequestFilter;
import net.javacrumbs.jsonunit.core.Option;
import net.minidev.json.JSONObject;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Test methods in the {@link ScenarioInstanceRestService} for status codes, return types
 * and correct answers. Affects database while doing this.
 */
public class ScenarioInstanceRestTest extends JerseyTest {

    private WebTarget base;
    private int scenarioId;
    private int scenarioInstanceId;
    String scenarioName = "testScenario";

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig(ScenarioInstanceRestService.class);
        config.register(AuthorizationRequestFilter.class);
        return config;
    }

    @Before
    public void setup() {
        Connector connector = new Connector();
        scenarioId = connector.insertScenario(scenarioName, 1);
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        scenarioInstanceId = dbScenarioInstance.createNewScenarioInstance(
                scenarioId, scenarioName);
        base = target("interface/v2");
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testGetScenarioInstance() {
        String path = String.format("scenario/%d/instance/%d", scenarioId, scenarioInstanceId);
        Response response = base.path(path).request().get();
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("name", scenarioName);
        expectedResponse.put("id", scenarioInstanceId);
        expectedResponse.put("terminated", false);
        expectedResponse.put("scenario_id", 1);
        String linkToActivities = String.format(
                "http://localhost:9998/interface/v2/scenario/%d/instance/%d/activity",
                scenarioId,
                scenarioInstanceId);
        expectedResponse.put("activities", linkToActivities);
        assertThat(expectedResponse.toJSONString(),
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testGetScenarioInstancesReturnJson() {
        String path = String.format("scenario/%d/instance", scenarioId);
        Response response = base.path(path).request().get();
        assertEquals("Get instances returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    @Test
    public void testGetScenarioInstanceReturnCode() {
        String path = String.format("scenario/%d/instance/%d", scenarioId, scenarioInstanceId);
        Response response = base.path(path).request().get();
        assertEquals("The Response code of getScenarioInstance was not 200",
                200, response.getStatus());
    }


    @Test
    public void testStartInvalidInstanceWName() {
        NamedJaxBean newName = new NamedJaxBean();
        newName.setName("Dies ist ein Test");
        Response response = base.path("scenario/9999/instance").request()
                .put(Entity.json(newName));
        // Starting a scenario instance with a non existing name is a bad request
        assertEquals("The Response code of start new instances was not 400",
                400, response.getStatus());
        assertEquals("Start new isntance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"There is no scenario with id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }


    @Test
    public void testGetScenarioInstancesWithInvalidFilter() {
        Response response = base.path("scenario/1/instance")
                .queryParam("filter", "noInstanceLikeThis").request().get();
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[],\"labels\":{},\"links\":{}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testTerminateInvalidScenarioId() {
        Response response = base.path("scenario/9999/instance/72/terminate")
                .request().post(Entity.json(null));
        assertEquals("The Response code of terminateScenarioInstance was not 400",
                400, response.getStatus());
        assertEquals("Get terminateScenarioInstance does not return TEXT",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals("The returned TEXT does not contain the expected content",
                "{\"error\":\"There is no scenario with id 9999\"}",
                response.readEntity(String.class));
    }

    @Test
    public void testTerminateInvalidInstanceId() {
        int nonExistingInstanceId = 9999;
        String path = String.format(
                "scenario/%d/instance/%d/terminate", scenarioId, nonExistingInstanceId);
        Response response = base.path(path).request().post(Entity.json(null));
        assertEquals("The Response code of terminating an instances was not 400",
                400, response.getStatus());
        assertEquals("The Media type of terminating an instance was not TEXT",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals("The content of the response was not as expected",
                "{\"error\":\"There is no scenario instance with id 9999\"}",
                response.readEntity(String.class));
    }

    @Test
    public void testStartInstanceInvalidId() {
        Response response = base.path("scenario/9999/instance").request().post(null);
        assertEquals("The Response code of start new instances was not 400",
                400, response.getStatus());
        assertEquals("Start new isntance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"There is no scenario with id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testStartNewInstance() {
        Response response = base.path("scenario/1/instance").request().post(null);
        assertEquals("The Response code of start new instances was not 201",
                201, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        JSONObject expectedResponse = new JSONObject();
        // Use the fact that ids are auto increment
        int newScenarioInstanceId = scenarioInstanceId + 1;
        expectedResponse.put("id", newScenarioInstanceId);
        String pathToNewlyCreatedResouce = String.format(
                "http://localhost:9998/interface/v2/scenario/%d/instance/%d",
                scenarioId,
                newScenarioInstanceId);
        expectedResponse.put("link", pathToNewlyCreatedResouce);
        assertThat(expectedResponse.toJSONString(),
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }


    @Test
    public void testStartNewNamedInstance() {
        NamedJaxBean newName = new NamedJaxBean();
        newName.setName("Dies ist ein Test");
        Response response = base.path("scenario/1/instance")
                .request().put(Entity.json(newName));
        assertEquals("The Response code of start new instances was not 201",
                201, response.getStatus());
        assertEquals("Start new instance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        JSONObject expectedResponse = new JSONObject();
        // Use the fact that ids are auto increment
        int newScenarioInstanceId = scenarioInstanceId + 1;
        expectedResponse.put("id", newScenarioInstanceId);
        String pathToNewlyCreatedResouce = String.format(
                "http://localhost:9998/interface/v2/scenario/%d/instance/%d",
                scenarioId,
                newScenarioInstanceId);
        expectedResponse.put("link", pathToNewlyCreatedResouce);
        assertThat(expectedResponse.toJSONString(),
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Even if the scenarioId is not valid, the redirection of instances works correctly
     */
    @Test
    public void testGetScenarioInstanceNotFoundInvalidScenarioId() {
        Response response = base.path("scenario/9999/instance/72").request().get();
        assertEquals("The Response code of getScenarioInstance was not 200",
                404, response.getStatus());
        assertEquals("getScenarioInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    @Test
    public void testGetScenarioInstancesInvalidScenarioId() {
        Response response = base.path("scenario/9999/instance").request().get();
        assertEquals("The Response code of get get instances was not 404",
                404, response.getStatus());
        assertEquals("Get instances returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON is invalid or does not contain the expected message",
                "{\"error\":\"There is no scenario with id 9999\"}",
                jsonEquals(response.readEntity(String.class)));
    }

    @Test
    public void testGetScenarioInstanceInvalidInstanceId() {
        Response response = base.path("scenario/1/instance/9999").request().get();
        assertEquals("The Response code of getScenarioInstance was not 404",
                404, response.getStatus());
        assertEquals("getScenarioInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"There is no scenario instance with id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }
}
