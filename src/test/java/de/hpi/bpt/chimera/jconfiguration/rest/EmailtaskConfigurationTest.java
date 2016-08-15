package de.hpi.bpt.chimera.jconfiguration.rest;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.DbEmailConfiguration;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcore.rest.DataDependencyRestService;
import de.hpi.bpt.chimera.jcore.rest.filters.AuthorizationRequestFilter;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
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

public class EmailtaskConfigurationTest extends JerseyTest {
    WebTarget base;

    private int SCENARIO_ID;
    private int EMAILTASK_ID;

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig(RestConfigurator.class);
        config.register(AuthorizationRequestFilter.class);
        return config;
    }

    @Before
    public void setup() {
        Connector connector = new Connector();
        SCENARIO_ID = connector.insertScenario("name", 1);
        int fragmentId = connector.insertFragment("TestFragment", SCENARIO_ID, 1);
        EMAILTASK_ID = connector.insertControlNode(
                "mailTask", "EmailTask", fragmentId, "modelId");
        DbEmailConfiguration dbEmailConfiguration = new DbEmailConfiguration();
        String insertMailConfiguration = "INSERT INTO emailconfiguration " +
                "(receivermailaddress, sendmailaddress, subject, message, controlnode_id) " +
                "VALUES ('receiver', 'sender', 'subject', 'message', %d);";
        String sql = String.format(insertMailConfiguration, EMAILTASK_ID);
        dbEmailConfiguration.executeInsertStatement(sql);
        base = target("config/v2");
    }

    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    /**
     * When you send a GET to {@link RestConfigurator#getAllEmailTasks(int)}
     * the response should be of type json.
     */
    @Test
    public void testGetMailTasksReturnsJson() {
        String path = String.format("scenario/%d/emailtask", SCENARIO_ID);
        Response response = base.path(path).request().get();
        assertEquals(200, response.getStatus());
        assertEquals("Get all mail tasks returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * When you send a GET to {@link RestConfigurator#getAllEmailTasks(int)} and
     * the scenario does not contain any mail task an object with no ids will be returned.
     */
    @Test
    public void testGetAllMailTasksIfAbsent() {
        Connector connector = new Connector();
        int emptyScenarioId = connector.insertScenario("name", 1);
        String path = String.format("scenario/%d/emailtask", emptyScenarioId);
        Response response = base.path(path).request().get();
        assertThat("Get all mail Tasks returns not an empty JSON Object when the scenario has no mail tasks",
                "{\"ids\":[]}", jsonEquals(response.readEntity(String.class)));
    }

    /**
     * When you send a GET to {@link RestConfigurator#getAllEmailTasks(int)}
     * the returned JSON Object should be a specified.
     * {"ids":[1,2,...],"labels":{1:"abcd"...}}}
     */
    @Test
    public void testGetAllMailTasksJSONResponse() {
        String path = String.format("scenario/%d/emailtask", SCENARIO_ID);
        Response response = base.path(path).request().get();
        String expectedResponse = String.format("{\"ids\":[%d]}", EMAILTASK_ID);
        assertThat(expectedResponse, jsonEquals(response.readEntity(String.class)));
    }

    /**
     * When you send a Get to {@link RestConfigurator#getAllEmailTasks(int)}
     * and the ScenarioID is invalid a 404 will be returned but the media type is still
     * JSON.
     */
    @Test
    public void testGetAllMailTasksMissingScenario() {
        Response response = base.path("scenario/99999/emailtask").request().get();
        assertEquals("The Response code of get all mail tasks was not 404",
                404, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
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
        int invalidMailTask = 9999;
        String path = String.format("scenario/%d/emailtask/%d", SCENARIO_ID, invalidMailTask);
        Response response = base.path(path).request().get();
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
        String path = String.format("scenario/%d/emailtask/%d", SCENARIO_ID, EMAILTASK_ID);
        Response response = base.path(path).request().get();
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
        String path = String.format("scenario/%d/emailtask/%d", SCENARIO_ID, EMAILTASK_ID);
        Response response = base.path(path).request().get();
        assertThat("Get mail Task configuration returns not an valid JSON object",
                "{\"receiver\":\"receiver\",\"subject\":\"subject\",\"message\":\"message\"}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }
}
