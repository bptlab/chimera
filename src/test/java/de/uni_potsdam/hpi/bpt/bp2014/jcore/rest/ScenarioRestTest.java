package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractUserManagementTest;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Test methods of the ScenarioRestService {@link ScenarioRestService}
 */
public class ScenarioRestTest extends AbstractTest {

    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2RESTTest_new.sql";
    }

    private WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(ScenarioRestService.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }

    /**
     * When querying scenarios with a filter parameter the response should be a json string
     * containing the links to the scenarios containing the filter string in the name.
     */
    @Test
    public void testGetScenarioContentWithFilter() {
        Response response = base.path("scenario").queryParam("filter", "HELLO").request().get();
        assertThat("Get Scenarios did not contain the expected information",
                "{\"ids\":[1,2],\"labels\":{\"1\":\"HELLOWORLD\",\"2\":\"helloWorld2\"},\"links\":{\"1\":\"http://localhost:9998/interface/v2/scenario/1\",\"2\":\"http://localhost:9998/interface/v2/scenario/2\"}}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testGetScenarioContent() {
        Response response = base.path("scenario").request().get();
        assertThat("Get Scenarios did not contain the expected information",
                "{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134,135,136,138,139,140,141,142,143,144, 145],\"links\":{\"140\":\"http://localhost:9998/interface/v2/scenario/140\",\"141\":\"http://localhost:9998/interface/v2/scenario/141\",\"142\":\"http://localhost:9998/interface/v2/scenario/142\",\"143\":\"http://localhost:9998/interface/v2/scenario/143\",\"1\":\"http://localhost:9998/interface/v2/scenario/1\",\"100\":\"http://localhost:9998/interface/v2/scenario/100\",\"111\":\"http://localhost:9998/interface/v2/scenario/111\",\"144\":\"http://localhost:9998/interface/v2/scenario/144\",\"2\":\"http://localhost:9998/interface/v2/scenario/2\",\"101\":\"http://localhost:9998/interface/v2/scenario/101\",\"134\":\"http://localhost:9998/interface/v2/scenario/134\",\"3\":\"http://localhost:9998/interface/v2/scenario/3\",\"113\":\"http://localhost:9998/interface/v2/scenario/113\",\"135\":\"http://localhost:9998/interface/v2/scenario/135\",\"103\":\"http://localhost:9998/interface/v2/scenario/103\",\"114\":\"http://localhost:9998/interface/v2/scenario/114\",\"136\":\"http://localhost:9998/interface/v2/scenario/136\",\"115\":\"http://localhost:9998/interface/v2/scenario/115\",\"105\":\"http://localhost:9998/interface/v2/scenario/105\",\"116\":\"http://localhost:9998/interface/v2/scenario/116\",\"138\":\"http://localhost:9998/interface/v2/scenario/138\",\"117\":\"http://localhost:9998/interface/v2/scenario/117\",\"139\":\"http://localhost:9998/interface/v2/scenario/139\",\"118\":\"http://localhost:9998/interface/v2/scenario/118\",\"145\":\"http://localhost:9998/interface/v2/scenario/145\"},\"labels\":{\"1\":\"HELLOWORLD\",\"2\":\"helloWorld2\",\"3\":\"EmailTest\",\"100\":\"TestScenario\",\"101\":\"Test Insert Scenario\",\"134\":\"ReiseTestScenario\",\"103\":\"ScenarioTest1\",\"135\":\"ReiseTestScenario\",\"136\":\"TXOR1Scenario\",\"105\":\"TestScenarioTerminationCondition\",\"138\":\"TestEmail1Scenario\",\"139\":\"TestEmail1Scenario\",\"140\":\"TestEmail1Scenario\",\"141\":\"TestEmail2Scenario\",\"142\":\"TestEmail3Scenario\",\"111\":\"Test2_2ReferenceTest\",\"143\":\"TestEmail3Scenario\",\"144\":\"XORTest2Scenario\",\"113\":\"referencetest3_2\",\"114\":\"RT4Scenario\",\"115\":\"TT2Scenario\",\"116\":\"TT2Scenario\",\"117\":\"AT2Scenario\",\"118\":\"AT3Scenario\",\"145\":\"ServiceTaskSzenario\"}}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testGetScenarioProducesJson() {
        Response response = base.path("scenario").request().get();
        assertEquals("The Response code of get Scenario was not 200",
                200, response.getStatus());
        assertEquals("Get Scenarios returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * TODO fix this test
     * the entity of the response will be a valid JSON array.
     */
    @Test
    public void testGetScenarioProducesValidJsonArray() {
        Response response = base.path("scenario").request().get();
        assertNotEquals("Get scenarios did not respond with a valid JSON Array",
                null, new JSONObject(response.readEntity(String.class)));
    }



    /**
     * {@link ScenarioRestService#getScenarios(UriInfo, String)}
     */
    @Test
    public void testGetScenarioInvalidId() {
        Response response = base.path("scenario/99999").request().get();
        assertEquals("Get Scenario returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals("Get scenario returns a not empty JSON, but the id was invalid",
                404, response.getStatus());
        assertEquals("The content of the invalid request is not an empty JSONObject",
                "{}",
                response.readEntity(String.class));
    }

    @Test
    public void testGetTerminationCondition() {
        Response response = base.path("scenario/105/terminationcondition").request().get();
        assertEquals("The Response code of getTerminationCondition was not 200",
                200, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"conditions\":{\"1\":[{\"data_object\":\"object1\",\"set_id\":\"1\",\"state\":\"c\"}]},\"setIDs\":[\"1\"]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }


    @Test
    public void testTerminationConditionNotFound() {
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

    @Test
    public void testGetScenario() {
        Response response = base.path("scenario/1").request().get();
        String responseEntity = response.readEntity(String.class);
        assertEquals("The Response code of get Scenario was not 200",
                200, response.getStatus());
        assertEquals("Get Scenarios returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertNotEquals("Get scenarios did not respond with a valid JSON Array",
                null, new JSONObject(responseEntity));
        assertThat("The content of the valid request is not as expected",
                "{\"modelid\":\"0\",\"instances\":\"http://localhost:9998/interface/v2/scenario/1/instance\",\"name\":\"HELLOWORLD\",\"id\":1,\"modelversion\":0}",
                jsonEquals(responseEntity).when(Option.IGNORING_ARRAY_ORDER));
    }
}
