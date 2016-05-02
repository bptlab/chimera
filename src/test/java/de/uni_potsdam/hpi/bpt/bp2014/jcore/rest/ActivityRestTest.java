package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Ignore;
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
 *
 */
public class ActivityRestTest extends AbstractTest {

    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2RESTTest_new.sql";
    }

    private WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(ActivityRestService.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }

    @Test
    public void testGetActivitiesRedirects() {
        Response response = base.path("scenario/9999/instance/72/activity").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                404, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

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

    @Test
    public void testNotFoundInvalidScenarioInstanceId() {
        Response response = base.path("scenario/1/instance/9999/activity").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 404",
                404, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"scenario or scenario instance id invalid\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testNotFoundForInvalidScenarioId() {
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


    @Test
    public void testGetActivitiesStateFilter() {
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
     * TODO should this be an invalid state and 404 is the right code not 400?
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


    @Test
    public void testGetTerminatedActivities() {
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

    @Test
    public void testGetActivities() {
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

    @Test
    public void testGetInputDataObjects(){
        Response response = base.path("scenario/135/instance/808/activity/4518/input").request().get();
        assertEquals("The response code of getInputDataObjects was not 200", 200, response.getStatus());
        assertEquals("GetInputDataObjects does not return a JSON", MediaType.APPLICATION_JSON,
                response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "[{\"id\":139,\"linkDataObject\":\"http://localhost:9998/interface/v2/scenario/135/instance/808/inputset/139\"}]",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_VALUES));
    }

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

    @Test
    public void testStateUpdateToInvalidState() {
        Response response = base.path("scenario/1/instance/72/activity/105")
                .queryParam("state", "invalid").request().post(Entity.json("[]"));
        assertEquals("The Response code of getTerminationCondition was not 400",
                400, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The state transition invalid is unknown\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * TODO improve this test because it relies on previous
     */
    @Test
    public void testStateWasNotSetCorrectly() {
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

    @Test
    public void testInvalidStateTransition() {
        // An activity which is running cannot be started again
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

    @Test
    public void testNotFoundInputInvalidActivityId() {
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

    @Test
    public void testGetInputForActivityWithoutInput() {
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

    @Test
    public void testNotFoundInvalidActivityId() {
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

    @Test
    public void testGetOutputForActivityWithoutOutput() {
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
}
