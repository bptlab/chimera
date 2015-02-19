package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.config.Config;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.LinkedList;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static jersey.repackaged.com.google.common.base.Predicates.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;


public class RestConnectionTest {

    private ExecutionService executionService = new ExecutionService();
    private HistoryService historyService = new HistoryService();

    String serverURL = Config.jcoreServerUrl;

    private static final String GET_ALL_SCENARIOS = "getAllScenarioIDs";
    private static final String LIST_ALL_SCENARIO_INSTANCES = "listAllScenarioInstancesForScenario";

    @Before
    public void setUp() {

    }

    /**
     *
     */
    @Test
    public void testGetScenarios() {

        String getUrl = "/jcomparser/scenarios/0/";

        expect().
                body("ids", hasItems(1, 2, 3)).
                when().
                get(getUrl);
    }

    /**
     *
     */
    @Test
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
