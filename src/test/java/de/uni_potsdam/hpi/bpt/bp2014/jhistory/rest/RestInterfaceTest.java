package de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


/**
 * This Class extends the {@link de.uni_potsdam.hpi.bpt.bp2014.AbstractTest}
 * to test the RestInterface of the JEngine core.
 * In order to do so it uses the functionality of the
 * {@link org.glassfish.jersey.test.JerseyTest}
 * There are test methods for every possible REST Call.
 * In order to stay independent from existing tests, the
 * database will be set up before and after the execution.
 * Define the database Properties inside the database_connection file.
 *
 *
 */
public class RestInterfaceTest extends AbstractTest {

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2_schema.sql";
    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineJHistoryRestTest.sql";
    }
    /**
     * The base url of the jcore rest interface.
     * Allows us to send requests to the {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface}.
     */
    private WebTarget base;

    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest.RestInterface.class);
    }

    @Before
    public void setUpBase() {
        base = target("history/v2/");
    }

    /**
     * tests if the GET for the ActivitiesLog returns correct values for a given scenarioInstance
     */
    @Test
    public void testGetActivitiesLog() {
        Response response = base.path("scenario/1/instance/1302/activities").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"1\":{\"h.scenarioinstance_id\":1302,\"h.id\":1,\"h.activityinstance_id\":9261,\"cn.label\":\"Activity1Fragment1\",\"h.newstate\":\"init\"}}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
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
     * tests if the GET for the terminated entries in ActivitiesLog returns correct values for a given scenarioInstance
     */
    @Test
    public void testGetActivitiesLogWithState() {
        Response response = base.path("scenario/1/instance/1302/activities").queryParam("state", "terminated").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"18\":{\"h.scenarioinstance_id\":1302,\"h.id\":18,\"h.activityinstance_id\":9265,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"running\",\"h.newstate\":\"terminated\"}}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }
    
    /**
     * tests if the GET for the DataObjectlog returns correct values for a given scenarioInstance
     */
    @Test
    public void testGetDataObjectsLog() {
        Response response = base.path("scenario/1/instance/1302/dataobjects").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"3\":{\"h.scenarioinstance_id\":1302,\"h.id\":3,\"h.oldstate_id\":1,\"h.newstate_id\":2,\"h.dataobjectinstance_id\":1058,\"newstate_name\":\"bearbeitet\",\"oldstate_name\":\"init\",\"do.name\":\"Bestellung\"}}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
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
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"1\":{\"h.scenarioinstance_id\":1329,\"da.name\":\"Attribut1\",\"h.id\":1,\"h.dataattributeinstance_id\":150,\"h.newvalue\":\"\",\"do.name\":\"DO\"}}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
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
