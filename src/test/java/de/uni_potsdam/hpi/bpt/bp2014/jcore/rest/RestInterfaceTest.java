package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.NamedJaxBean;
import net.javacrumbs.jsonunit.core.Option;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.*;

/**
 * This Class extends the {@link de.uni_potsdam.hpi.bpt.bp2014.AbstractTest}
 * to test the RestInterface of the JEngine core.
 * In order to do so it uses the functionality of the
 * {@link org.glassfish.jersey.test.JerseyTest}
 * There are test methods for every possible REST Call.
 * In order to stay independent from existing tests, the
 * database will be set up before and after the execution.
 * Define the database Properties inside the database_connection file.
 */
public class RestInterfaceTest extends AbstractTest {
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
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }

    /**
     */
    @Test
    public void testGetActivitiesRedirects() {
        Response response = base.path("scenario/9999/instance/72/activity").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":{\"6688\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/6688\",\"id\":6688,\"label\":\"ActivityFragment4\",\"state\":\"ready\"},\"186\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/186\",\"id\":186,\"label\":\"Activity1Fragment1\",\"state\":\"ready\"}},\"ids\":[186,6688]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with an wrong scenario instance ID
     * then a 404 with error message (inside JSON) should be returned.
     */
    @Test
    public void testGetActivitiesInvalidInstance() {
        Response response = base.path("scenario/1/instance/9999/activity").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 404",
                404, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"There is no instance with id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with an correct parameters a state but no filter
     * then the request should return all activities with this state.
     */
    @Test
    public void testGetActivitiesWithState() {
        Response response = base.path("scenario/1/instance/72/activity")
                .queryParam("state", "ready(ControlFlow)").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[186,189,6686,6688],\"activities\":[{\"id\":186,\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/186\",\"state\":\"ready(ControlFlow)\",\"label\":\"Activity1Fragment1\"},{\"id\":189,\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/189\",\"state\":\"ready(ControlFlow)\",\"label\":\"Activity1Fragment2\"},{\"id\":6686,\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/6686\",\"state\":\"ready(ControlFlow)\",\"label\":\"ActivityFragment3\"},{\"id\":6688,\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/6688\",\"state\":\"ready(ControlFlow)\",\"label\":\"ActivityFragment4\"}]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with an correct parameters, an invalid state but no filter
     * the request should return a 404 with error message
     */
    @Test
    public void testGetActivitiesWithInvalidState() {
        Response response = base.path("scenario/1/instance/72/activity")
                .queryParam("state", "enabled").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 404",
                404, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The state is not allowed enabled\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }



    /**
     * with an correct parameters a state but no filter
     * then the request should return all activities with this state.
     */
    @Test
    public void testGetActivitiesWithStateTerminated() {
        Response response = base.path("scenario/1/instance/72/activity")
                .queryParam("state", "terminated").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":[],\"ids\":[]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with an correct parameters a state and a filter
     * then the request should return all activities with the state who fulfill the filter condition.
     */
    @Test @Ignore
    public void testGetActivitiesWithStateAndFilter() {
        Response response = base.path("scenario/1/instance/72/activity")
            .queryParam("state", "ready")
                .queryParam("filter", "2").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":[{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/189\",\"id\":189,\"label\":\"Activity1Fragment2\",\"state\":\"ready\"}],\"ids\":[189]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with an correct parameters, an invalid state but no filter
     * the request should return a 404 with error message
     */
    @Test
    public void testGetActivitiesWithInvalidStateFilter() {
        Response response = base.path("scenario/1/instance/72/activity")
                .queryParam("state", "enabled")
                .queryParam("filter", "1").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 404",
                404, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The state is not allowed enabled\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with correct instance and scenario
     * a 200 with json content will be returned.
     */
    @Test
    public void testGetActivitiesCorrect() {
        Response response = base.path("scenario/1/instance/72/activity").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":{\"6688\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/6688\",\"id\":6688,\"label\":\"ActivityFragment4\",\"state\":\"ready\"},\"186\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/186\",\"id\":186,\"label\":\"Activity1Fragment1\",\"state\":\"ready\"}},\"ids\":[186,6688]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with a filter String
     * then only activities with a label like the filter String will be returned.
     */
    @Test @Ignore
    public void testGetActivitiesWithFilter() {
        Response response = base.path("scenario/1/instance/72/activity").queryParam("filter", "2").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":{\"189\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/189\",\"id\":189,\"label\":\"Activity1Fragment2\",\"state\":\"ready\"}},\"ids\":[189]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }



    /**
     * with an valid id
     * then a JSON with the termination condition will be returned
     */
    @Test
    public void testGetTerminationCondition() {
        Response response = base.path("scenario/105/terminationcondition").request().get();
        assertEquals("The Response code of getTermiantionCondition was not 200",
                200, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"conditions\":{\"1\":[{\"data_object\":\"A\",\"set_id\":1,\"state\":\"c\"}]},\"setIDs\":[\"1\"]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with an invalid id
     * then a 404 with an error message should be returned
     */
    @Test
    public void testInvalidGetTerminationCondition() {
        Response response = base.path("scenario/102/terminationcondition").request().get();
        assertEquals("The Response code of getTerminationCondition was not 404",
                404, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"There is no scenario with the id 102\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with valid arguments
     * then you should get a 200 response code and
     * a JSONObject with the id, label of the activity and a link to the outputSet and the inputSet.
     */
    @Test
    public void testGetActivity(){
        Response response = base.path("scenario/135/instance/808/activity/4517").request().get();
        assertEquals("The Response code of getActivity was not 200", 200, response.getStatus());
        assertEquals("Get Activity does not return a JSON", MediaType.APPLICATION_JSON,
                response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"id\":4517,\"label\":\"Reiseplanung beginnen\",\"outputSetLink\":\"http://localhost:9998/interface/v2/scenario/135/instance/808/activity/4517/output\",\"inputSetLink\":\"http://localhost:9998/interface/v2/scenario/135/instance/808/activity/4517/input\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with valid arguments
     * then you then you should get a 200 response code and a JSONObject with the id of the inputSet and
     * a link to get the dataObjectInstance with their dataAttributeInstance.
     */
    @Test
    public void testGetInputDataObjects(){
        Response response = base.path("scenario/135/instance/808/activity/4518/input").request().get();
        assertEquals("The response code of getInputDataObjects was not 200", 200, response.getStatus());
        assertEquals("GetInputDataObjects does not return a JSON", MediaType.APPLICATION_JSON,
                response.getMediaType().toString());
        // String responseJson = response.readEntity(String.class);
        assertThat("The returned JSON does not contain the expected content",
                "[{\"id\":139,\"linkDataObject\":\"http://localhost:9998/interface/v2/scenario/135/instance/808/inputset/139\"}]",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_VALUES));
    }

    /**
     * with valid arguments
     * then you then you should get a 200 response code and a JSONObject with the id of the outputSet and
     * a link to get the dataObjectInstance with their dataAttributeInstance.
     */
    @Test
    public void testGetOutputDataObjects(){
        Response response = base.path("scenario/135/instance/808/activity/4518/output").request().get();
        assertEquals("The response code of getOutputDataObjects was not 200", 200, response.getStatus());
        assertEquals("GetOutputDataObjects does not return a JSON", MediaType.APPLICATION_JSON,
                response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "[{\"id\":140,\"linkDataObject\":\"http://localhost:9998/interface/v2/scenario/135/instance/808/outputset/140\"}]",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER).when(Option.COMPARING_ONLY_STRUCTURE));
    }
    /**
     * with an invalid state
     * a bad request with an error message should be returned.
     */
    @Test
    public void testInvalidStateUpdateActivity() {
        Response response = base.path("scenario/1/instance/72/activity/105")
                .queryParam("state", "complete").request().post(Entity.json("[]"));
        assertEquals("The Response code of getTerminationCondition was not 400",
                400, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The state transition complete is unknown\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     *
     */
    @Test
    public void testInvalidStateUpdateActivity2() {
        Response response = base.path("scenario/1/instance/72/activity/105")
                .request().post(Entity.json("[]"));
        assertEquals("The Response code of getTerminationCondition was not 400",
                400, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The state is not set\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     *
     * with an valid state for an invalid activity.
     * a bad request with an error message should be returned.
     */
    @Test
    public void testInvalidActivityUpdateActivity() {
        Response response = base.path("scenario/1/instance/72/activity/105")
                .queryParam("state", "begin").request().post(Entity.json("[]"));
        assertEquals("The Response code of getTerminationCondition was not 400",
                400, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"impossible to start activity with id 105\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     *
     * with an valid state and valid activity
     * then a 201 will be returned with a message inside a JSON-Object.
     */
    //@Test
    public void testUpdateActivity() {
        Response response = base.path("scenario/1/instance/72/activity/186")
                .queryParam("state", "begin").request().post(Entity.json("[]"));
        assertEquals("The Response code of updateActivityState was not 202",
                202, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"activity state changed.\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
        response = base.path("scenario/1/instance/72/activity/186")
                .queryParam("state", "terminate").request().post(Entity.json("[]"));
        assertEquals("The Response code of updateActivityState was not 202",
                202, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"activity state changed.\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * with an valid state and valid activity
     * then a 201 will be returned with a message inside a JSON-Object.
     */
    @Test
    public void testUpdateActivityWAttributes(){
        // TODO Find out what this does
        Response response = base.path("scenario/135/instance/808/activity/4518")
                .queryParam("state", "begin").request().post(Entity.json("[]"));
        assertEquals("The Response code of updateActivityState was not 202",
                202, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"activity state changed.\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));

        // Update value of activity
        Response updateResponse = base.path("scenario/135/instance/808/activity/4518")
                .request()
                .put(Entity.json("{\"id\":1,\"value\":\"Test\"}"));
        assertEquals("The Response code of updateActivityState was not 202",
                202, updateResponse.getStatus());

        // Terminate activity
        response = base.path("scenario/135/instance/808/activity/4518")
                .queryParam("state", "terminate").request()
                .post(Entity.json("[{\"label\":\"Reiseplan\",\"id\":22,\"state\":\"init\",\"attributeConfiguration\":{\"entry\":[{\"key\":1,\"value\":\"{name=Preis, type=, value=400}\"}]}}]"));
        assertEquals("The Response code of getTerminationCondition was not 202",
                202, updateResponse.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"activity state changed.\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given is the Rest API
     * with an invalid scenario id or instance id
     * then a 400 will be returned with an error message
     */
    @Test
    public void testTerminateInvalidScenarioInstance() {
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

    /**
     * Given is the Rest API
     * with an valid scenario and instance id
     * the instance will be terminated.
     */
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

    /**
     * Given: Is an invalid scenario instance
     * with an invalid scenario and instance id
     * a 404 with an error message is returned
     */
    @Test
    public void testGetInputForInvalidScenario() {
        Response response = base.path("scenario/9987/instance/1234/activity/1/input")
                .request().get();
        assertEquals("The Response code of getInputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getInputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no such scenario instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given: Is an invalid activity instance
     * with an invalid activity instance id
     * a 404 with an error message is returned
     */
    @Test
    public void testGetInputForInvalidActivity() {
        Response response = base.path("scenario/1/instance/72/activity/9999/input")
                .request().get();
        assertEquals("The Response code of getInputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getInputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no such activity instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given: Is an valid activity instance
     * with a valid activity instance without input sets
     * a 404 with an error message will be returned
     */
    @Test
    public void testGetInputForWOInputSets() {
        Response response = base.path("scenario/135/instance/808/activity/4517/input")
                .request().get();
        assertEquals("The Response code of getInputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getInputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no inputSet for this activity instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given: Is an invalid scenario instance
     * with an invalid scenario and instance id
     * a 404 with an error message is returned
     */
    @Test
    public void testGetOutputForInvalidScenario() {
        Response response = base.path("scenario/0/instance/0/activity/1/output")
                .request().get();
        assertEquals("The Response code of getOutputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getOutputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no such scenario instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given: Is an invalid activity instance
     * with an invalid activity instance id
     * a 404 with an error message is returned
     */
    @Test
    public void testGetOutputInvalidActivity() {
        Response response = base.path("scenario/1/instance/72/activity/9999/output")
                .request().get();
        assertEquals("The Response code of getOutputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getOutputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no such activity instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given: Is an valid activity instance
     * with a valid activity instance without input sets
     * a 404 with an error message will be returned
     */
    @Test
    public void testGetOutputForWOOutputSets() {
        Response response = base.path("scenario/118/instance/704/activity/3749/output")
                .request().get();
        assertEquals("The Response code of getOutputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getOutputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no outputSet for this activity instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * test that getVersion() gives a valid response.
      */
    @Test
    public void testGetVersion() {
        Response response = base.path("version").request().get();
        assertEquals("The Response code of getVersion was not 200",
                200, response.getStatus());
        assertEquals("Get Version does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"version\": \"1.0-SNAPSHOT\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }
}