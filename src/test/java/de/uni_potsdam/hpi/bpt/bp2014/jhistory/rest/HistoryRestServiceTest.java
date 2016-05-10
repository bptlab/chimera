package de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.sql.SQLException;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


/**
 * This Class extends the {@link de.uni_potsdam.hpi.bpt.bp2014.AbstractTest}
 * to test the HistoryRestService of the JEngine core.
 * In order to do so it uses the functionality of the
 * {@link org.glassfish.jersey.test.JerseyTest}
 * There are test methods for every possible REST Call.
 * In order to stay independent from existing tests, the
 * database will be set up before and after the execution.
 * Define the database Properties inside the database_connection file.
 *
 *
 */
public class HistoryRestServiceTest extends JerseyTest {

    /**
     * The base url of the jcore rest interface.
     * Allows us to send requests to the {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface}.
     */
    private WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(HistoryRestService.class);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        AbstractDatabaseDependentTest.resetDatabase();
    }



    @Before
    public void setUpBase() {
        base = target("history/v2");
    }

    /**
     * tests if the GET for the ActivitiesLog returns correct values for a given scenarioInstance
     */
    @Test
    public void testGetActivitiesLog() throws IOException {
        String path = "src/test/resources/history/simpleScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.beginActivityByName("Do something", instance);
        // ScenarioTestHelper.terminateActivityInstanceByName("Do something", instance);

        int scenarioId = instance.getScenarioId();
        int scenarioInstanceId = instance.getScenarioInstanceId();
        String requestPath = String.format(
                "scenario/%d/instance/%d/activities", scenarioId, scenarioInstanceId);
        Response response = base.path(requestPath).request().get();
        String expected =
                "[" +
                "  {" +
                "    \"timeStamp\": \"15:17:52\"," +
                "    \"newValue\": \"begin\"," +
                "    \"cause\": 0,\n" +
                "    \"oldValue\": \"init\"," +
                "    \"label\": \"Do something\"," +
                "    \"loggedId\": 2" +
                "  }" +
                "]";
        // Ignore vlaues in assertion because of timestamp
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class), jsonEquals(expected)
                        .when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_VALUES));
    }

    /**
     * tests if status Code is 404 when call fails
     */
    @Test
    public void testGetActivitiesLogStatusCode404() {
        Response response = base.path("scenario/0/instance/0/activities")
                .request().get();
        assertEquals("The Response code of getActivitiesLog was not 404",
                400, response.getStatus());
        assertEquals("getActivitiesLog does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"The instance or scenario ID is incorrect\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * tests if status Code is 200 when call was successful
     */
    @Test
    public void testGetActivitiesLogStatusCode200() {
        Response response = base.path("scenario/1/instance/1302/activities")
                .request().get();
        assertEquals("The Response code of getActivitiesLog was not 200",
                200, response.getStatus());
        assertEquals("getActivitiesLog does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * tests if status Code is 404 when call fails
     */
    @Test
    public void testGetDataObjectsLogStatusCode404() {
        Response response = base.path("scenario/0/instance/0/dataobjects")
                .request().get();
        assertEquals("The Response code of getDataObjectsLog was not 400",
                400, response.getStatus());
        assertEquals("getDataObjectsLog does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"The instance or scenario ID is incorrect\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * tests if status Code is 200 when call was successful
     */
    @Test
    public void testGetDataObjectsLogStatusCode200() {
        Response response = base.path("scenario/1/instance/1302/dataobjects")
                .request().get();
        assertEquals("The Response code of getDataObjectsLog was not 200",
                200, response.getStatus());
        assertEquals("getDataObjectsLog does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * tests if the GET for the DataAttributesLog returns correct values for a given scenarioInstance
     */
    @Test
    public void testGetDataAttributesLog() {
        Response response = base.path("scenario/156/instance/1329/attributes").request().get();

        String responseJson = response.readEntity(String.class);
        assertThat("Get activities did not contain the expected information", responseJson,
                jsonEquals("{}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }

    /**
     * tests if status Code is 404 when call fails
     */
    @Test
    public void testGetDataAttributesLogStatusCode404() {
        Response response = base.path("scenario/0/instance/0/attributes")
                .request().get();
        assertEquals("The Response code of getDataAttributesLog was not 404",
                400, response.getStatus());
        assertEquals("getDataAttributesLog does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"The instance or scenario ID is incorrect\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * tests if status Code is 200 when call was successful
     */
    @Test
    public void testGetDataAttributesLogStatusCode200() {
        Response response = base.path("scenario/1/instance/1302/attributes")
                .request().get();
        assertEquals("The Response code of getDataAttributesLog was not 200",
                200, response.getStatus());
        assertEquals("getDataAttributesLog does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }
}
