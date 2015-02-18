package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.matcher.RestAssuredMatchers.*;


public class RestConnectionTest {
    //test scenario 2
    //get("/lotto").then().body("lotto.lottoId", equalTo(5));
    //get("/lotto").then().body("lotto.winners.winnerId", hasItems(23, 54));

    get("/products").then().assertThat().body(matchesJsonSchemaInClasspath("products-schema.json"));
}
