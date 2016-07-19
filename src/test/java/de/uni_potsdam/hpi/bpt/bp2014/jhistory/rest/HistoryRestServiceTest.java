package de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractUserManagementTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


/**
 * This Class extends the {@link AbstractUserManagementTest}
 * to test the HistoryRestService of the JEngine core.
 * In order to do so it uses the functionality of the
 * {@link org.glassfish.jersey.test.JerseyTest}
 * There are test methods for every possible REST Call.
 * In order to stay independent from existing tests, the
 * database will be set up before and after the execution.
 * Define the database Properties inside the database_connection file.
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

    @Test
    public void testGetActivitiesLog() throws IOException {
        String path = "src/test/resources/history/simpleScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.beginActivityByName("Do something", instance);
        // ScenarioTestHelper.terminateActivityByName("Do something", instance);

        int scenarioId = instance.getScenarioId();
        int scenarioInstanceId = instance.getScenarioInstanceId();
        String requestPath = String.format(
                "scenario/%d/instance/%d/activities", scenarioId, scenarioInstanceId);
        Response response = base.path(requestPath).request().get();
        JSONArray json = new JSONArray(response.readEntity(String.class));

        String expected =
                "  {" +
                "    \"timeStamp\": \"15:17:52\"," +
                "    \"newValue\": \"begin\"," +
                "    \"cause\": 0,\n" +
                "    \"oldValue\": \"init\"," +
                "    \"label\": \"Do something\"," +
                "    \"loggedId\": 2" +
                "  }";
        // Ignore vlaues in assertion because of timestamp
        assertThat("Get activities did not contain the expected information",
                json.getJSONObject(1).toString(), jsonEquals(expected)
                        .when(Option.IGNORING_VALUES));
    }

    @Test
    public void testDataAttributeRest() throws IOException {
        int scenarioInstanceId = 1;
        int scenarioId = 1;
        DbLogEntry logEntry = new DbLogEntry();
        logEntry.logDataAttributeCreation(1, "val", scenarioInstanceId);
        logEntry.logDataAttributeTransition(1, "foo", 1, scenarioInstanceId);
        logEntry.logDataAttributeTransition(1, "bar", 1, scenarioInstanceId);
        String requestPath = String.format(
                "scenario/%d/instance/%d/attributes", scenarioId, scenarioInstanceId);
        Response response = base.path(requestPath).request().get();
        JSONArray resp = new JSONArray(response.readEntity(String.class));

        assertEquals(3, resp.length());
        JSONObject creation = resp.getJSONObject(0);
        assertEquals(JSONObject.NULL, creation.get("oldValue"));
        assertEquals("val", creation.get("newValue"));

        JSONObject first = resp.getJSONObject(1);
        assertEquals(first.get("oldValue"), "val");
        assertEquals(first.get("newValue"), "foo");

        JSONObject second = resp.getJSONObject(2);
        assertEquals(second.get("oldValue"), "foo");
        assertEquals(second.get("newValue"), "bar");
    }

    @Test
    public void testGetCompleteLog() throws IOException {
        int scenarioInstanceId = 1;
        int scenarioId = 1;

        String path = "src/test/resources/history/HistoryExample.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Create Data", instance);
        ScenarioTestHelper.executeActivityByName("Change Data", instance);


        String requestPath = String.format(
                "scenario/%d/instance/%d", scenarioId, scenarioInstanceId);
        Response response = base.path(requestPath).request().get();
        JSONArray resp = new JSONArray(response.readEntity(String.class));
        // There should be init running terminate for both activities and init again for the
        // first one.
        // One init for data attribute init and init changed for data object
        assertEquals(10, resp.length());
    }

    @Test
    public void testDataObjectRest() throws IOException {
        int scenarioInstanceId = 1;
        int scenarioId = 1;

        String path = "src/test/resources/history/HistoryExample.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.executeActivityByName("Create Data", instance);
        ScenarioTestHelper.executeActivityByName("Change Data", instance);
        String requestPath = String.format(
                "scenario/%d/instance/%d/dataobjects", scenarioId, scenarioInstanceId);
        Response response = base.path(requestPath).request().get();
        JSONArray resp = new JSONArray(response.readEntity(String.class));

        JSONObject initEntry = resp.getJSONObject(0);
        assertEquals(JSONObject.NULL, initEntry.get("oldValue"));
        assertEquals("init", initEntry.get("newValue"));

        JSONObject changedEntry = resp.getJSONObject(1);
        assertEquals("init", changedEntry.get("oldValue"));
        assertEquals("changed", changedEntry.get("newValue"));
    }

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

    @Test
    public void testGetActivitiesLogStatusCode200() {
        Response response = base.path("scenario/1/instance/1302/activities")
                .request().get();
        assertEquals("The Response code of getActivitiesLog was not 200",
                200, response.getStatus());
        assertEquals("getActivitiesLog does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

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
                jsonEquals("{\"error\":\"The instance or scenario Id is incorrect\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testGetDataObjectsLogStatusCode200() {
        Response response = base.path("scenario/1/instance/1302/dataobjects")
                .request().get();
        assertEquals("The Response code of getDataObjectsLog was not 200",
                200, response.getStatus());
        assertEquals("getDataObjectsLog does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    @Test
    public void testGetDataAttributesLog() {
        Response response = base.path("scenario/156/instance/1329/attributes").request().get();

        String responseJson = response.readEntity(String.class);
        assertThat("Get activities did not contain the expected information", responseJson,
                jsonEquals("[]").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }

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
