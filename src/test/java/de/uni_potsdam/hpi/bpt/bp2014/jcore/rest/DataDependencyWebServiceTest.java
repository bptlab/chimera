package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import net.javacrumbs.jsonunit.core.Option;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.*;

/**
 *
 */
public class DataDependencyWebServiceTest extends AbstractTest {
    static Logger log = Logger.getLogger(RestInterfaceTest.class);

    private WebTarget base;

    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2RESTTest_new.sql";
    }
    
    @Override
    protected Application configure() {
        return new ResourceConfig(DataDependencyWebService.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }

    /**
     * with an invalid scenario/Instance
     * a 404 with an error message is returned
     */
    @Test
    public void testInvalidScenarioGetOutputSetDataAttributes() {
        log.debug("Enter <<testInvalidScenarioGetOutputSetDataAttributes>>");
        Response response = base.path("scenario/9987/instance/1234/outputset/140")
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
     * with an invalid ouptutSetID
     * a 404 with an error message is returned
     */
    @Test
    public void testInvalidOutputSetGetOutputSetDataAttributes() {
        Response response = base.path("scenario/135/instance/808/outputset/1400")
                .request().get();
        assertEquals("The Response code of getOutputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getOutputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no such outputSet instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     *  with valid arguments
     *  a 200 is returned with a JSONArray with JSONObjects for each dataObjectInstance
     */
    @Test
    public void testGetInputSetDataAttributes() {
        Response response = base.path("scenario/135/instance/808/inputset/139")
                .request().get();
        assertEquals("The Response code of getInputDataAttributes was not 200",
                200, response.getStatus());
        assertEquals("getAttributes does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("[{\"label\":\"Reiseplan\",\"id\":675,\"state\":\"init\",\"attributeConfiguration\":[{\"id\":1,\"name\":\"Preis\",\"type\":\"\",\"value\":\"250\"}]}]")
                        .when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }
    /**
     * with an invalid scenario/Instance
     * a 404 with an error message is returned
     */
    @Test
    public void testInvalidScenarioGetInputSetDataAttributes() {
        Response response = base.path("scenario/9987/instance/1234/inputset/140")
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
     * with an invalid inputSetID
     * a 404 with an error message is returned
     */
    @Test
    public void testInvalidInputSetGetInputSetDataAttributes() {
        Response response = base.path("scenario/135/instance/808/inputset/1400")
                .request().get();
        assertEquals("The Response code of getInputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getInputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no such inputSet instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     *  with valid arguments
     *  a 200 is returned with a JSONArray with JSONObjects for each dataObjectInstance
     */
    @Test
    public void testGetOutputSetDataAttributes() {
        log.debug("Enter <<testGetOutputSetDataAttributes>>");
        ExecutionService.getInstance(135).reloadScenarioInstanceFromDatabase(135, 808);
        Response response = base.path("scenario/135/instance/808/outputset/140")
                .request().get();
        assertEquals("The Response code of getOutputDataAttributes was not 200",
                200, response.getStatus());
        assertEquals("getOutputDataAttributes does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("[{\"label\":\"Reiseplan\",\"id\":675,\"state\":\"Ziel festgelegt\",\"attributeConfiguration\":[{\"id\":1,\"name\":\"Preis\",\"type\":\"\",\"value\":\"250\"}]}]")
                        .when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
        log.debug("Leave <<testGetOutputSetDataAttributes>>");
    }

}