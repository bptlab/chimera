package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 *
 */
@PrepareForTest()
public class RestConnectionTestWithMock extends JerseyTest {

    private ExecutionService executionService;

    private static final String GET_ALL_SCENARIOS = "getAllScenarioIDs";
    private static final String LIST_ALL_SCENARIO_INSTANCES = "listAllScenarioInstancesForScenario";



    @Override
    protected Application configure() {
        return new ResourceConfig(RestConnection.class);
    }

    @Before
    public void setUpExecutionService() {
        executionService = new ExecutionService();
    }

    /**
     *
     */
    //@Test
    public void testGetScenarios() {
        String getUrl = "jcomparser/scenarios";
        final Response test = target(getUrl).request().get();
        assertEquals("{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134]}", test.readEntity(String.class));

        //retrieve GET and check if it is as expected


    }

    /**
     *
     */
    //@Test
    public void testGetAllEnabledActivities() {
       /*
        int scenarioID = 1;
        int newInstanceID = executionService.startNewScenarioInstance(scenarioID);
        String url = serverURL + "jcomparser/scenarios/" + scenarioID + "/instance/" + newInstanceID + "/activityinstance/0/";
        get("/products").then().assertThat().body(matchesJsonSchemaInClasspath("json/products-schema.json"));
        */
    }
    //create a new instance for our test


    //get("/lotto").then().body("lotto.lottoId", equalTo(5));
    //get("/lotto").then().body("lotto.winners.winnerId", hasItems(23, 54));

    // get("/products").then().assertThat().body(matchesJsonSchemaInClasspath("json/products-schema.json"));
    //given().param("key1", "value1").param("key2", "value2").when().post("/somewhere").then().body(containsString("OK"));

}
