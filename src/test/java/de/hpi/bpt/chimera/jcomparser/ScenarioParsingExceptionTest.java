package de.hpi.bpt.chimera.jcomparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.jcore.rest.ScenarioRestService;

/**
 * This class should contain tests which are used to find out if the
 * correct exception is thrown when sending and invalid scenario via Rest
 */
public class ScenarioParsingExceptionTest extends AbstractTest {
    WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(ScenarioRestService.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }

    // When passing in a scenario with a data node which references an non
    // existing dataclass an 422 Unprocessable Entity response should be returned
    @Test
    public void testInvalidDataclassReference() throws IOException {
        String path = "src/test/resources/jcomparser/" +
                "ScenarioParsingExceptionTests/InvalidDataclass.json";
        File file = new File(path);
        String json = FileUtils.readFileToString(file);
        Response response = base.path("scenario").request().post(Entity.json(json));
        assertEquals(422, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
        assertEquals("[{\"text\":\"Data node foo references an invalid data class\",\"type\":\"danger\"}]", response.readEntity(String.class));
    }

    @Test
    public void testInvalidTerminationConditionReference() throws IOException {
        String path = "src/test/resources/jcomparser/" +
                "ScenarioParsingExceptionTests/InvalidTerminationCondition.json";
        File file = new File(path);
        String json = FileUtils.readFileToString(file);
        Response response = base.path("scenario").request().post(Entity.json(json));
        assertEquals(422, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
        assertEquals("[{\"text\":\"Termination condition references invalid data class Default\",\"type\":\"danger\"}]",
                response.readEntity(String.class));
    }

    @Test
    public void testValidDataclassNames() throws IOException {
        String path = "src/test/resources/jcomparser/" +
                "ScenarioParsingExceptionTests/InvalidDataClassNames.json";
        File file = new File(path);
        String json = FileUtils.readFileToString(file);
        Response response = base.path("scenario").request().post(Entity.json(json));
        assertEquals(Response.Status.Family.CLIENT_ERROR, response.getStatusInfo().getFamily());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
        assertEquals("[{\"text\":\"Data@DROP*FROM EVERYTHING is not a valid data class name\",\"type\":\"danger\"}]",
                response.readEntity(String.class));
    }

    @Test
    public void testTaskNameValidation() throws IOException {
        String path = "src/test/resources/jcomparser/" +
                "ScenarioParsingExceptionTests/InvalidTaskName.json";
        File file = new File(path);
        String json = FileUtils.readFileToString(file);
        Response response = base.path("scenario").request().post(Entity.json(json));
        assertEquals(Response.Status.Family.CLIENT_ERROR, response.getStatusInfo().getFamily());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
        assertEquals("[{\"text\":\"DROP * FROM EVERYTHING is not a valid task name\",\"type\":\"danger\"}]",
                response.readEntity(String.class));
    }


    @Test
    public void testFragmentsPresent() throws IOException {
        String path = "src/test/resources/jcomparser/" +
                "ScenarioParsingExceptionTests/EmptyFragments.json";
        File file = new File(path);
        String json = FileUtils.readFileToString(file);
        Response response = base.path("scenario").request().post(Entity.json(json));
        assertEquals(Response.Status.Family.CLIENT_ERROR, response.getStatusInfo().getFamily());
        assertEquals("[{\"text\":\"No fragments specified\",\"type\":\"danger\"}]", response.readEntity(String.class));
    }

}
