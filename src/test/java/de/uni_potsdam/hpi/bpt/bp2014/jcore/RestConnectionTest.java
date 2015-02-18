package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.matcher.RestAssuredMatchers.*;
import org.junit.Test;


public class RestConnectionTest {

    private ExecutionService executionService = new ExecutionService();
    private HistoryService historyService = new HistoryService();

    @Test
    public void testGetScenarios() {
        //Todo: retrieve Server_URL from central config file
        String url = Server_URL + "jcomparser/scenarios/";
        int instanceID =executionService.startNewScenarioInstance(scenarioID);
    }
    //create a new instance for our test


    //get("/lotto").then().body("lotto.lottoId", equalTo(5));
    //get("/lotto").then().body("lotto.winners.winnerId", hasItems(23, 54));

   // get("/products").then().assertThat().body(matchesJsonSchemaInClasspath("json/products-schema.json"));
   //given().param("key1", "value1").param("key2", "value2").when().post("/somewhere").then().body(containsString("OK"));

}
