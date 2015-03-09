package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RestConnection2Test extends AbstractTest {

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2.sql";

    static {
        TEST_SQL_SEED_FILE = "src/main/resources/JEngineV2RESTTest.sql";
    }

    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jcore.RestConnection.class);

    }

    /* #############################################################################
     *
     * HTTP GET REQUEST
     *
     * #############################################################################
     */

    @Test
    public void testGetAllScenarios() {
        final Response test = target("/interface/v1/en/scenario/0/").request().get();
        assertEquals("{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134]}", test.readEntity(String.class));
    }

    @Test
    public void testGetScenarioDetails() {
        final Response test = target("/interface/v1/en/scenario/1/").request().get();
        assertEquals("{\"label\":\"HELLOWORLD\"}", test.readEntity(String.class));
    }

    @Test
    public void testGetScenarioInstances() {
        final Response test = target("/interface/v1/en/scenario/1/instance/0/").request().get();
        assertEquals("{\"ids\":[47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,92,94,95,97,99,101,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,212,214,215,216,217,218,219,220,221,222,223,224,226,228,244,246,248,250,252,255,257,259,261,262,263,265,266,270,279,281,282,284,285,286,294,296,309,310,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,333,334,346,348,349,351,353,356,358,359,361,363,365,366,367,369,370,371,372,373,374,375,376,377,378,379,380,381,383,385,386,387,388,390,392,394,396,398,400,402,404,406,408,409,410,411,412,413,415,416,417,418,419,420,421,422,430,433,438,441,444,445,446,448,449,452,456,459,460,461,464,467,470,473,476,479,482,485,488,491,492,493,494,497,505,515,519,527,531,552,561,563,572,582,590,598,605,612,614,621,628,635,642,649,656,663,670,677,684,691,698,705,712,719,726,733,740,747,754,756,763,770,777,784,791]}", test.readEntity(String.class));
    }

    @Test
    public void testGetScenarioInstanceDetailFail() {
        final Response test = target("/interface/v1/en/scenario/1/instance/1/").request().get();
        assertEquals("Error: not correct instanceID", test.readEntity(String.class));
    }

    @Test
    public void testGetScenarioInstanceDetail() {
        final Response test = target("/interface/v1/en/scenario/1/instance/47/").request().get();
        assertEquals("{\"label\":\"HELLOWORLD\"}", test.readEntity(String.class));
    }

    @Test
    public void testGetAllActivitiesForScenarioInstance() {
        final Response test = target("/interface/v1/en/scenario/1/instance/47/activityinstance/0/").request().get();
        assertEquals("{\"ids\":[5,4,16],\"label\":{\"16\":\"ActivityFragment4\",\"4\":\"Activity1Fragment2\",\"5\":\"Activity2Fragment1\"}}", test.readEntity(String.class));
    }

    @Test
    public void testGetAllActivityDetailsForScenarioInstance() {
        final Response test = target("/interface/v1/en/scenario/1/instance/47/activityinstance/16/").request().get();
        assertEquals("{\"label\":\"ActivityFragment4\"}", test.readEntity(String.class));
    }

    @Test
    public void testGetAllDataobjectForScenarioInstance() {
        final Response test = target("/interface/v1/en/scenario/1/instance/328/dataobject/0/").request().get();
        assertEquals("{\"states\":{\"1\":\"bearbeitet\",\"2\":\"init\"},\"ids\":[1,2],\"label\":{\"1\":\"object1\",\"2\":\"object2\"}}", test.readEntity(String.class));
    }

    @Test
    public void testUpdateEmailConfiguration() {

        Map object = new HashMap();
        object.put("receiver", "test@test.de");
        object.put("subject", "TestSubject");
        object.put("Content", "Sehr geehrte Damen und Herren,");

        Entity userEntity = Entity.entity(new JSONObject(object), MediaType.APPLICATION_JSON);

        final Response test = target("/interface/v1/en/scenario/1/instance/328/emailtask/1337/").request().post(userEntity);
        //assertEquals("true", test.readEntity(String.class));
    }


    /* #############################################################################
     *
     * HTTP POST REQUEST
     *
     * #############################################################################
     */

    @Test
    public void testPostActivityStatusUpdate() {
        //final Response test = target("/interface/v1/en/scenario/1/instance/0/activityinstance/319/?status=enabled").request().post();
        //assertEquals("true", test.readEntity(String.class));
    }

    @Test
    public void testPostNewInstanceForScenario() {
        //  final Response test = target("/interface/v1/en/scenario/1/").request().post();
        //assertEquals("123", test.readEntity(String.class));
    }
}
