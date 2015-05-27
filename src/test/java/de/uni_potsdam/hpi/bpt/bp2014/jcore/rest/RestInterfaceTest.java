package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.*;

/**
 * This Class extends the {@link de.uni_potsdam.hpi.bpt.bp2014.AbstractTest}
 * to test the RestInterface of the JEngine core.
 * In order to do so it uses the functionality of the
 * {@link org.glassfish.jersey.test.JerseyTest}
 * There are test methods for every possible REST Call.
 * In order to stay independent from existing tests, the
 * database will be set up before and after the execution.
 * Define the database Properties inside the database_connection file.
 */
public class RestInterfaceTest extends AbstractTest {

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2_schema.sql";
    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2RESTTest_new.sql";
    }
    /**
     * The base url of the jcore rest interface.
     * Allows us to send requests to the {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface}.
     */
    private WebTarget base;

    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }

    /**
     * When you sent a GET to {@link RestInterface#getScenarios(UriInfo, String)}
     * the media type of the response will be JSON.
     */
    @Test
    public void testGetScenarioProducesJson() {
        Response response = base.path("scenario").request().get();
        assertEquals("The Response code of get Scenario was not 200",
                200, response.getStatus());
        assertEquals("Get Scenarios returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * When you sent a get to {@link RestInterface#getScenarios(UriInfo, String)} 
     * the entity of the response will be a valid JSON array.
     */
    @Test
    public void testGetScenarioProducesValidJsonArray() {
        Response response = base.path("scenario").request().get();
        assertNotEquals("Get scenarios did not respond with a valid JSON Array",
                null, new JSONObject(response.readEntity(String.class)));
    }

    /**
     * When you sent a GET to {@link RestInterface#getScenarios(UriInfo, String)} 
     * the returned JSON will contain the latest version of all Scenarios.
     */
    @Test
    public void testGetScenarioContent() {
        Response response = base.path("scenario").request().get();
        assertThat("Get Scenarios did not contain the expected information",
                "{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134,135,136,138,139,140,141,142,143,144, 145],\"links\":{\"140\":\"http://localhost:9998/interface/v2/scenario/140\",\"141\":\"http://localhost:9998/interface/v2/scenario/141\",\"142\":\"http://localhost:9998/interface/v2/scenario/142\",\"143\":\"http://localhost:9998/interface/v2/scenario/143\",\"1\":\"http://localhost:9998/interface/v2/scenario/1\",\"100\":\"http://localhost:9998/interface/v2/scenario/100\",\"111\":\"http://localhost:9998/interface/v2/scenario/111\",\"144\":\"http://localhost:9998/interface/v2/scenario/144\",\"2\":\"http://localhost:9998/interface/v2/scenario/2\",\"101\":\"http://localhost:9998/interface/v2/scenario/101\",\"134\":\"http://localhost:9998/interface/v2/scenario/134\",\"3\":\"http://localhost:9998/interface/v2/scenario/3\",\"113\":\"http://localhost:9998/interface/v2/scenario/113\",\"135\":\"http://localhost:9998/interface/v2/scenario/135\",\"103\":\"http://localhost:9998/interface/v2/scenario/103\",\"114\":\"http://localhost:9998/interface/v2/scenario/114\",\"136\":\"http://localhost:9998/interface/v2/scenario/136\",\"115\":\"http://localhost:9998/interface/v2/scenario/115\",\"105\":\"http://localhost:9998/interface/v2/scenario/105\",\"116\":\"http://localhost:9998/interface/v2/scenario/116\",\"138\":\"http://localhost:9998/interface/v2/scenario/138\",\"117\":\"http://localhost:9998/interface/v2/scenario/117\",\"139\":\"http://localhost:9998/interface/v2/scenario/139\",\"118\":\"http://localhost:9998/interface/v2/scenario/118\",\"145\":\"http://localhost:9998/interface/v2/scenario/145\"},\"labels\":{\"1\":\"HELLOWORLD\",\"2\":\"helloWorld2\",\"3\":\"EmailTest\",\"100\":\"TestScenario\",\"101\":\"Test Insert Scenario\",\"134\":\"ReiseTestScenario\",\"103\":\"ScenarioTest1\",\"135\":\"ReiseTestScenario\",\"136\":\"TXOR1Scenario\",\"105\":\"TestScenarioTerminationCondition\",\"138\":\"TestEmail1Scenario\",\"139\":\"TestEmail1Scenario\",\"140\":\"TestEmail1Scenario\",\"141\":\"TestEmail2Scenario\",\"142\":\"TestEmail3Scenario\",\"111\":\"Test2_2ReferenceTest\",\"143\":\"TestEmail3Scenario\",\"144\":\"XORTest2Scenario\",\"113\":\"referencetest3_2\",\"114\":\"RT4Scenario\",\"115\":\"TT2Scenario\",\"116\":\"TT2Scenario\",\"117\":\"AT2Scenario\",\"118\":\"AT3Scenario\",\"145\":\"ServiceTaskSzenario\"}}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you sent a GET to {@link RestInterface#getScenarios(UriInfo, String)} and
     * you use a Filter
     * then the returned JSON will contain the latest version of all Scenarios with
     * a name containing the filterString.
     */
    @Test
    public void testGetScenarioContentWithFilter() {
        Response response = base.path("scenario").queryParam("filter", "HELLO").request().get();
        assertThat("Get Scenarios did not contain the expected information",
                "{\"ids\":[1,2],\"labels\":{\"1\":\"HELLOWORLD\",\"2\":\"helloWorld2\"},\"links\":{\"1\":\"http://localhost:9998/interface/v2/scenario/1\",\"2\":\"http://localhost:9998/interface/v2/scenario/2\"}}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a GET to {@link RestInterface#getScenario(UriInfo, int)} with an invalid id
     * a empty JSON with a 404 will be returned.
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

    /**
     * If you send a GET to {@link RestInterface#getScenario(UriInfo, int)}  with an valid id
     * a JSON containing the id, name and modelversion will be returned.
     */
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
                "{\"modelid\":0,\"instances\":\"http://localhost:9998/interface/v2/scenario/1/instance\",\"name\":\"HELLOWORLD\",\"id\":1,\"modelversion\":0}",
                jsonEquals(responseEntity).when(Option.IGNORING_ARRAY_ORDER));

    }

    /**
     * When you send a Get to {@link RestInterface#getScenarioInstances(UriInfo, int, String)} 
     * with valid params and no filter
     * then you get 200 a JSON Object.
     */
    @Test
    public void testGetScenarioInstancesReturnsOkAndJSON() {
        Response response = base.path("scenario/1/instance").request().get();
        assertEquals("The Response code of get get instances was not 200",
                200, response.getStatus());
        assertEquals("Get instances returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     * When you send a Get to {@link  RestInterface#getScenarioInstances(UriInfo, int, String)}
     * with an invalid scenario
     * then you get 404 with an error message inside the returned JSON object
     */
    @Test
    public void testGetScenarioInstancesInvalidScenario() {
        Response response = base.path("scenario/9999/instance").request().get();
        assertEquals("The Response code of get get instances was not 404",
                404, response.getStatus());
        assertEquals("Get instances returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON is invalid or does not contain the expected message",
                "{\"error\":\"Scenario not found!\"}",
                jsonEquals(response.readEntity(String.class)));
    }

    /**
     * When you send a Get to {@link  RestInterface#getScenarioInstances(UriInfo, int, String)}
     * with a valid scenario id
     * then a json object with all instances, id and label should be returned.
     * The schema should be:
     * {"ids": [1,2..], "names":{1: "abc" ...}}
     */
    @Test
    public void testGetScenarioInstancesReturnsCorrectJson() {
        Response response = base.path("scenario/1/instance").request().get();
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,92,94,95,97,99,101,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,212,214,215,216,217,218,219,220,221,222,223,224,226,228,244,246,248,250,252,255,257,259,261,262,263,265,266,270,279,281,282,284,285,286,294,296,309,310,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,333,334,346,348,349,351,353,356,358,359,361,363,365,366,367,369,370,371,372,373,374,375,376,377,378,379,380,381,383,385,386,387,388,390,392,394,396,398,400,402,404,406,408,409,410,411,412,413,415,416,418,419,420,422,430,433,438,441,444,445,446,448,449,452,456,459,460,461,464,467,470,473,476,479,482,485,488,491,492,493,494,497,505,515,519,527,531,552,561,563,572,582,590,598,605,612,614,621,628,635,642,649,656,663,670,677,684,691,698,705,712,719,726,733,740,747,754,756,763,770,777,784,791,809,816,823,836,846,853,860,867,895,915,951,952],\"links\":{\"590\":\"http://localhost:9998/interface/v2/scenario/1/instance/590\",\"470\":\"http://localhost:9998/interface/v2/scenario/1/instance/470\",\"351\":\"http://localhost:9998/interface/v2/scenario/1/instance/351\",\"110\":\"http://localhost:9998/interface/v2/scenario/1/instance/110\",\"473\":\"http://localhost:9998/interface/v2/scenario/1/instance/473\",\"111\":\"http://localhost:9998/interface/v2/scenario/1/instance/111\",\"353\":\"http://localhost:9998/interface/v2/scenario/1/instance/353\",\"112\":\"http://localhost:9998/interface/v2/scenario/1/instance/112\",\"113\":\"http://localhost:9998/interface/v2/scenario/1/instance/113\",\"476\":\"http://localhost:9998/interface/v2/scenario/1/instance/476\",\"114\":\"http://localhost:9998/interface/v2/scenario/1/instance/114\",\"356\":\"http://localhost:9998/interface/v2/scenario/1/instance/356\",\"598\":\"http://localhost:9998/interface/v2/scenario/1/instance/598\",\"115\":\"http://localhost:9998/interface/v2/scenario/1/instance/115\",\"116\":\"http://localhost:9998/interface/v2/scenario/1/instance/116\",\"358\":\"http://localhost:9998/interface/v2/scenario/1/instance/358\",\"479\":\"http://localhost:9998/interface/v2/scenario/1/instance/479\",\"117\":\"http://localhost:9998/interface/v2/scenario/1/instance/117\",\"359\":\"http://localhost:9998/interface/v2/scenario/1/instance/359\",\"118\":\"http://localhost:9998/interface/v2/scenario/1/instance/118\",\"119\":\"http://localhost:9998/interface/v2/scenario/1/instance/119\",\"915\":\"http://localhost:9998/interface/v2/scenario/1/instance/915\",\"361\":\"http://localhost:9998/interface/v2/scenario/1/instance/361\",\"482\":\"http://localhost:9998/interface/v2/scenario/1/instance/482\",\"120\":\"http://localhost:9998/interface/v2/scenario/1/instance/120\",\"121\":\"http://localhost:9998/interface/v2/scenario/1/instance/121\",\"363\":\"http://localhost:9998/interface/v2/scenario/1/instance/363\",\"122\":\"http://localhost:9998/interface/v2/scenario/1/instance/122\",\"485\":\"http://localhost:9998/interface/v2/scenario/1/instance/485\",\"123\":\"http://localhost:9998/interface/v2/scenario/1/instance/123\",\"244\":\"http://localhost:9998/interface/v2/scenario/1/instance/244\",\"365\":\"http://localhost:9998/interface/v2/scenario/1/instance/365\",\"124\":\"http://localhost:9998/interface/v2/scenario/1/instance/124\",\"366\":\"http://localhost:9998/interface/v2/scenario/1/instance/366\",\"125\":\"http://localhost:9998/interface/v2/scenario/1/instance/125\",\"246\":\"http://localhost:9998/interface/v2/scenario/1/instance/246\",\"367\":\"http://localhost:9998/interface/v2/scenario/1/instance/367\",\"488\":\"http://localhost:9998/interface/v2/scenario/1/instance/488\",\"126\":\"http://localhost:9998/interface/v2/scenario/1/instance/126\",\"127\":\"http://localhost:9998/interface/v2/scenario/1/instance/127\",\"248\":\"http://localhost:9998/interface/v2/scenario/1/instance/248\",\"369\":\"http://localhost:9998/interface/v2/scenario/1/instance/369\",\"128\":\"http://localhost:9998/interface/v2/scenario/1/instance/128\",\"129\":\"http://localhost:9998/interface/v2/scenario/1/instance/129\",\"809\":\"http://localhost:9998/interface/v2/scenario/1/instance/809\",\"370\":\"http://localhost:9998/interface/v2/scenario/1/instance/370\",\"491\":\"http://localhost:9998/interface/v2/scenario/1/instance/491\",\"250\":\"http://localhost:9998/interface/v2/scenario/1/instance/250\",\"371\":\"http://localhost:9998/interface/v2/scenario/1/instance/371\",\"492\":\"http://localhost:9998/interface/v2/scenario/1/instance/492\",\"130\":\"http://localhost:9998/interface/v2/scenario/1/instance/130\",\"372\":\"http://localhost:9998/interface/v2/scenario/1/instance/372\",\"493\":\"http://localhost:9998/interface/v2/scenario/1/instance/493\",\"131\":\"http://localhost:9998/interface/v2/scenario/1/instance/131\",\"252\":\"http://localhost:9998/interface/v2/scenario/1/instance/252\",\"373\":\"http://localhost:9998/interface/v2/scenario/1/instance/373\",\"494\":\"http://localhost:9998/interface/v2/scenario/1/instance/494\",\"132\":\"http://localhost:9998/interface/v2/scenario/1/instance/132\",\"374\":\"http://localhost:9998/interface/v2/scenario/1/instance/374\",\"133\":\"http://localhost:9998/interface/v2/scenario/1/instance/133\",\"375\":\"http://localhost:9998/interface/v2/scenario/1/instance/375\",\"134\":\"http://localhost:9998/interface/v2/scenario/1/instance/134\",\"255\":\"http://localhost:9998/interface/v2/scenario/1/instance/255\",\"376\":\"http://localhost:9998/interface/v2/scenario/1/instance/376\",\"497\":\"http://localhost:9998/interface/v2/scenario/1/instance/497\",\"135\":\"http://localhost:9998/interface/v2/scenario/1/instance/135\",\"377\":\"http://localhost:9998/interface/v2/scenario/1/instance/377\",\"136\":\"http://localhost:9998/interface/v2/scenario/1/instance/136\",\"257\":\"http://localhost:9998/interface/v2/scenario/1/instance/257\",\"378\":\"http://localhost:9998/interface/v2/scenario/1/instance/378\",\"137\":\"http://localhost:9998/interface/v2/scenario/1/instance/137\",\"379\":\"http://localhost:9998/interface/v2/scenario/1/instance/379\",\"138\":\"http://localhost:9998/interface/v2/scenario/1/instance/138\",\"259\":\"http://localhost:9998/interface/v2/scenario/1/instance/259\",\"139\":\"http://localhost:9998/interface/v2/scenario/1/instance/139\",\"816\":\"http://localhost:9998/interface/v2/scenario/1/instance/816\",\"380\":\"http://localhost:9998/interface/v2/scenario/1/instance/380\",\"381\":\"http://localhost:9998/interface/v2/scenario/1/instance/381\",\"140\":\"http://localhost:9998/interface/v2/scenario/1/instance/140\",\"261\":\"http://localhost:9998/interface/v2/scenario/1/instance/261\",\"141\":\"http://localhost:9998/interface/v2/scenario/1/instance/141\",\"262\":\"http://localhost:9998/interface/v2/scenario/1/instance/262\",\"383\":\"http://localhost:9998/interface/v2/scenario/1/instance/383\",\"142\":\"http://localhost:9998/interface/v2/scenario/1/instance/142\",\"263\":\"http://localhost:9998/interface/v2/scenario/1/instance/263\",\"143\":\"http://localhost:9998/interface/v2/scenario/1/instance/143\",\"385\":\"http://localhost:9998/interface/v2/scenario/1/instance/385\",\"144\":\"http://localhost:9998/interface/v2/scenario/1/instance/144\",\"265\":\"http://localhost:9998/interface/v2/scenario/1/instance/265\",\"386\":\"http://localhost:9998/interface/v2/scenario/1/instance/386\",\"145\":\"http://localhost:9998/interface/v2/scenario/1/instance/145\",\"266\":\"http://localhost:9998/interface/v2/scenario/1/instance/266\",\"387\":\"http://localhost:9998/interface/v2/scenario/1/instance/387\",\"146\":\"http://localhost:9998/interface/v2/scenario/1/instance/146\",\"388\":\"http://localhost:9998/interface/v2/scenario/1/instance/388\",\"147\":\"http://localhost:9998/interface/v2/scenario/1/instance/147\",\"148\":\"http://localhost:9998/interface/v2/scenario/1/instance/148\",\"149\":\"http://localhost:9998/interface/v2/scenario/1/instance/149\",\"823\":\"http://localhost:9998/interface/v2/scenario/1/instance/823\",\"705\":\"http://localhost:9998/interface/v2/scenario/1/instance/705\",\"47\":\"http://localhost:9998/interface/v2/scenario/1/instance/47\",\"48\":\"http://localhost:9998/interface/v2/scenario/1/instance/48\",\"49\":\"http://localhost:9998/interface/v2/scenario/1/instance/49\",\"390\":\"http://localhost:9998/interface/v2/scenario/1/instance/390\",\"270\":\"http://localhost:9998/interface/v2/scenario/1/instance/270\",\"150\":\"http://localhost:9998/interface/v2/scenario/1/instance/150\",\"392\":\"http://localhost:9998/interface/v2/scenario/1/instance/392\",\"151\":\"http://localhost:9998/interface/v2/scenario/1/instance/151\",\"152\":\"http://localhost:9998/interface/v2/scenario/1/instance/152\",\"394\":\"http://localhost:9998/interface/v2/scenario/1/instance/394\",\"153\":\"http://localhost:9998/interface/v2/scenario/1/instance/153\",\"154\":\"http://localhost:9998/interface/v2/scenario/1/instance/154\",\"396\":\"http://localhost:9998/interface/v2/scenario/1/instance/396\",\"155\":\"http://localhost:9998/interface/v2/scenario/1/instance/155\",\"156\":\"http://localhost:9998/interface/v2/scenario/1/instance/156\",\"398\":\"http://localhost:9998/interface/v2/scenario/1/instance/398\",\"157\":\"http://localhost:9998/interface/v2/scenario/1/instance/157\",\"158\":\"http://localhost:9998/interface/v2/scenario/1/instance/158\",\"279\":\"http://localhost:9998/interface/v2/scenario/1/instance/279\",\"159\":\"http://localhost:9998/interface/v2/scenario/1/instance/159\",\"951\":\"http://localhost:9998/interface/v2/scenario/1/instance/951\",\"952\":\"http://localhost:9998/interface/v2/scenario/1/instance/952\",\"712\":\"http://localhost:9998/interface/v2/scenario/1/instance/712\",\"50\":\"http://localhost:9998/interface/v2/scenario/1/instance/50\",\"836\":\"http://localhost:9998/interface/v2/scenario/1/instance/836\",\"51\":\"http://localhost:9998/interface/v2/scenario/1/instance/51\",\"52\":\"http://localhost:9998/interface/v2/scenario/1/instance/52\",\"53\":\"http://localhost:9998/interface/v2/scenario/1/instance/53\",\"54\":\"http://localhost:9998/interface/v2/scenario/1/instance/54\",\"719\":\"http://localhost:9998/interface/v2/scenario/1/instance/719\",\"55\":\"http://localhost:9998/interface/v2/scenario/1/instance/55\",\"56\":\"http://localhost:9998/interface/v2/scenario/1/instance/56\",\"57\":\"http://localhost:9998/interface/v2/scenario/1/instance/57\",\"58\":\"http://localhost:9998/interface/v2/scenario/1/instance/58\",\"59\":\"http://localhost:9998/interface/v2/scenario/1/instance/59\",\"160\":\"http://localhost:9998/interface/v2/scenario/1/instance/160\",\"281\":\"http://localhost:9998/interface/v2/scenario/1/instance/281\",\"161\":\"http://localhost:9998/interface/v2/scenario/1/instance/161\",\"282\":\"http://localhost:9998/interface/v2/scenario/1/instance/282\",\"162\":\"http://localhost:9998/interface/v2/scenario/1/instance/162\",\"163\":\"http://localhost:9998/interface/v2/scenario/1/instance/163\",\"284\":\"http://localhost:9998/interface/v2/scenario/1/instance/284\",\"164\":\"http://localhost:9998/interface/v2/scenario/1/instance/164\",\"285\":\"http://localhost:9998/interface/v2/scenario/1/instance/285\",\"165\":\"http://localhost:9998/interface/v2/scenario/1/instance/165\",\"286\":\"http://localhost:9998/interface/v2/scenario/1/instance/286\",\"166\":\"http://localhost:9998/interface/v2/scenario/1/instance/166\",\"167\":\"http://localhost:9998/interface/v2/scenario/1/instance/167\",\"168\":\"http://localhost:9998/interface/v2/scenario/1/instance/168\",\"169\":\"http://localhost:9998/interface/v2/scenario/1/instance/169\",\"60\":\"http://localhost:9998/interface/v2/scenario/1/instance/60\",\"846\":\"http://localhost:9998/interface/v2/scenario/1/instance/846\",\"61\":\"http://localhost:9998/interface/v2/scenario/1/instance/61\",\"605\":\"http://localhost:9998/interface/v2/scenario/1/instance/605\",\"726\":\"http://localhost:9998/interface/v2/scenario/1/instance/726\",\"62\":\"http://localhost:9998/interface/v2/scenario/1/instance/62\",\"63\":\"http://localhost:9998/interface/v2/scenario/1/instance/63\",\"64\":\"http://localhost:9998/interface/v2/scenario/1/instance/64\",\"65\":\"http://localhost:9998/interface/v2/scenario/1/instance/65\",\"66\":\"http://localhost:9998/interface/v2/scenario/1/instance/66\",\"67\":\"http://localhost:9998/interface/v2/scenario/1/instance/67\",\"68\":\"http://localhost:9998/interface/v2/scenario/1/instance/68\",\"69\":\"http://localhost:9998/interface/v2/scenario/1/instance/69\",\"170\":\"http://localhost:9998/interface/v2/scenario/1/instance/170\",\"171\":\"http://localhost:9998/interface/v2/scenario/1/instance/171\",\"172\":\"http://localhost:9998/interface/v2/scenario/1/instance/172\",\"173\":\"http://localhost:9998/interface/v2/scenario/1/instance/173\",\"294\":\"http://localhost:9998/interface/v2/scenario/1/instance/294\",\"174\":\"http://localhost:9998/interface/v2/scenario/1/instance/174\",\"175\":\"http://localhost:9998/interface/v2/scenario/1/instance/175\",\"296\":\"http://localhost:9998/interface/v2/scenario/1/instance/296\",\"176\":\"http://localhost:9998/interface/v2/scenario/1/instance/176\",\"177\":\"http://localhost:9998/interface/v2/scenario/1/instance/177\",\"178\":\"http://localhost:9998/interface/v2/scenario/1/instance/178\",\"179\":\"http://localhost:9998/interface/v2/scenario/1/instance/179\",\"853\":\"http://localhost:9998/interface/v2/scenario/1/instance/853\",\"612\":\"http://localhost:9998/interface/v2/scenario/1/instance/612\",\"733\":\"http://localhost:9998/interface/v2/scenario/1/instance/733\",\"70\":\"http://localhost:9998/interface/v2/scenario/1/instance/70\",\"614\":\"http://localhost:9998/interface/v2/scenario/1/instance/614\",\"71\":\"http://localhost:9998/interface/v2/scenario/1/instance/71\",\"72\":\"http://localhost:9998/interface/v2/scenario/1/instance/72\",\"73\":\"http://localhost:9998/interface/v2/scenario/1/instance/73\",\"74\":\"http://localhost:9998/interface/v2/scenario/1/instance/74\",\"75\":\"http://localhost:9998/interface/v2/scenario/1/instance/75\",\"76\":\"http://localhost:9998/interface/v2/scenario/1/instance/76\",\"77\":\"http://localhost:9998/interface/v2/scenario/1/instance/77\",\"78\":\"http://localhost:9998/interface/v2/scenario/1/instance/78\",\"79\":\"http://localhost:9998/interface/v2/scenario/1/instance/79\",\"180\":\"http://localhost:9998/interface/v2/scenario/1/instance/180\",\"181\":\"http://localhost:9998/interface/v2/scenario/1/instance/181\",\"182\":\"http://localhost:9998/interface/v2/scenario/1/instance/182\",\"183\":\"http://localhost:9998/interface/v2/scenario/1/instance/183\",\"184\":\"http://localhost:9998/interface/v2/scenario/1/instance/184\",\"185\":\"http://localhost:9998/interface/v2/scenario/1/instance/185\",\"186\":\"http://localhost:9998/interface/v2/scenario/1/instance/186\",\"187\":\"http://localhost:9998/interface/v2/scenario/1/instance/187\",\"188\":\"http://localhost:9998/interface/v2/scenario/1/instance/188\",\"189\":\"http://localhost:9998/interface/v2/scenario/1/instance/189\",\"860\":\"http://localhost:9998/interface/v2/scenario/1/instance/860\",\"740\":\"http://localhost:9998/interface/v2/scenario/1/instance/740\",\"621\":\"http://localhost:9998/interface/v2/scenario/1/instance/621\",\"80\":\"http://localhost:9998/interface/v2/scenario/1/instance/80\",\"81\":\"http://localhost:9998/interface/v2/scenario/1/instance/81\",\"867\":\"http://localhost:9998/interface/v2/scenario/1/instance/867\",\"82\":\"http://localhost:9998/interface/v2/scenario/1/instance/82\",\"505\":\"http://localhost:9998/interface/v2/scenario/1/instance/505\",\"747\":\"http://localhost:9998/interface/v2/scenario/1/instance/747\",\"83\":\"http://localhost:9998/interface/v2/scenario/1/instance/83\",\"84\":\"http://localhost:9998/interface/v2/scenario/1/instance/84\",\"628\":\"http://localhost:9998/interface/v2/scenario/1/instance/628\",\"190\":\"http://localhost:9998/interface/v2/scenario/1/instance/190\",\"191\":\"http://localhost:9998/interface/v2/scenario/1/instance/191\",\"192\":\"http://localhost:9998/interface/v2/scenario/1/instance/192\",\"193\":\"http://localhost:9998/interface/v2/scenario/1/instance/193\",\"194\":\"http://localhost:9998/interface/v2/scenario/1/instance/194\",\"195\":\"http://localhost:9998/interface/v2/scenario/1/instance/195\",\"196\":\"http://localhost:9998/interface/v2/scenario/1/instance/196\",\"197\":\"http://localhost:9998/interface/v2/scenario/1/instance/197\",\"198\":\"http://localhost:9998/interface/v2/scenario/1/instance/198\",\"199\":\"http://localhost:9998/interface/v2/scenario/1/instance/199\",\"754\":\"http://localhost:9998/interface/v2/scenario/1/instance/754\",\"635\":\"http://localhost:9998/interface/v2/scenario/1/instance/635\",\"756\":\"http://localhost:9998/interface/v2/scenario/1/instance/756\",\"92\":\"http://localhost:9998/interface/v2/scenario/1/instance/92\",\"515\":\"http://localhost:9998/interface/v2/scenario/1/instance/515\",\"94\":\"http://localhost:9998/interface/v2/scenario/1/instance/94\",\"95\":\"http://localhost:9998/interface/v2/scenario/1/instance/95\",\"519\":\"http://localhost:9998/interface/v2/scenario/1/instance/519\",\"97\":\"http://localhost:9998/interface/v2/scenario/1/instance/97\",\"99\":\"http://localhost:9998/interface/v2/scenario/1/instance/99\",\"400\":\"http://localhost:9998/interface/v2/scenario/1/instance/400\",\"642\":\"http://localhost:9998/interface/v2/scenario/1/instance/642\",\"763\":\"http://localhost:9998/interface/v2/scenario/1/instance/763\",\"402\":\"http://localhost:9998/interface/v2/scenario/1/instance/402\",\"404\":\"http://localhost:9998/interface/v2/scenario/1/instance/404\",\"406\":\"http://localhost:9998/interface/v2/scenario/1/instance/406\",\"527\":\"http://localhost:9998/interface/v2/scenario/1/instance/527\",\"649\":\"http://localhost:9998/interface/v2/scenario/1/instance/649\",\"408\":\"http://localhost:9998/interface/v2/scenario/1/instance/408\",\"409\":\"http://localhost:9998/interface/v2/scenario/1/instance/409\",\"770\":\"http://localhost:9998/interface/v2/scenario/1/instance/770\",\"410\":\"http://localhost:9998/interface/v2/scenario/1/instance/410\",\"531\":\"http://localhost:9998/interface/v2/scenario/1/instance/531\",\"411\":\"http://localhost:9998/interface/v2/scenario/1/instance/411\",\"895\":\"http://localhost:9998/interface/v2/scenario/1/instance/895\",\"412\":\"http://localhost:9998/interface/v2/scenario/1/instance/412\",\"413\":\"http://localhost:9998/interface/v2/scenario/1/instance/413\",\"656\":\"http://localhost:9998/interface/v2/scenario/1/instance/656\",\"777\":\"http://localhost:9998/interface/v2/scenario/1/instance/777\",\"415\":\"http://localhost:9998/interface/v2/scenario/1/instance/415\",\"416\":\"http://localhost:9998/interface/v2/scenario/1/instance/416\",\"418\":\"http://localhost:9998/interface/v2/scenario/1/instance/418\",\"419\":\"http://localhost:9998/interface/v2/scenario/1/instance/419\",\"420\":\"http://localhost:9998/interface/v2/scenario/1/instance/420\",\"663\":\"http://localhost:9998/interface/v2/scenario/1/instance/663\",\"784\":\"http://localhost:9998/interface/v2/scenario/1/instance/784\",\"422\":\"http://localhost:9998/interface/v2/scenario/1/instance/422\",\"309\":\"http://localhost:9998/interface/v2/scenario/1/instance/309\",\"670\":\"http://localhost:9998/interface/v2/scenario/1/instance/670\",\"791\":\"http://localhost:9998/interface/v2/scenario/1/instance/791\",\"430\":\"http://localhost:9998/interface/v2/scenario/1/instance/430\",\"310\":\"http://localhost:9998/interface/v2/scenario/1/instance/310\",\"552\":\"http://localhost:9998/interface/v2/scenario/1/instance/552\",\"312\":\"http://localhost:9998/interface/v2/scenario/1/instance/312\",\"433\":\"http://localhost:9998/interface/v2/scenario/1/instance/433\",\"313\":\"http://localhost:9998/interface/v2/scenario/1/instance/313\",\"314\":\"http://localhost:9998/interface/v2/scenario/1/instance/314\",\"677\":\"http://localhost:9998/interface/v2/scenario/1/instance/677\",\"315\":\"http://localhost:9998/interface/v2/scenario/1/instance/315\",\"316\":\"http://localhost:9998/interface/v2/scenario/1/instance/316\",\"317\":\"http://localhost:9998/interface/v2/scenario/1/instance/317\",\"438\":\"http://localhost:9998/interface/v2/scenario/1/instance/438\",\"318\":\"http://localhost:9998/interface/v2/scenario/1/instance/318\",\"319\":\"http://localhost:9998/interface/v2/scenario/1/instance/319\",\"561\":\"http://localhost:9998/interface/v2/scenario/1/instance/561\",\"320\":\"http://localhost:9998/interface/v2/scenario/1/instance/320\",\"441\":\"http://localhost:9998/interface/v2/scenario/1/instance/441\",\"200\":\"http://localhost:9998/interface/v2/scenario/1/instance/200\",\"321\":\"http://localhost:9998/interface/v2/scenario/1/instance/321\",\"563\":\"http://localhost:9998/interface/v2/scenario/1/instance/563\",\"684\":\"http://localhost:9998/interface/v2/scenario/1/instance/684\",\"201\":\"http://localhost:9998/interface/v2/scenario/1/instance/201\",\"322\":\"http://localhost:9998/interface/v2/scenario/1/instance/322\",\"202\":\"http://localhost:9998/interface/v2/scenario/1/instance/202\",\"323\":\"http://localhost:9998/interface/v2/scenario/1/instance/323\",\"444\":\"http://localhost:9998/interface/v2/scenario/1/instance/444\",\"203\":\"http://localhost:9998/interface/v2/scenario/1/instance/203\",\"324\":\"http://localhost:9998/interface/v2/scenario/1/instance/324\",\"445\":\"http://localhost:9998/interface/v2/scenario/1/instance/445\",\"204\":\"http://localhost:9998/interface/v2/scenario/1/instance/204\",\"325\":\"http://localhost:9998/interface/v2/scenario/1/instance/325\",\"446\":\"http://localhost:9998/interface/v2/scenario/1/instance/446\",\"205\":\"http://localhost:9998/interface/v2/scenario/1/instance/205\",\"326\":\"http://localhost:9998/interface/v2/scenario/1/instance/326\",\"206\":\"http://localhost:9998/interface/v2/scenario/1/instance/206\",\"327\":\"http://localhost:9998/interface/v2/scenario/1/instance/327\",\"448\":\"http://localhost:9998/interface/v2/scenario/1/instance/448\",\"207\":\"http://localhost:9998/interface/v2/scenario/1/instance/207\",\"328\":\"http://localhost:9998/interface/v2/scenario/1/instance/328\",\"449\":\"http://localhost:9998/interface/v2/scenario/1/instance/449\",\"208\":\"http://localhost:9998/interface/v2/scenario/1/instance/208\",\"209\":\"http://localhost:9998/interface/v2/scenario/1/instance/209\",\"691\":\"http://localhost:9998/interface/v2/scenario/1/instance/691\",\"572\":\"http://localhost:9998/interface/v2/scenario/1/instance/572\",\"210\":\"http://localhost:9998/interface/v2/scenario/1/instance/210\",\"452\":\"http://localhost:9998/interface/v2/scenario/1/instance/452\",\"212\":\"http://localhost:9998/interface/v2/scenario/1/instance/212\",\"333\":\"http://localhost:9998/interface/v2/scenario/1/instance/333\",\"334\":\"http://localhost:9998/interface/v2/scenario/1/instance/334\",\"214\":\"http://localhost:9998/interface/v2/scenario/1/instance/214\",\"456\":\"http://localhost:9998/interface/v2/scenario/1/instance/456\",\"698\":\"http://localhost:9998/interface/v2/scenario/1/instance/698\",\"215\":\"http://localhost:9998/interface/v2/scenario/1/instance/215\",\"216\":\"http://localhost:9998/interface/v2/scenario/1/instance/216\",\"217\":\"http://localhost:9998/interface/v2/scenario/1/instance/217\",\"459\":\"http://localhost:9998/interface/v2/scenario/1/instance/459\",\"218\":\"http://localhost:9998/interface/v2/scenario/1/instance/218\",\"219\":\"http://localhost:9998/interface/v2/scenario/1/instance/219\",\"460\":\"http://localhost:9998/interface/v2/scenario/1/instance/460\",\"461\":\"http://localhost:9998/interface/v2/scenario/1/instance/461\",\"582\":\"http://localhost:9998/interface/v2/scenario/1/instance/582\",\"220\":\"http://localhost:9998/interface/v2/scenario/1/instance/220\",\"221\":\"http://localhost:9998/interface/v2/scenario/1/instance/221\",\"101\":\"http://localhost:9998/interface/v2/scenario/1/instance/101\",\"222\":\"http://localhost:9998/interface/v2/scenario/1/instance/222\",\"464\":\"http://localhost:9998/interface/v2/scenario/1/instance/464\",\"223\":\"http://localhost:9998/interface/v2/scenario/1/instance/223\",\"103\":\"http://localhost:9998/interface/v2/scenario/1/instance/103\",\"224\":\"http://localhost:9998/interface/v2/scenario/1/instance/224\",\"104\":\"http://localhost:9998/interface/v2/scenario/1/instance/104\",\"346\":\"http://localhost:9998/interface/v2/scenario/1/instance/346\",\"467\":\"http://localhost:9998/interface/v2/scenario/1/instance/467\",\"105\":\"http://localhost:9998/interface/v2/scenario/1/instance/105\",\"226\":\"http://localhost:9998/interface/v2/scenario/1/instance/226\",\"106\":\"http://localhost:9998/interface/v2/scenario/1/instance/106\",\"348\":\"http://localhost:9998/interface/v2/scenario/1/instance/348\",\"107\":\"http://localhost:9998/interface/v2/scenario/1/instance/107\",\"228\":\"http://localhost:9998/interface/v2/scenario/1/instance/228\",\"349\":\"http://localhost:9998/interface/v2/scenario/1/instance/349\",\"108\":\"http://localhost:9998/interface/v2/scenario/1/instance/108\",\"109\":\"http://localhost:9998/interface/v2/scenario/1/instance/109\"},\"labels\":{\"515\":\"HELLOWORLD\",\"519\":\"HELLOWORLD\",\"527\":\"HELLOWORLD\",\"531\":\"HELLOWORLD\",\"552\":\"HELLOWORLD\",\"47\":\"HELLOWORLD\",\"48\":\"HELLOWORLD\",\"49\":\"HELLOWORLD\",\"561\":\"HELLOWORLD\",\"50\":\"HELLOWORLD\",\"51\":\"HELLOWORLD\",\"563\":\"HELLOWORLD\",\"52\":\"HELLOWORLD\",\"53\":\"HELLOWORLD\",\"54\":\"HELLOWORLD\",\"55\":\"HELLOWORLD\",\"56\":\"HELLOWORLD\",\"57\":\"HELLOWORLD\",\"58\":\"HELLOWORLD\",\"59\":\"HELLOWORLD\",\"60\":\"HELLOWORLD\",\"572\":\"HELLOWORLD\",\"61\":\"HELLOWORLD\",\"62\":\"HELLOWORLD\",\"63\":\"HELLOWORLD\",\"64\":\"HELLOWORLD\",\"65\":\"HELLOWORLD\",\"66\":\"HELLOWORLD\",\"67\":\"HELLOWORLD\",\"68\":\"HELLOWORLD\",\"69\":\"HELLOWORLD\",\"70\":\"HELLOWORLD\",\"582\":\"HELLOWORLD\",\"71\":\"HELLOWORLD\",\"72\":\"HELLOWORLD\",\"73\":\"HELLOWORLD\",\"74\":\"HELLOWORLD\",\"75\":\"HELLOWORLD\",\"76\":\"HELLOWORLD\",\"77\":\"HELLOWORLD\",\"78\":\"HELLOWORLD\",\"590\":\"HELLOWORLD\",\"79\":\"HELLOWORLD\",\"80\":\"HELLOWORLD\",\"81\":\"HELLOWORLD\",\"82\":\"HELLOWORLD\",\"83\":\"HELLOWORLD\",\"84\":\"HELLOWORLD\",\"598\":\"HELLOWORLD\",\"92\":\"HELLOWORLD\",\"605\":\"HELLOWORLD\",\"94\":\"HELLOWORLD\",\"95\":\"HELLOWORLD\",\"97\":\"HELLOWORLD\",\"99\":\"HELLOWORLD\",\"612\":\"HELLOWORLD\",\"101\":\"HELLOWORLD\",\"614\":\"HELLOWORLD\",\"103\":\"HELLOWORLD\",\"104\":\"HELLOWORLD\",\"105\":\"HELLOWORLD\",\"106\":\"HELLOWORLD\",\"107\":\"HELLOWORLD\",\"108\":\"HELLOWORLD\",\"109\":\"HELLOWORLD\",\"621\":\"HELLOWORLD\",\"110\":\"HELLOWORLD\",\"111\":\"HELLOWORLD\",\"112\":\"HELLOWORLD\",\"113\":\"HELLOWORLD\",\"114\":\"HELLOWORLD\",\"115\":\"HELLOWORLD\",\"116\":\"HELLOWORLD\",\"628\":\"HELLOWORLD\",\"117\":\"HELLOWORLD\",\"118\":\"HELLOWORLD\",\"119\":\"HELLOWORLD\",\"120\":\"HELLOWORLD\",\"121\":\"HELLOWORLD\",\"122\":\"HELLOWORLD\",\"123\":\"HELLOWORLD\",\"635\":\"HELLOWORLD\",\"124\":\"HELLOWORLD\",\"125\":\"HELLOWORLD\",\"126\":\"HELLOWORLD\",\"127\":\"HELLOWORLD\",\"128\":\"HELLOWORLD\",\"129\":\"HELLOWORLD\",\"130\":\"HELLOWORLD\",\"642\":\"HELLOWORLD\",\"131\":\"HELLOWORLD\",\"132\":\"HELLOWORLD\",\"133\":\"HELLOWORLD\",\"134\":\"HELLOWORLD\",\"135\":\"HELLOWORLD\",\"136\":\"HELLOWORLD\",\"137\":\"HELLOWORLD\",\"649\":\"HELLOWORLD\",\"138\":\"HELLOWORLD\",\"139\":\"HELLOWORLD\",\"140\":\"HELLOWORLD\",\"141\":\"HELLOWORLD\",\"142\":\"HELLOWORLD\",\"143\":\"HELLOWORLD\",\"144\":\"HELLOWORLD\",\"656\":\"HELLOWORLD\",\"145\":\"HELLOWORLD\",\"146\":\"HELLOWORLD\",\"147\":\"HELLOWORLD\",\"148\":\"HELLOWORLD\",\"149\":\"HELLOWORLD\",\"150\":\"HELLOWORLD\",\"151\":\"HELLOWORLD\",\"663\":\"HELLOWORLD\",\"152\":\"HELLOWORLD\",\"153\":\"HELLOWORLD\",\"154\":\"HELLOWORLD\",\"155\":\"HELLOWORLD\",\"156\":\"HELLOWORLD\",\"157\":\"HELLOWORLD\",\"158\":\"HELLOWORLD\",\"670\":\"HELLOWORLD\",\"159\":\"HELLOWORLD\",\"160\":\"HELLOWORLD\",\"161\":\"HELLOWORLD\",\"162\":\"HELLOWORLD\",\"163\":\"HELLOWORLD\",\"164\":\"HELLOWORLD\",\"165\":\"HELLOWORLD\",\"677\":\"HELLOWORLD\",\"166\":\"HELLOWORLD\",\"167\":\"HELLOWORLD\",\"168\":\"HELLOWORLD\",\"169\":\"HELLOWORLD\",\"170\":\"HELLOWORLD\",\"171\":\"HELLOWORLD\",\"172\":\"HELLOWORLD\",\"684\":\"HELLOWORLD\",\"173\":\"HELLOWORLD\",\"174\":\"HELLOWORLD\",\"175\":\"HELLOWORLD\",\"176\":\"HELLOWORLD\",\"177\":\"HELLOWORLD\",\"178\":\"HELLOWORLD\",\"179\":\"HELLOWORLD\",\"691\":\"HELLOWORLD\",\"180\":\"HELLOWORLD\",\"181\":\"HELLOWORLD\",\"182\":\"HELLOWORLD\",\"183\":\"HELLOWORLD\",\"184\":\"HELLOWORLD\",\"185\":\"HELLOWORLD\",\"186\":\"HELLOWORLD\",\"698\":\"HELLOWORLD\",\"187\":\"HELLOWORLD\",\"188\":\"HELLOWORLD\",\"189\":\"HELLOWORLD\",\"190\":\"HELLOWORLD\",\"191\":\"HELLOWORLD\",\"192\":\"HELLOWORLD\",\"193\":\"HELLOWORLD\",\"705\":\"HELLOWORLD\",\"194\":\"HELLOWORLD\",\"195\":\"HELLOWORLD\",\"196\":\"HELLOWORLD\",\"197\":\"HELLOWORLD\",\"198\":\"HELLOWORLD\",\"199\":\"HELLOWORLD\",\"200\":\"HELLOWORLD\",\"712\":\"HELLOWORLD\",\"201\":\"HELLOWORLD\",\"202\":\"HELLOWORLD\",\"203\":\"HELLOWORLD\",\"204\":\"HELLOWORLD\",\"205\":\"HELLOWORLD\",\"206\":\"HELLOWORLD\",\"207\":\"HELLOWORLD\",\"719\":\"HELLOWORLD\",\"208\":\"HELLOWORLD\",\"209\":\"HELLOWORLD\",\"210\":\"HELLOWORLD\",\"212\":\"HELLOWORLD\",\"214\":\"HELLOWORLD\",\"726\":\"HELLOWORLD\",\"215\":\"HELLOWORLD\",\"216\":\"HELLOWORLD\",\"217\":\"HELLOWORLD\",\"218\":\"HELLOWORLD\",\"219\":\"HELLOWORLD\",\"220\":\"HELLOWORLD\",\"221\":\"HELLOWORLD\",\"733\":\"HELLOWORLD\",\"222\":\"HELLOWORLD\",\"223\":\"HELLOWORLD\",\"224\":\"HELLOWORLD\",\"226\":\"HELLOWORLD\",\"228\":\"HELLOWORLD\",\"740\":\"HELLOWORLD\",\"747\":\"HELLOWORLD\",\"754\":\"HELLOWORLD\",\"244\":\"HELLOWORLD\",\"756\":\"HELLOWORLD\",\"246\":\"HELLOWORLD\",\"248\":\"HELLOWORLD\",\"250\":\"HELLOWORLD\",\"763\":\"HELLOWORLD\",\"252\":\"HELLOWORLD\",\"255\":\"HELLOWORLD\",\"257\":\"HELLOWORLD\",\"770\":\"HELLOWORLD\",\"259\":\"HELLOWORLD\",\"261\":\"HELLOWORLD\",\"262\":\"HELLOWORLD\",\"263\":\"HELLOWORLD\",\"265\":\"HELLOWORLD\",\"777\":\"HELLOWORLD\",\"266\":\"HELLOWORLD\",\"270\":\"HELLOWORLD\",\"784\":\"HELLOWORLD\",\"279\":\"HELLOWORLD\",\"791\":\"HELLOWORLD\",\"281\":\"HELLOWORLD\",\"282\":\"HELLOWORLD\",\"284\":\"HELLOWORLD\",\"285\":\"HELLOWORLD\",\"286\":\"HELLOWORLD\",\"294\":\"HELLOWORLD\",\"296\":\"HELLOWORLD\",\"809\":\"HELLOWORLD\",\"816\":\"HELLOWORLD\",\"309\":\"HELLOWORLD\",\"310\":\"HELLOWORLD\",\"823\":\"HELLOWORLD\",\"312\":\"HELLOWORLD\",\"313\":\"HELLOWORLD\",\"314\":\"HELLOWORLD\",\"315\":\"HELLOWORLD\",\"316\":\"HELLOWORLD\",\"317\":\"HELLOWORLD\",\"318\":\"HELLOWORLD\",\"319\":\"HELLOWORLD\",\"320\":\"HELLOWORLD\",\"321\":\"HELLOWORLD\",\"322\":\"HELLOWORLD\",\"323\":\"HELLOWORLD\",\"324\":\"HELLOWORLD\",\"836\":\"HELLOWORLD\",\"325\":\"HELLOWORLD\",\"326\":\"HELLOWORLD\",\"327\":\"HELLOWORLD\",\"328\":\"HELLOWORLD\",\"333\":\"HELLOWORLD\",\"334\":\"HELLOWORLD\",\"846\":\"HELLOWORLD\",\"853\":\"HELLOWORLD\",\"346\":\"HELLOWORLD\",\"348\":\"HELLOWORLD\",\"860\":\"HELLOWORLD\",\"349\":\"HELLOWORLD\",\"351\":\"HELLOWORLD\",\"353\":\"HELLOWORLD\",\"867\":\"HELLOWORLD\",\"356\":\"HELLOWORLD\",\"358\":\"HELLOWORLD\",\"359\":\"HELLOWORLD\",\"361\":\"HELLOWORLD\",\"363\":\"HELLOWORLD\",\"365\":\"HELLOWORLD\",\"366\":\"HELLOWORLD\",\"367\":\"HELLOWORLD\",\"369\":\"HELLOWORLD\",\"370\":\"HELLOWORLD\",\"371\":\"HELLOWORLD\",\"372\":\"HELLOWORLD\",\"373\":\"HELLOWORLD\",\"374\":\"HELLOWORLD\",\"375\":\"HELLOWORLD\",\"376\":\"HELLOWORLD\",\"377\":\"HELLOWORLD\",\"378\":\"HELLOWORLD\",\"379\":\"HELLOWORLD\",\"380\":\"HELLOWORLD\",\"381\":\"HELLOWORLD\",\"383\":\"HELLOWORLD\",\"895\":\"HELLOWORLD\",\"385\":\"HELLOWORLD\",\"386\":\"HELLOWORLD\",\"387\":\"HELLOWORLD\",\"388\":\"HELLOWORLD\",\"390\":\"HELLOWORLD\",\"392\":\"HELLOWORLD\",\"394\":\"HELLOWORLD\",\"396\":\"HELLOWORLD\",\"398\":\"HELLOWORLD\",\"400\":\"HELLOWORLD\",\"402\":\"HELLOWORLD\",\"915\":\"HELLOWORLD\",\"404\":\"HELLOWORLD\",\"406\":\"HELLOWORLD\",\"408\":\"HELLOWORLD\",\"409\":\"HELLOWORLD\",\"410\":\"HELLOWORLD\",\"411\":\"HELLOWORLD\",\"412\":\"HELLOWORLD\",\"413\":\"HELLOWORLD\",\"415\":\"HELLOWORLD\",\"416\":\"HELLOWORLD\",\"418\":\"HELLOWORLD\",\"419\":\"HELLOWORLD\",\"420\":\"HELLOWORLD\",\"422\":\"HELLOWORLD\",\"430\":\"HELLOWORLD\",\"433\":\"HELLOWORLD\",\"438\":\"HELLOWORLD\",\"951\":\"HELLOWORLD\",\"952\":\"HELLOWORLD\",\"441\":\"HELLOWORLD\",\"444\":\"HELLOWORLD\",\"445\":\"HELLOWORLD\",\"446\":\"HELLOWORLD\",\"448\":\"HELLOWORLD\",\"449\":\"HELLOWORLD\",\"452\":\"HELLOWORLD\",\"456\":\"HELLOWORLD\",\"459\":\"HELLOWORLD\",\"460\":\"HELLOWORLD\",\"461\":\"HELLOWORLD\",\"464\":\"HELLOWORLD\",\"467\":\"HELLOWORLD\",\"470\":\"HELLOWORLD\",\"473\":\"HELLOWORLD\",\"476\":\"HELLOWORLD\",\"479\":\"HELLOWORLD\",\"482\":\"HELLOWORLD\",\"485\":\"HELLOWORLD\",\"488\":\"HELLOWORLD\",\"491\":\"HELLOWORLD\",\"492\":\"HELLOWORLD\",\"493\":\"HELLOWORLD\",\"494\":\"HELLOWORLD\",\"497\":\"HELLOWORLD\",\"505\":\"HELLOWORLD\"}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Post to {@link RestInterface#terminateScenarioInstance(int, int)}
     * with an valid scenario instance id
     * the scenario should be terminated and the response is a 201.
     */
    @Test
    public void terminateScenarioInstance() {
        Response response = base.path("scenario/1/instance/47").request().put(Entity.json(""));
        assertEquals("The Response code of terminating an instances was not 200",
                200, response.getStatus());
    }

    /**
     * When you send a Post to {@link RestInterface#terminateScenarioInstance(int, int)}
     * with an invalid instance id
     * then the Response should be a 404 with an error message.
     */
    @Test
    public void terminateScenarioInstanceInvalidId() {
        Response response = base.path("scenario/1/instance/9999").request().put(Entity.json(""));
        assertEquals("The Response code of terminating an instances was not 400",
                400, response.getStatus());
        assertEquals("The Media type of terminating an instance was not JSON",
                MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
        assertThat("The content of the response was not as expected",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"The Scenario instance could not be found!\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }


    /**
     * When you send a Get to {@link  RestInterface#getScenarioInstances(UriInfo, int, String)}
     * with a valid scenario id and a filter
     * only instances with names containing this string will be returned.
     */
    @Test
    public void testGetScenarioInstancesWithFilter() {
        Response response = base.path("scenario/1/instance").queryParam("filter", "noInstanceLikeThis").request().get();
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[],\"labels\":{},\"links\":{}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Post to {@link RestInterface#startNewInstance(UriInfo, int)}
     * then the Response will be a 201 and a json object wit the new id will be returned.
     */
    @Test
    public void testStartNewInstanceWOName() {
        Response response = base.path("scenario/1/instance").request().post(null);
        assertEquals("The Response code of start new instances was not 201",
                201, response.getStatus());
        assertEquals("Start new isntance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"id\":966,\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/966\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Post to {@link RestInterface#startNewInstance(UriInfo, int)} 
     * then the Response will be a 201 and a json object wit the new id will be returned.
     */
    @Test
    public void testStartInvalidInstanceWOName() {
        Response response = base.path("scenario/9999/instance").request().post(null);
        assertEquals("The Response code of start new instances was not 400",
                400, response.getStatus());
        assertEquals("Start new isntance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The Scenario could not be found!\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Put to {@link RestInterface#startNewNamedInstance(UriInfo, int, RestInterface.NamedJaxBean)}
     * then the Response will be a 201 and a json object wit the new id will be returned.
     */
    @Test
    public void testStartNewInstanceWName() {
        RestInterface.NamedJaxBean newName = new RestInterface.NamedJaxBean();
        newName.name = "Dies ist ein Test";
        Response response = base.path("scenario/1/instance")
                .request().put(Entity.json(newName));
        assertEquals("The Response code of start new instances was not 201",
                201, response.getStatus());
        assertEquals("Start new instance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"id\":966,\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/966\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }


    /**
     * When you send a Put to {@link RestInterface#startNewNamedInstance(UriInfo, int, RestInterface.NamedJaxBean)}
     * then the Response will be a 201 and a json object wit the new id will be returned.
     */
    @Test
    public void testStartInvalidInstanceWName() {
        RestInterface.NamedJaxBean newName = new RestInterface.NamedJaxBean();
        newName.name = "Dies ist ein Test";
        Response response = base.path("scenario/9999/instance").request()
                .put(Entity.json(newName));
        assertEquals("The Response code of start new instances was not 400",
                400, response.getStatus());
        assertEquals("Start new isntance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The Scenario could not be found!\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Post to {@link RestInterface#getScenarioInstance(UriInfo, int, int)}
     * with a correct scenario id and a correct instance id
     * the respond will be a 200 with a JSONObject
     */
    @Test
    public void testGetScenarioInstanceReturnsJSON() {
        Response response = base.path("scenario/1/instance/72").request().get();
        assertEquals("The Response code of getScenarioInstance was not 200",
                200, response.getStatus());
        assertEquals("getScenarioInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"name\":\"HELLOWORLD\",\"id\":72,\"terminated\":false,\"scenario_id\":1,\"activities\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Post to {@link RestInterface#getScenarioInstance(UriInfo, int, int)}
     * with a wrong scenario id and a correct instance id
     * the respond will be a 200 with a redirected URI.
     */
    @Test
    public void testGetScenarioInstanceWithWrongScenarioRedirects() {
        Response response = base.path("scenario/9999/instance/72").request().get();
        assertEquals("The Response code of getScenarioInstance was not 200",
                200, response.getStatus());
        assertEquals("getScenarioInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"name\":\"HELLOWORLD\",\"id\":72,\"terminated\":false,\"scenario_id\":1,\"activities\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }



    /**
     * When you send a Post to {@link RestInterface#getScenarioInstance(UriInfo, int, int)}
     * with a wrong scenario id and a correct instance id
     * the respond will be a 404 with a redirected URI.
     */
    @Test
    public void testGetScenarioInstanceWithWrongInstanceThrowsError() {
        Response response = base.path("scenario/9999/instance/9999").request().get();
        assertEquals("The Response code of getScenarioInstance was not 404",
                404, response.getStatus());
        assertEquals("getScenarioInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"There is no instance with the id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getActivitiesOfInstance(UriInfo, int, int, String, String)}
     * with an wrong scenario ID the request should be redirected to the correct one.
     */
    @Test
    public void testGetActivitiesRedirects() {
        Response response = base.path("scenario/9999/instance/72/activity").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":{\"189\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/189\",\"id\":189,\"label\":\"Activity1Fragment2\",\"state\":\"ready\"},\"6686\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/6686\",\"id\":6686,\"label\":\"ActivityFragment4\",\"state\":\"ready\"},\"186\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/186\",\"id\":186,\"label\":\"Activity1Fragment1\",\"state\":\"ready\"}},\"ids\":[186,189,6686]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getActivitiesOfInstance(UriInfo, int, int, String, String)} 
     * with an wrong scenario instance ID
     * then a 404 with error message (inside JSON) should be returned.
     */
    @Test
    public void testGetActivitiesInvalidInstance() {
        Response response = base.path("scenario/1/instance/9999/activity").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 404",
                404, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"There is no instance with id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getActivitiesOfInstance(UriInfo, int, int, String, String)} 
     * with an correct parameters a state but no filter
     * then the request should return all activities with this state.
     */
    @Test
    public void testGetActivitiesWithState() {
        Response response = base.path("scenario/1/instance/72/activity")
                .queryParam("state", "ready").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":[{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/186\",\"id\":186,\"label\":\"Activity1Fragment1\",\"state\":\"ready\"},{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/189\",\"id\":189,\"label\":\"Activity1Fragment2\",\"state\":\"ready\"},{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/6686\",\"id\":6686,\"label\":\"ActivityFragment4\",\"state\":\"ready\"}],\"ids\":[186,189,6686]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getActivitiesOfInstance(UriInfo, int, int, String, String)} 
     * with an correct parameters, an invalid state but no filter
     * the request should return a 404 with error message
     */
    @Test
    public void testGetActivitiesWithInvalidState() {
        Response response = base.path("scenario/1/instance/72/activity")
                .queryParam("state", "enabled").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 404",
                404, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The state is not allowed enabled\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }



    /**
     * When you send a Get to {@link RestInterface#getActivitiesOfInstance(UriInfo, int, int, String, String)} 
     * with an correct parameters a state but no filter
     * then the request should return all activities with this state.
     */
    @Test
    public void testGetActivitiesWithStateTerminated() {
        Response response = base.path("scenario/1/instance/72/activity")
                .queryParam("state", "terminated").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":[],\"ids\":[]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getActivitiesOfInstance(UriInfo, int, int, String, String)} 
     * with an correct parameters a state and a filter
     * then the request should return all activities with the state who fulfill the filter condition.
     */
    @Test
    public void testGetActivitiesWithStateAndFilter() {
        Response response = base.path("scenario/1/instance/72/activity")
            .queryParam("state", "ready")
                .queryParam("filter", "2").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":[{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/189\",\"id\":189,\"label\":\"Activity1Fragment2\",\"state\":\"ready\"}],\"ids\":[189]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getActivitiesOfInstance(UriInfo, int, int, String, String)}
     * with an correct parameters, an invalid state but no filter
     * the request should return a 404 with error message
     */
    @Test
    public void testGetActivitiesWithInvalidStateFilter() {
        Response response = base.path("scenario/1/instance/72/activity")
                .queryParam("state", "enabled")
                .queryParam("filter", "1").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 404",
                404, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The state is not allowed enabled\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getActivitiesOfInstance(UriInfo, int, int, String, String)}
     * with correct instance and scenario
     * a 200 with json content will be returned.
     */
    @Test
    public void testGetActivitiesCorrect() {
        Response response = base.path("scenario/1/instance/72/activity").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":{\"189\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/189\",\"id\":189,\"label\":\"Activity1Fragment2\",\"state\":\"ready\"},\"6686\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/6686\",\"id\":6686,\"label\":\"ActivityFragment4\",\"state\":\"ready\"},\"186\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/186\",\"id\":186,\"label\":\"Activity1Fragment1\",\"state\":\"ready\"}},\"ids\":[186,189,6686]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getActivitiesOfInstance(UriInfo, int, int, String, String)} 
     * with a filter String
     * then only activities with a label like the filter String will be returned.
     */
    @Test
    public void testGetActivitiesWithFilter() {
        Response response = base.path("scenario/1/instance/72/activity").queryParam("filter", "2").request().get();
        assertEquals("The Response code of getActivitiesOfInstance was not 200",
                200, response.getStatus());
        assertEquals("GetActivitiesOfInstance returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"activities\":{\"189\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/activity/189\",\"id\":189,\"label\":\"Activity1Fragment2\",\"state\":\"ready\"}},\"ids\":[189]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * WHen you send a Get to {@link RestInterface#getDataObjects(UriInfo, int, int, String)} 
     * with a correct instance id and a wrong scenario ID
     * you will be redirected automatically.
     */
    @Test
    public void testGetDataObjectsRedirects() {
        Response response = base.path("scenario/9999/instance/72/dataobject").request().get();
        assertEquals("The Response code of getDataObjects was not 200",
                200, response.getStatus());
        assertEquals("getDataObjects returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[1,2],\"results\":{\"1\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/dataobject/1\",\"id\":1,\"label\":\"object1\",\"state\":\"init\"},\"2\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/72/dataobject/2\",\"id\":2,\"label\":\"object2\",\"state\":\"init\"}}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }

    /**
     * WHen you send a Get to {@link RestInterface#getDataObjects(UriInfo, int, int, String)} 
     * with an invalid instance
     * an 404 with error message will be returned
     */
    @Test
    public void testGetDataObjectsInvalid() {
        Response response = base.path("scenario/9999/instance/9999/dataobject").request().get();
        assertEquals("The Response code of getDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getDataObjects returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"There is no instance with the id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getDataObjects(UriInfo, int, int, String)} 
     * with an valid instance and scenario and no filter String
     * you will get a list of all DataObjects for this scenario.
     */
    @Test
    public void testGetDataObjectsWOFilter() {
        Response response = base.path("scenario/1/instance/62/dataobject").request().get();
        assertEquals("The Response code of getDataObjects was not 200",
                200, response.getStatus());
        assertEquals("getDataObjects returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[1,2],\"results\":{\"1\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/62/dataobject/1\",\"id\":1,\"label\":\"object1\",\"state\":\"init\"},\"2\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/62/dataobject/2\",\"id\":2,\"label\":\"object2\",\"state\":\"init\"}}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getDataObjects(UriInfo, int, int, String)} 
     * with an valid instance and scenario and an filter String
     * you will get a list of all DataObjects with labels like the filter String for this scenario.
     */
    @Test
    public void testGetDataObjectsWithFilter() {
        Response response = base.path("scenario/1/instance/62/dataobject")
                .queryParam("filter", "1").request().get();
        assertEquals("The Response code of getDataObjects was not 200",
                200, response.getStatus());
        assertEquals("getDataObjects returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[1],\"results\":{\"1\":{\"link\":\"http://localhost:9998/interface/v2/scenario/1/instance/62/dataobject/1\",\"id\":1,\"label\":\"object1\",\"state\":\"init\"}}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getDataObject(int, int, int)}
     * with a correct scenario instance id but a wrong scenario id
     * you will be redirected
     */
    @Test
    public void testGetDataObjectRedirects() {
        Response response = base.path("scenario/9999/instance/62/dataobject/1").request().get();
        assertEquals("The Response code of getDataObject was not 200",
                200, response.getStatus());
        assertEquals("getDataObject return a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"label\":\"object1\",\"set_id\":0,\"id\":1,\"state\":\"init\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER)
                        .when(Option.IGNORING_EXTRA_FIELDS));
    }

    /**
     * When you send a Get to {@link RestInterface#getDataObject(int, int, int)}
     * with correct instance and scenario id but a wrong dataobject id
     * you will get a 404 with an error message.
     */
    @Test
    public void testGetDataObjectInvalidDoId() {
        Response response = base.path("scenario/1/instance/62/dataobject/9999").request().get();
        assertEquals("The Response code of getDataObject was not 404",
                404, response.getStatus());
        assertEquals("getDataObject return a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"There is no dataobject with the id 9999 for the scenario instance 62\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getDataObject(int, int, int)}
     * with correct scenario id but an incorrect instance id
     * you will get a 404 with an error message
     */
    @Test
    public void testGetDataObjectInvalidInstanceId() {
        Response response = base.path("scenario/1/instance/9999/dataobject/1").request().get();
        assertEquals("The Response code of getDataObject was not 404",
                404, response.getStatus());
        assertEquals("getDataObject return a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"There is no instance with the id 9999\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getDataObject(int, int, int)}
     * with correct instance, scenario and dataobject id
     * you will get a 200 with an json object.
     */
    @Test
    public void testGetDataObject() {
        Response response = base.path("scenario/1/instance/62/dataobject/1").request().get();
        assertEquals("The Response code of getDataObject was not 200",
                200, response.getStatus());
        assertEquals("getDataObject return a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"label\":\"object1\",\"set_id\":0,\"id\":1,\"state\":\"init\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER)
                        .when(Option.IGNORING_EXTRA_FIELDS));
        ;
    }


    /**
     * When you send a Get to {@link RestInterface#getTerminationCondition(int)}
     * with an valid id
     * then a JSON with the termination condition will be returned
     */
    @Test
    public void testGetTerminationCondition() {
        Response response = base.path("scenario/105/terminationcondition").request().get();
        assertEquals("The Response code of getTermiantionCondition was not 200",
                200, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"conditions\":{\"1\":[{\"data_object\":\"A\",\"set_id\":1,\"state\":\"c\"}]},\"setIDs\":[1]}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getTerminationCondition(int)}
     * with an invalid id
     * then a 404 with an error message should be returned
     */
    @Test
    public void testInvalidGetTerminationCondition() {
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

    /**
     * When you send a Get to {@link RestInterface#getActivity(UriInfo, int, int, int)}
     * with valid arguments
     * then you should get a 200 response code and
     * a JSONObject with the id, label of the activity and a link to the outputSet and the inputSet.
     */
    @Test
    public void testGetActivity(){
        Response response = base.path("scenario/135/instance/808/activity/4517").request().get();
        assertEquals("The Response code of getActivity was not 200", 200, response.getStatus());
        assertEquals("Get Activity does not return a JSON", MediaType.APPLICATION_JSON,
                response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"id\":4517,\"label\":\"Reiseplanung beginnen\",\"outputSetLink\":\"http://localhost:9998/interface/v2/scenario/135/instance/808/activity/4517/output\",\"inputSetLink\":\"http://localhost:9998/interface/v2/scenario/135/instance/808/activity/4517/input\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#getInputDataObjects(UriInfo, int, int, int)}
     * with valid arguments
     * then you then you should get a 200 response code and a JSONObject with the id of the inputSet and
     * a link to get the dataObjectInstance with their dataAttributeInstance.
     */
    @Test
    public void testGetInputDataObjects(){
        Response response = base.path("scenario/135/instance/808/activity/4518/input").request().get();
        assertEquals("The response code of getInputDataObjects was not 200", 200, response.getStatus());
        assertEquals("GetInputDataObjects does not return a JSON", MediaType.APPLICATION_JSON,
                response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "[{\"id\":139,\"linkDataObject\":\"http://localhost:9998/interface/v2/scenario/135/instance/808/inputset/139\"}]",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_VALUES));
    }

    /**
     * When you send a Get to {@link RestInterface#getOutputDataObjects(UriInfo, int, int, int)}
     * with valid arguments
     * then you then you should get a 200 response code and a JSONObject with the id of the outputSet and
     * a link to get the dataObjectInstance with their dataAttributeInstance.
     */
    @Test
    public void testGetOutputDataObjects(){
        Response response = base.path("scenario/135/instance/808/activity/4518/output").request().get();
        assertEquals("The response code of getOutputDataObjects was not 200", 200, response.getStatus());
        assertEquals("GetOutputDataObjects does not return a JSON", MediaType.APPLICATION_JSON,
                response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "[{\"id\":140,\"linkDataObject\":\"http://localhost:9998/interface/v2/scenario/135/instance/808/outputset/140\"}]",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER).when(Option.COMPARING_ONLY_STRUCTURE));
    }
    /**
     * When you send a Get to {@link RestInterface#updateActivityState(int, int, int, String)}
     * with an invalid state
     * a bad request with an error message should be returned.
     */
    @Test
    public void testInvalidStateUpdateActivity() {
        Response response = base.path("scenario/1/instance/72/activity/105")
                .queryParam("state", "complete").request().post(Entity.json("[]"));
        assertEquals("The Response code of getTerminationCondition was not 400",
                400, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The state transition complete is unknown\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     *
     */
    @Test
    public void testInvalidStateUpdateActivity2() {
        Response response = base.path("scenario/1/instance/72/activity/105")
                .request().post(Entity.json("[]"));
        assertEquals("The Response code of getTerminationCondition was not 400",
                400, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"The state is not set\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     *
     * When you send a Get to {@link RestInterface#updateActivityState(int, int, int, String)}
     * with an valid state for an invalid activity.
     * a bad request with an error message should be returned.
     */
    @Test
    public void testInvalidActivityUpdateActivity() {
        Response response = base.path("scenario/1/instance/72/activity/105")
                .queryParam("state", "begin").request().post(Entity.json("[]"));
        assertEquals("The Response code of getTerminationCondition was not 400",
                400, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"error\":\"impossible to start activity with id 105\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     *
     * When you send a Get to {@link RestInterface#updateActivityState(int, int, int, String)}
     * with an valid state and valid activity
     * then a 201 will be returned with a message inside a JSON-Object.
     */
    //@Test
    public void testUpdateActivity() {
        Response response = base.path("scenario/1/instance/72/activity/186")
                .queryParam("state", "begin").request().post(Entity.json("[]"));
        assertEquals("The Response code of updateActivityState was not 202",
                202, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"activity state changed.\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
        response = base.path("scenario/1/instance/72/activity/186")
                .queryParam("state", "terminate").request().post(Entity.json("[]"));
        assertEquals("The Response code of updateActivityState was not 202",
                202, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"activity state changed.\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * When you send a Get to {@link RestInterface#updateActivityState(int, int, int, String)}
     * with an valid state and valid activity
     * then a 201 will be returned with a message inside a JSON-Object.
     */
    @Test
    public void testUpdateActivityWAttributes(){
        Response response = base.path("scenario/135/instance/808/activity/4518")
                .queryParam("state", "begin").request().post(Entity.json("[]"));
        assertEquals("The Response code of updateActivityState was not 202",
                202, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"activity state changed.\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
        response = base.path("scenario/135/instance/808/activity/4518")
                .request()
                .put(Entity.json("{\"id\":1,\"value\":\"Test\"}"));
        assertEquals("The Response code of updateActivityState was not 202",
                202, response.getStatus());
        response = base.path("scenario/135/instance/808/activity/4518")
                .queryParam("state", "terminate").request()
                .post(Entity.json("[{\"label\":\"Reiseplan\",\"id\":22,\"state\":\"init\",\"attributeConfiguration\":{\"entry\":[{\"key\":1,\"value\":\"{name=Preis, type=, value=400}\"}]}}]"));
        assertEquals("The Response code of getTerminationCondition was not 202",
                202, response.getStatus());
        assertEquals("Get TerminationCondition does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"message\":\"activity state changed.\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given is the Rest API
     * When you send a POST to {@link RestInterface#terminateScenarioInstance(int, int)}
     * with an invalid scenario id or instance id
     * then a 400 will be returned with an error message
     */
    @Test
    public void testTerminateInvalidScenarioInstance() {
        Response response = base.path("scenario/9999/instance/72")
                .queryParam("state", "begin").request().put(Entity.json("{}"));
        assertEquals("The Response code of terminateScenarioInstance was not 400",
                400, response.getStatus());
        assertEquals("Get terminateScenarioInstance does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"The Scenario instance could not be found!\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
        response = base.path("scenario/1/instance/9999")
                .queryParam("status", "begin").request().put(Entity.json("{}"));
        assertEquals("The Response code of terminateScenarioInstance was not 400",
                400, response.getStatus());
        assertEquals("Get terminateScenarioInstance does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"The Scenario instance could not be found!\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given is the Rest API
     * When you send a POST to {@link RestInterface#terminateScenarioInstance(int, int)}
     * with an valid scenario and instance id
     * the instance will be terminated.
     */
    @Test
    public void testTerminateScenarioInstance() {
        Response response = base.path("scenario/1/instance/72")
                .queryParam("state", "begin").request().put(Entity.json("{}"));
        assertEquals("The Response code of terminateScenarioInstance was not 200",
                200, response.getStatus());
        assertEquals("terminateScenarioInstance does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"message\":\"The is instance has been terminated.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given: Is an invalid scenario instance
     * when you send a get to {@link RestInterface#getInputDataObjects(UriInfo, int, int, int)}
     * with an invalid scenario and instance id
     * a 404 with an error message is returned
     */
    @Test
    public void testGetInputForInvalidScenario() {
        Response response = base.path("scenario/9987/instance/1234/activity/1/input")
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
     * Given: Is an invalid activity instance
     * when you send a get to {@link RestInterface#getInputDataObjects(UriInfo, int, int, int)}
     * with an invalid activity instance id
     * a 404 with an error message is returned
     */
    @Test
    public void testGetInputForInvalidActivity() {
        Response response = base.path("scenario/1/instance/72/activity/9999/input")
                .request().get();
        assertEquals("The Response code of getInputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getInputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no such activity instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given: Is an valid activity instance
     * when you send a get to {@link RestInterface#getInputDataObjects(UriInfo, int, int, int)}
     * with a valid activity instance without input sets
     * a 404 with an error message will be returned
     */
    @Test
    public void testGetInputForWOInputSets() {
        Response response = base.path("scenario/135/instance/808/activity/4517/input")
                .request().get();
        assertEquals("The Response code of getInputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getInputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no inputSet for this activity instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given: Is an invalid scenario instance
     * when you send a get to {@link RestInterface#getOutputDataObjects(UriInfo, int, int, int)}
     * with an invalid scenario and instance id
     * a 404 with an error message is returned
     */
    @Test
    public void testGetOutputForInvalidScenario() {
        Response response = base.path("scenario/0/instance/0/activity/1/output")
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
     * Given: Is an invalid activity instance
     * when you send a get to {@link RestInterface#getOutputDataObjects(UriInfo, int, int, int)}
     * with an invalid activity instance id
     * a 404 with an error message is returned
     */
    @Test
    public void testGetOutputInvalidActivity() {
        Response response = base.path("scenario/1/instance/72/activity/9999/output")
                .request().get();
        assertEquals("The Response code of getOutputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getOutputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no such activity instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     * Given: Is an valid activity instance
     * when you send a get to {@link RestInterface#getOutputDataObjects(UriInfo, int, int, int)}
     * with a valid activity instance without input sets
     * a 404 with an error message will be returned
     */
    @Test
    public void testGetOutputForWOOutputSets() {
        Response response = base.path("scenario/118/instance/704/activity/3749/output")
                .request().get();
        assertEquals("The Response code of getOutputDataObjects was not 404",
                404, response.getStatus());
        assertEquals("getOutputDataObjects does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("{\"error\":\"There is no outputSet for this activity instance.\"}")
                        .when(Option.IGNORING_ARRAY_ORDER));
    }
    /**
     *  when you send a get to {@link RestInterface#getOutputDataObjectsAndAttributes(int, int, int)}
     *  with valid arguments
     *  a 200 is returned with a JSONArray with JSONObjects for each dataObjectInstance
     */
    @Test
    public void testGetOutputSetDataAttributes() {
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
    }
    /**
     * when you send a get to {@link RestInterface#getOutputDataObjectsAndAttributes(int, int, int)}
     * with an invalid scenario/Instance
     * a 404 with an error message is returned
     */
    @Test
    public void testInvalidScenarioGetOutputSetDataAttributes() {
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
     * when you send a get to {@link RestInterface#getOutputDataObjectsAndAttributes(int, int, int)}
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
     *  when you send a get to {@link RestInterface#getInputDataObjectsAndAttributes(int, int, int)}
     *  with valid arguments
     *  a 200 is returned with a JSONArray with JSONObjects for each dataObjectInstance
     */
    @Test
    public void testGetInputSetDataAttributes() {
        Response response = base.path("scenario/135/instance/808/inputset/139")
                .request().get();
        assertEquals("The Response code of getInputDataAttributes was not 200",
                200, response.getStatus());
        assertEquals("getDataAttributes does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                response.readEntity(String.class),
                jsonEquals("[{\"label\":\"Reiseplan\",\"id\":675,\"state\":\"init\",\"attributeConfiguration\":[{\"id\":1,\"name\":\"Preis\",\"type\":\"\",\"value\":\"250\"}]}]")
                        .when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }
    /**
     * when you send a get to {@link RestInterface#getInputDataObjectsAndAttributes(int, int, int)}
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
     * when you send a get to {@link RestInterface#getInputDataObjectsAndAttributes(int, int, int)}
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
}