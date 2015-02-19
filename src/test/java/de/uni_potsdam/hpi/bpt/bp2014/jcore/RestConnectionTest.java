package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;

import static org.easymock.EasyMock.createNiceMock;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;

import de.uni_potsdam.hpi.bpt.bp2014.config.Config;

import java.util.LinkedList;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@PowerMockIgnore({"javax.net.ssl.*","java.sql.*","com.mysql.jdbc.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(ExecutionService.class)
public class RestConnectionTest {

    private ExecutionService executionService = new ExecutionService();
    private HistoryService historyService = new HistoryService();

    String serverURL = Config.jcoreServerUrl;

    private static final String GET_ALL_SCENARIOS = "getAllScenarioIDs";
    private static final String LIST_ALL_SCENARIO_INSTANCES = "listAllScenarioInstancesForScenario";

    @Before
    public void setUp() {
        ExecutionService executionService = PowerMock.createPartialMock(ExecutionService.class,
                GET_ALL_SCENARIOS,
                LIST_ALL_SCENARIO_INSTANCES);
    }

    @Test
    public void testGetScenarios() {
        suppress(constructor(Connection.class));

        String getUrl = "/jcomparser/scenarios/0/";

        // mock all the static methods in a class called "Static"
        PowerMockito.mockStatic(Static.class);
        // use Mockito to set up your expectation
        Mockito.when(Static.firstStaticMethod(param)).thenReturn(value);
        Mockito.when(Static.secondStaticMethod()).thenReturn(123);

        // execute your test
        classCallStaticMethodObj.execute();

        // Different from Mockito, always use PowerMockito.verifyStatic() first
        PowerMockito.verifyStatic(Static.class, Mockito.times(2));
        // Use EasyMock-like verification semantic per static method invocation
        Static.firstStaticMethod(param);

        // Remember to call verifyStatic() again
        PowerMockito.verifyStatic(Static.class); // default is once
        Static.secondStaticMethod();

        // Again, remember to call verifyStatic()
        PowerMockito.verifyStatic(Static.class, Mockito.never());
        Static.thirdStaticMethod();


        //mock executionService class so we provide what we expect
        try {
           /*
            LinkedList ll = new LinkedList();
            ll.add(1);
            ll.add(2);
            ll.add(3);
            EasyMock.expect(GET_ALL_SCENARIOS).andStubReturn(ll);
            */

            PowerMock.expectPrivate(executionService, GET_ALL_SCENARIOS).andAnswer(new IAnswer<LinkedList>() {
                @Override
                public LinkedList answer() throws Throwable {
                    LinkedList ll = new LinkedList();
                    ll.add(1);
                    ll.add(2);
                    ll.add(3);
                    return (ll);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        //retrieve GET and check if it is as expected
        get(getUrl).
                then().
                assertThat().
                body(
                        matchesJsonSchemaInClasspath("json/all-scenarios-schema.json")
                );
    }

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
