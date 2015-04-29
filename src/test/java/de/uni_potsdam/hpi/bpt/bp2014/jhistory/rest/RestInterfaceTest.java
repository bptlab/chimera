package de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertThat;

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

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2.sql";
    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineJHistoryRestTest.sql";
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
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest.RestInterface.class);
    }

    @Before
    public void setUpBase() {
        base = target("history/v2/");
    }

    /**
     *
     */
    @Test
    public void testGetActivitiesLog() {
        Response response = base.path("scenario/1/instance/1302/activities").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"1\":{\"h.scenarioinstance_id\":1302,\"h.id\":1,\"h.activityinstance_id\":9261,\"cn.label\":\"Activity1Fragment1\",\"h.newstate\":\"init\"},\"2\":{\"h.scenarioinstance_id\":1302,\"h.id\":2,\"h.activityinstance_id\":9261,\"cn.label\":\"Activity1Fragment1\",\"h.oldstate\":\"init\",\"h.newstate\":\"ready(ControlFlow)\"},\"3\":{\"h.scenarioinstance_id\":1302,\"h.id\":3,\"h.activityinstance_id\":9261,\"cn.label\":\"Activity1Fragment1\",\"h.oldstate\":\"ready(ControlFlow)\",\"h.newstate\":\"ready\"},\"4\":{\"h.scenarioinstance_id\":1302,\"h.id\":4,\"h.activityinstance_id\":9262,\"cn.label\":\"Activity1Fragment2\",\"h.newstate\":\"init\"},\"5\":{\"h.scenarioinstance_id\":1302,\"h.id\":5,\"h.activityinstance_id\":9262,\"cn.label\":\"Activity1Fragment2\",\"h.oldstate\":\"init\",\"h.newstate\":\"ready(ControlFlow)\"},\"6\":{\"h.scenarioinstance_id\":1302,\"h.id\":6,\"h.activityinstance_id\":9263,\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"init\"},\"7\":{\"h.scenarioinstance_id\":1302,\"h.id\":7,\"h.activityinstance_id\":9263,\"cn.label\":\"ActivityFragment3\",\"h.oldstate\":\"init\",\"h.newstate\":\"ready(ControlFlow)\"},\"8\":{\"h.scenarioinstance_id\":1302,\"h.id\":8,\"h.activityinstance_id\":9264,\"cn.label\":\"ActivityFragment4\",\"h.newstate\":\"init\"},\"9\":{\"h.scenarioinstance_id\":1302,\"h.id\":9,\"h.activityinstance_id\":9264,\"cn.label\":\"ActivityFragment4\",\"h.oldstate\":\"init\",\"h.newstate\":\"ready(ControlFlow)\"},\"10\":{\"h.scenarioinstance_id\":1302,\"h.id\":10,\"h.activityinstance_id\":9264,\"cn.label\":\"ActivityFragment4\",\"h.oldstate\":\"ready(ControlFlow)\",\"h.newstate\":\"ready\"},\"11\":{\"h.scenarioinstance_id\":1302,\"h.id\":11,\"h.activityinstance_id\":9261,\"cn.label\":\"Activity1Fragment1\",\"h.oldstate\":\"ready\",\"h.newstate\":\"running\"},\"12\":{\"h.scenarioinstance_id\":1302,\"h.id\":12,\"h.activityinstance_id\":9261,\"cn.label\":\"Activity1Fragment1\",\"h.oldstate\":\"running\",\"h.newstate\":\"terminated\"},\"13\":{\"h.scenarioinstance_id\":1302,\"h.id\":13,\"h.activityinstance_id\":9262,\"cn.label\":\"Activity1Fragment2\",\"h.oldstate\":\"ready(ControlFlow)\",\"h.newstate\":\"ready\"},\"14\":{\"h.scenarioinstance_id\":1302,\"h.id\":14,\"h.activityinstance_id\":9265,\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"init\"},\"15\":{\"h.scenarioinstance_id\":1302,\"h.id\":15,\"h.activityinstance_id\":9265,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"init\",\"h.newstate\":\"ready(ControlFlow)\"},\"17\":{\"h.scenarioinstance_id\":1302,\"h.id\":17,\"h.activityinstance_id\":9265,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"ready\",\"h.newstate\":\"running\"},\"16\":{\"h.scenarioinstance_id\":1302,\"h.id\":16,\"h.activityinstance_id\":9265,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"ready(ControlFlow)\",\"h.newstate\":\"ready\"},\"19\":{\"h.scenarioinstance_id\":1302,\"h.id\":19,\"h.activityinstance_id\":9263,\"cn.label\":\"ActivityFragment3\",\"h.oldstate\":\"ready(ControlFlow)\",\"h.newstate\":\"ready\"},\"18\":{\"h.scenarioinstance_id\":1302,\"h.id\":18,\"h.activityinstance_id\":9265,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"running\",\"h.newstate\":\"terminated\"},\"21\":{\"h.scenarioinstance_id\":1302,\"h.id\":21,\"h.activityinstance_id\":9266,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"init\",\"h.newstate\":\"ready(ControlFlow)\"},\"20\":{\"h.scenarioinstance_id\":1302,\"h.id\":20,\"h.activityinstance_id\":9266,\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"init\"},\"23\":{\"h.scenarioinstance_id\":1302,\"h.id\":23,\"h.activityinstance_id\":9263,\"cn.label\":\"ActivityFragment3\",\"h.oldstate\":\"ready\",\"h.newstate\":\"ready(ControlFlow)\"},\"22\":{\"h.scenarioinstance_id\":1302,\"h.id\":22,\"h.activityinstance_id\":9262,\"cn.label\":\"Activity1Fragment2\",\"h.oldstate\":\"ready\",\"h.newstate\":\"running\"},\"25\":{\"h.scenarioinstance_id\":1302,\"h.id\":25,\"h.activityinstance_id\":9266,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"ready(ControlFlow)\",\"h.newstate\":\"ready\"},\"24\":{\"h.scenarioinstance_id\":1302,\"h.id\":24,\"h.activityinstance_id\":9262,\"cn.label\":\"Activity1Fragment2\",\"h.oldstate\":\"running\",\"h.newstate\":\"terminated\"},\"27\":{\"h.scenarioinstance_id\":1302,\"h.id\":27,\"h.activityinstance_id\":9267,\"cn.label\":\"Activity1Fragment2\",\"h.oldstate\":\"init\",\"h.newstate\":\"ready(ControlFlow)\"},\"26\":{\"h.scenarioinstance_id\":1302,\"h.id\":26,\"h.activityinstance_id\":9267,\"cn.label\":\"Activity1Fragment2\",\"h.newstate\":\"init\"},\"29\":{\"h.scenarioinstance_id\":1302,\"h.id\":29,\"h.activityinstance_id\":9266,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"running\",\"h.newstate\":\"terminated\"},\"28\":{\"h.scenarioinstance_id\":1302,\"h.id\":28,\"h.activityinstance_id\":9266,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"ready\",\"h.newstate\":\"running\"},\"31\":{\"h.scenarioinstance_id\":1302,\"h.id\":31,\"h.activityinstance_id\":9268,\"cn.label\":\"Activity1Fragment1\",\"h.oldstate\":\"init\",\"h.newstate\":\"ready(ControlFlow)\"},\"30\":{\"h.scenarioinstance_id\":1302,\"h.id\":30,\"h.activityinstance_id\":9268,\"cn.label\":\"Activity1Fragment1\",\"h.newstate\":\"init\"}}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }

    /**
     *
     */
    @Test
    public void testGetActivitiesLogWithState() {
        Response response = base.path("scenario/1/instance/1302/activities").queryParam("state", "terminated").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"18\":{\"h.scenarioinstance_id\":1302,\"h.id\":18,\"h.activityinstance_id\":9265,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"running\",\"h.newstate\":\"terminated\"},\"24\":{\"h.scenarioinstance_id\":1302,\"h.id\":24,\"h.activityinstance_id\":9262,\"cn.label\":\"Activity1Fragment2\",\"h.oldstate\":\"running\",\"h.newstate\":\"terminated\"},\"29\":{\"h.scenarioinstance_id\":1302,\"h.id\":29,\"h.activityinstance_id\":9266,\"cn.label\":\"Activity2Fragment1\",\"h.oldstate\":\"running\",\"h.newstate\":\"terminated\"},\"12\":{\"h.scenarioinstance_id\":1302,\"h.id\":12,\"h.activityinstance_id\":9261,\"cn.label\":\"Activity1Fragment1\",\"h.oldstate\":\"running\",\"h.newstate\":\"terminated\"}}'.\n").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }
    
    /**
     *
     */
    //@Test
    public void testGetDataObjectsLog() {
        Response response = base.path("scenario/1/instance/1302/dataobjects").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"3\":{\"h.scenarioinstance_id\":1302,\"h.id\":3,\"h.oldstate_id\":1,\"h.newstate_id\":2,\"h.dataobjectinstance_id\":1058,\"newstate_name\":\"bearbeitet\",\"oldstate_name\":\"init\",\"do.name\":\"Wischeimer\"},\"4\":{\"h.scenarioinstance_id\":1302,\"h.id\":4,\"h.oldstate_id\":5,\"h.newstate_id\":6,\"h.dataobjectinstance_id\":1059,\"newstate_name\":\"fertig\",\"oldstate_name\":\"init\",\"do.name\":\"Wischeimer\"},\"5\":{\"h.scenarioinstance_id\":1302,\"h.id\":5,\"h.oldstate_id\":2,\"h.newstate_id\":3,\"h.dataobjectinstance_id\":1058,\"newstate_name\":\"gepr?ft\",\"oldstate_name\":\"bearbeitet\",\"do.name\":\"Wischeimer\"},\"6\":{\"h.scenarioinstance_id\":1302,\"h.id\":6,\"h.oldstate_id\":3,\"h.newstate_id\":4,\"h.dataobjectinstance_id\":1058,\"newstate_name\":\"abgeschlossen\",\"oldstate_name\":\"gepr?ft\",\"do.name\":\"Wischeimer\"}}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }
}
