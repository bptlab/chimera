package de.hpi.bpt.chimera.jconfiguration.rest;

import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.jcore.rest.RestInterface;

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
 * This class is the test for {@link RestConfigurator}.
 * hence the database configuration will be restored afterwards.
 */
public class RestConfiguratorTest extends AbstractTest {

    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2RESTTest_new.sql";
    }
    /**
     * The base url of the jcore rest interface.
     * Allows us to send requests to the {@link RestInterface}.
     */
    private WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(RestConfigurator.class);
    }

    @Before
    public void setUpBase() {
        base = target("config/v2");
    }

    // ************************** EMAIL SERVICE TASKS **********************************/

    /**
     * When you send a GET to {@link RestConfigurator#getAllEmailTasks(int, String)}
     * the response should be of type json.
     */
    @Test
    public void testGetMailTasksReturnsJson() {
        Response response = base.path("scenario/1/emailtask").request().get();
        assertEquals("The Response code of get all mail tasks was not 200",
                200, response.getStatus());
        assertEquals("Get all mail tasks returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * When you send a GET to {@link RestConfigurator#getAllEmailTasks(int, String)} and
     * the scenario does not contain any mail task an object with no ids will be returned.
     */
    @Test
    public void testGetAllMailTasksIfAbsent() {
        Response response = base.path("scenario/1/emailtask").request().get();
        assertThat("Get all mail Tasks returns not an empty JSON Object when the scenario has no mail tasks",
                "{\"ids\":[]}", jsonEquals(response.readEntity(String.class)));
    }

    /**
     * When you send a GET to {@link RestConfigurator#getAllEmailTasks(int, String)}
     * the returned JSON Object should be a specified.
     * {"ids":[1,2,...],"labels":{1:"abcd"...}}}
     */
    @Test
    public void testGetAllMailTasksJSONResponse() {
        Response response = base.path("scenario/142/emailtask").request().get();
        assertThat("Get all mail Tasks returns not an empty JSON Object when the scenario has no mail tasks",
                "{\"ids\":[362]}", jsonEquals(response.readEntity(String.class)));
    }

    /**
     * When you send a Get to {@link RestConfigurator#getAllEmailTasks(int, String)}
     * and the ScenarioID is invalid a 404 will be returned but the media type is still
     * JSON.
     */
    @Test
    public void testGetAllMailTasksMissingScenario() {
        Response response = base.path("scenario/99999/emailtask").request().get();
        assertEquals("The Response code of get all mail tasks was not 404",
                404, response.getStatus());
        assertEquals("Get all mail tasks returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * When you send a Get to {@link RestConfigurator#getEmailTaskConfiguration(int, int)}
     * with an invalid scenario an empty JSON object should be returned, with a 404.
     */
    @Test
    public void testGetEmailTaskConfigurationMissingScenario() {
        Response response = base.path("scenario/99999/emailtask/1").request().get();
        assertEquals("The Response code of get email task configuration was not 404",
                404, response.getStatus());
        assertEquals("Get mail task configuration responses with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * When you send a Get to {@link RestConfigurator#getEmailTaskConfiguration(int, int)}
     * with an invalid mailTask an empty JSON object should be returned, with a 404.
     */
    @Test
    public void testGetEmailTaskConfigurationMissingMailTask() {
        Response response = base.path("scenario/1/emailtask/9999").request().get();
        assertEquals("The Response code of get email task configuration was not 404",
                404, response.getStatus());
        assertEquals("Get mail task configuration responses with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * When you send a Get to {@link RestConfigurator#getEmailTaskConfiguration(int, int)}
     * a 200 with an json object should be returned
     */
    @Test
    public void testGetEmailTaskReturnsJSON() {
        Response response = base.path("scenario/142/emailtask/353").request().get();
        assertEquals("The Response code of get  mail configuration was not 200",
                200, response.getStatus());
        assertEquals("Get all mail tasks returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * When you send a Get to {@link RestConfigurator#getEmailTaskConfiguration(int, int)}
     * a valid json object with "receiver", "message", "subject" should be returned
     */
    @Test
    public void testGetEmailTaskReturnsCorrectJSON() {
        Response response = base.path("scenario/142/emailtask/353").request().get();
        assertThat("Get mail Task configuration returns not an valid JSON object",
                "{\"receiver\":\"bp2014w1@byom.de\",\"subject\":\"Test\",\"message\":\"Test Message\"}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    // ************************** Scenario **********************************/

    /**
     * When one sends a DELETE to {@link RestConfigurator#deleteScenario(Integer)}
     * the returned Status Code should be 202 since the scenario will be deleted in any case.
     */
    @Test
    public void testDeleteScenarioWithRunningInstances() {
        Response response = base.path("scenario/1/").request().delete();
        assertEquals("The Response code of deleting a scenario was not 202",
                202, response.getStatus());
    }

    /**
     * When one sends a DELETE to {@link RestConfigurator#deleteScenario(Integer)}
     * the returned Status Code should be 202 since the scenario will be deleted in any case.
     */
    @Test
    public void testDeleteScenarioWithoutRunningInstances() {
        Response response = base.path("scenario/152/").request().delete();
        assertEquals("The Response code of deleting a scenario was not 202",
                202, response.getStatus());
    }


    // ************************** WEB SERVICE TASKS **********************************/
    @Test
    public void testGetAllWebserviceTasks() {
        Response response = base.path("scenario/145/webservice").request().get();
        assertThat("Get all webservice tasks returns something wrong",
                "{\"ids\":[390]}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testUpdateWebservicePostBadRequest() {
        Response response = base.path("webservice/390").request().put(Entity.json("{\"method2222\":\"GET\"}"));
        assertEquals("The Response code of updating the WebserviceConfiguration was not 400",
                400, response.getStatus());
    }
}
