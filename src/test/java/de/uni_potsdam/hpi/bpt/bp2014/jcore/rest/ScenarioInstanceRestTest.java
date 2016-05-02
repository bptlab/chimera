package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.NamedJaxBean;
import net.javacrumbs.jsonunit.core.Option;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
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
public class ScenarioInstanceRestTest extends AbstractTest {
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2RESTTest_new.sql";
    }

    private WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(ScenarioInstanceRestService.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }


    @Test
    public void testGetScenarioInstance() {
        Response response = base.path("scenario/1/instance/72").request().get();
        assertThat("The returned JSON does not contain the expected content",
                "{\"name\":\"HELLOWORLD\",\"id\":72,\"terminated\":false,\"scenario_id\":1,\"activities\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testGetScenarioInstancesReturnJson() {
        Response response = base.path("scenario/1/instance").request().get();
        assertEquals("Get instances returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    @Test
    public void testGetScenarioInstanceReturnCode() {
        Response response = base.path("scenario/1/instance/72").request().get();
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
                "{\"error\":\"The Scenario could not be found!\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }


    @Test
    public void testGetScenarioInstancesWithFilter() {
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
        assertEquals("The Response code of terminateScenarioInstance was not 404",
                404, response.getStatus());
        assertEquals("Get terminateScenarioInstance does not return TEXT",
                MediaType.TEXT_PLAIN, response.getMediaType().toString());
        assertEquals("The returned TEXT does not contain the expected content",
                "Scenario or scenario instance does not exist",
                response.readEntity(String.class));
    }

    @Test
    public void testTerminateInvalidInstanceId() {
        Response response = base.path("scenario/1/instance/9999/terminate").request().post(Entity.json(null));
        assertEquals("The Response code of terminating an instances was not 404",
                404, response.getStatus());
        assertEquals("The Media type of terminating an instance was not TEXT",
                MediaType.TEXT_PLAIN, response.getMediaType().toString());
        assertEquals("The content of the response was not as expected",
                "Scenario or scenario instance does not exist",
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
                "{\"error\":\"The Scenario could not be found!\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testStartNewInstance() {
        Response response = base.path("scenario/1/instance").request().post(null);
        assertEquals("The Response code of start new instances was not 201",
                201, response.getStatus());
        assertEquals("Start new isntance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"id\":966,\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/966\"}",
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
        assertThat("The returned JSON does not contain the expected content",
                "{\"id\":966,\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/966\"}",
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
    public void testTerminateScenarioInstance() {
        Response response = base.path("scenario/1/instance/72/terminate")
                .request().post(Entity.json(null));
        assertEquals("The Response code of terminateScenarioInstance was not 200",
                200, response.getStatus());
        assertEquals("terminateScenarioInstance does not return a TEXT",
                MediaType.TEXT_PLAIN, response.getMediaType().toString());
        assertEquals("The returned TEXT does not contain the expected content",
                "Instance has been terminated",
                response.readEntity(String.class));
    }

    @Test
    public void testGetScenarioInstancesInvalidScenarioId() {
        Response response = base.path("scenario/9999/instance").request().get();
        assertEquals("The Response code of get get instances was not 404",
                404, response.getStatus());
        assertEquals("Get instances returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON is invalid or does not contain the expected message",
                "{\"error\":\"Scenario not found!\"}",
                jsonEquals(response.readEntity(String.class)));
    }

    @Test
    public void testGetScenarioInstanceInvalidInstanceId() {
        Response response = base.path("scenario/9999/instance/9999").request().get();
        assertEquals("The Response code of getScenarioInstance was not 404",
                404, response.getStatus());
        assertEquals("getScenarioInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"There is no instance with the id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }
}
