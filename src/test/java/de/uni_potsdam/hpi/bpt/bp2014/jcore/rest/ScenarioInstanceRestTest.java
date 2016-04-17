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
import javax.ws.rs.core.UriInfo;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class ScenarioInstanceRestTest extends AbstractTest {
    static Logger log = Logger.getLogger(RestInterfaceTest.class);

    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2RESTTest_new.sql";
    }
    /**
     * The base url of the jcore rest interface.
     * Allows us to send requests to the {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface}.
     */
    private WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(ScenarioInstanceRestService.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }

    /**

     * with a correct scenario id and a correct instance id
     * the respond will be a 200 with a JSONObject
     */
    @Test
    public void testGetScenarioInstanceReturnsJSON() {
        Response response = base.path("scenario/1/instance/72").request().get();
        assertEquals("The Response code of getScenarioInstance was not 200",
                200, response.getStatus());
        assertEquals("getScenarioInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"name\":\"HELLOWORLD\",\"id\":72,\"terminated\":false,\"scenario_id\":1,\"activities\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }


    /**
     * then the Response will be a 201 and a json object wit the new id will be returned.
     */
    @Test
    public void testStartInvalidInstanceWName() {
        NamedJaxBean newName = new NamedJaxBean();
        newName.setName("Dies ist ein Test");
        Response response = base.path("scenario/9999/instance").request()
                .put(Entity.json(newName));
        assertEquals("The Response code of start new instances was not 400",
                400, response.getStatus());
        assertEquals("Start new isntance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The Scenario could not be found!\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with an invalid instance id
     * then the Response should be a 404 with an error message.
     */
    @Test
    public void terminateScenarioInstanceInvalidId() {
        Response response = base.path("scenario/1/instance/9999/terminate").request().post(Entity.json(null));
        assertEquals("The Response code of terminating an instances was not 404",
                404, response.getStatus());
        assertEquals("The Media type of terminating an instance was not TEXT",
                MediaType.TEXT_PLAIN, response.getMediaType().toString());
        assertEquals("The content of the response was not as expected",
                "Scenario or scenario instance does not exist",
                response.readEntity(String.class));
    }

    /**
     * with a valid scenario id and a filter
     * only instances with names containing this string will be returned.
     */
    @Test
    public void testGetScenarioInstancesWithFilter() {
        Response response = base.path("scenario/1/instance")
                .queryParam("filter", "noInstanceLikeThis").request().get();
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[],\"labels\":{},\"links\":{}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }


    /**
     * then the Response will be a 201 and a json object wit the new id will be returned.
     */
    @Test
    public void testStartInvalidInstanceWOName() {
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

    /**
     * then the Response will be a 201 and a json object wit the new id will be returned.
     */
    @Test
    public void testStartNewInstanceWOName() {
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


    /**
     * then the Response will be a 201 and a json object wit the new id will be returned.
     */
    @Test
    public void testStartNewInstanceWName() {
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
     * with a wrong scenario id and a correct instance id
     * the respond will be a 200 with a redirected URI.
     */
    @Test
    public void testGetScenarioInstanceWithWrongScenarioRedirects() {
        Response response = base.path("scenario/9999/instance/72").request().get();
        assertEquals("The Response code of getScenarioInstance was not 200",
                200, response.getStatus());
        assertEquals("getScenarioInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"name\":\"HELLOWORLD\",\"id\":72,\"terminated\":false,\"scenario_id\":1,\"activities\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with an valid scenario instance id
     * the scenario should be terminated and the response is a 201.
     */
    @Test
    public void terminateScenarioInstance() {
        Response response = base.path("scenario/1/instance/47/terminate").request().post(Entity.json(null));
        assertEquals("The Response code of terminating an instances was not 200",
                200, response.getStatus());
    }



    /**
     * with valid params and no filter
     * then you get 200 a JSON Object.
     */
    @Test
    public void testGetScenarioInstancesReturnsOkAndJSON() {
        Response response = base.path("scenario/1/instance").request().get();
        assertEquals("The Response code of get get instances was not 200",
                200, response.getStatus());
        assertEquals("Get instances returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * with an invalid scenario
     * then you get 404 with an error message inside the returned JSON object
     */
    @Test
    public void testGetScenarioInstancesInvalidScenario() {
        Response response = base.path("scenario/9999/instance").request().get();
        assertEquals("The Response code of get get instances was not 404",
                404, response.getStatus());
        assertEquals("Get instances returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON is invalid or does not contain the expected message",
                "{\"error\":\"Scenario not found!\"}",
                jsonEquals(response.readEntity(String.class)));
    }

}
