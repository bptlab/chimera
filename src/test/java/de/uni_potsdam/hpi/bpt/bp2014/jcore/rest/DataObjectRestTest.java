package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class DataObjectRestTest extends AbstractTest {

    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2RESTTest_new.sql";
    }

    private WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(DataObjectRestInterface.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }

    /**
     * Test that only the scenario instance id is needed to access the list of all data objects
     * although the scenario id is wrong.
     */
    @Test
    public void testGetDataObjectsRedirects() {
        Response response = base.path("scenario/9999/instance/72/dataobject").request().get();
        assertEquals("The Response code of getDataObjects was not 200",
                200, response.getStatus());
        assertEquals("getDataObjects returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[1,2],\"results\":{\"1\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/dataobject/1\",\"id\":1,\"label\":\"object1\",\"state\":\"init\"},\"2\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/dataobject/2\",\"id\":2,\"label\":\"object2\",\"state\":\"init\"}}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }

    /**
     * Test that only the scenario instance id is and data object id needed to access the
     * list of all data objects although the scenario id is wrong.
     */
    @Test
    public void testGetDataObjectInstanceRedirect() {
        Response response = base.path("scenario/9999/instance/62/dataobject/1").request().get();
        assertEquals("The Response code of getDataObject was not 200",
                200, response.getStatus());
        assertEquals("getDataObject return a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"label\":\"object1\",\"setId\":0,\"id\":1,\"state\":\"init\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER)
                        .when(Option.IGNORING_EXTRA_FIELDS));
    }

    @Test
    public void testGetDataObjectsInvalidScenarioId() {
        Response response = base.path("scenario/9999/instance/9999/dataobject").request().get();
        assertEquals("The Response code of getDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getDataObjects returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"There is no instance with the id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }


    @Test
    public void testGetDataObjects() {
        Response response = base.path("scenario/1/instance/62/dataobject").request().get();
        assertEquals("The Response code of getDataObjects was not 200",
                200, response.getStatus());
        assertEquals("getDataObjects returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[1,2],\"results\":{\"1\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/62/dataobject/1\",\"id\":1,\"label\":\"object1\",\"state\":\"init\"},\"2\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/62/dataobject/2\",\"id\":2,\"label\":\"object2\",\"state\":\"init\"}}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testGetDataObjectsWithFilter() {
        Response response = base.path("scenario/1/instance/62/dataobject")
                .queryParam("filter", "1").request().get();
        assertEquals("The Response code of getDataObjects was not 200",
                200, response.getStatus());
        assertEquals("getDataObjects returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[1],\"results\":{\"1\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/62/dataobject/1\",\"id\":1,\"label\":\"object1\",\"state\":\"init\"}}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testGetDataObjectNotFound() {
        Response response = base.path("scenario/1/instance/62/dataobject/9999").request().get();
        assertEquals("The Response code of getDataObject was not 404",
                404, response.getStatus());
        assertEquals("getDataObject return a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"There is no dataobject with the id 9999 for the scenario instance 62\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testGetDataObject() {
        Response response = base.path("scenario/1/instance/62/dataobject/1").request().get();
        assertEquals("The Response code of getDataObject was not 200",
                200, response.getStatus());
        assertEquals("getDataObject return a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"label\":\"object1\",\"setId\":0,\"id\":1,\"state\":\"init\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER)
                        .when(Option.IGNORING_EXTRA_FIELDS));
    }

    @Test
    public void testGetDataObjectInvalidInstanceId() {
        Response response = base.path("scenario/1/instance/9999/dataobject/1").request().get();
        assertEquals("The Response code of getDataObject was not 404",
                404, response.getStatus());
        assertEquals("getDataObject return a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"There is no instance with the id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }
}