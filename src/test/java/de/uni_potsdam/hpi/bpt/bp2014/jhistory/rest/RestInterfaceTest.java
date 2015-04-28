package de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        Response response = base.path("scenario/1/instance/966/activities").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"93\":{\"h.activityinstance_id\":6685,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready(ControlFlow)\",\"cn.label\":\"Activity1Fragment1\",\"h.newstate\":\"ready\"},\"92\":{\"h.activityinstance_id\":6685,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"init\",\"cn.label\":\"Activity1Fragment1\",\"h.newstate\":\"ready(ControlFlow)\"},\"95\":{\"h.activityinstance_id\":6686,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"init\",\"cn.label\":\"Activity1Fragment2\",\"h.newstate\":\"ready(ControlFlow)\"},\"94\":{\"h.activityinstance_id\":6686,\"h.scenarioinstance_id\":966,\"cn.label\":\"Activity1Fragment2\",\"h.newstate\":\"init\"},\"91\":{\"h.activityinstance_id\":6685,\"h.scenarioinstance_id\":966,\"cn.label\":\"Activity1Fragment1\",\"h.newstate\":\"init\"},\"102\":{\"h.activityinstance_id\":6685,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"running\",\"cn.label\":\"Activity1Fragment1\",\"h.newstate\":\"terminated\"},\"103\":{\"h.activityinstance_id\":6686,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready(ControlFlow)\",\"cn.label\":\"Activity1Fragment2\",\"h.newstate\":\"ready\"},\"100\":{\"h.activityinstance_id\":6688,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready(ControlFlow)\",\"cn.label\":\"ActivityFragment4\",\"h.newstate\":\"ready\"},\"101\":{\"h.activityinstance_id\":6685,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready\",\"cn.label\":\"Activity1Fragment1\",\"h.newstate\":\"running\"},\"98\":{\"h.activityinstance_id\":6688,\"h.scenarioinstance_id\":966,\"cn.label\":\"ActivityFragment4\",\"h.newstate\":\"init\"},\"99\":{\"h.activityinstance_id\":6688,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"init\",\"cn.label\":\"ActivityFragment4\",\"h.newstate\":\"ready(ControlFlow)\"},\"96\":{\"h.activityinstance_id\":6687,\"h.scenarioinstance_id\":966,\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"init\"},\"97\":{\"h.activityinstance_id\":6687,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"init\",\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"ready(ControlFlow)\"},\"110\":{\"h.activityinstance_id\":6690,\"h.scenarioinstance_id\":966,\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"init\"},\"111\":{\"h.activityinstance_id\":6690,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"init\",\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"ready(ControlFlow)\"},\"108\":{\"h.activityinstance_id\":6689,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"running\",\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"terminated\"},\"109\":{\"h.activityinstance_id\":6687,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready(ControlFlow)\",\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"ready\"},\"106\":{\"h.activityinstance_id\":6689,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready(ControlFlow)\",\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"ready\"},\"107\":{\"h.activityinstance_id\":6689,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready\",\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"running\"},\"104\":{\"h.activityinstance_id\":6689,\"h.scenarioinstance_id\":966,\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"init\"},\"105\":{\"h.activityinstance_id\":6689,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"init\",\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"ready(ControlFlow)\"},\"119\":{\"h.activityinstance_id\":6686,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"running\",\"cn.label\":\"Activity1Fragment2\",\"h.newstate\":\"terminated\"},\"118\":{\"h.activityinstance_id\":6691,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready\",\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"ready(ControlFlow)\"},\"117\":{\"h.activityinstance_id\":6686,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready\",\"cn.label\":\"Activity1Fragment2\",\"h.newstate\":\"running\"},\"116\":{\"h.activityinstance_id\":6691,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready(ControlFlow)\",\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"ready\"},\"115\":{\"h.activityinstance_id\":6691,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"init\",\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"ready(ControlFlow)\"},\"114\":{\"h.activityinstance_id\":6691,\"h.scenarioinstance_id\":966,\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"init\"},\"113\":{\"h.activityinstance_id\":6687,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"running\",\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"terminated\"},\"112\":{\"h.activityinstance_id\":6687,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready\",\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"running\"},\"127\":{\"h.activityinstance_id\":6693,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready(ControlFlow)\",\"cn.label\":\"ActivityFragment4\",\"h.newstate\":\"ready\"},\"126\":{\"h.activityinstance_id\":6693,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"init\",\"cn.label\":\"ActivityFragment4\",\"h.newstate\":\"ready(ControlFlow)\"},\"125\":{\"h.activityinstance_id\":6693,\"h.scenarioinstance_id\":966,\"cn.label\":\"ActivityFragment4\",\"h.newstate\":\"init\"},\"124\":{\"h.activityinstance_id\":6688,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"running\",\"cn.label\":\"ActivityFragment4\",\"h.newstate\":\"terminated\"},\"123\":{\"h.activityinstance_id\":6688,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready\",\"cn.label\":\"ActivityFragment4\",\"h.newstate\":\"running\"},\"122\":{\"h.activityinstance_id\":6692,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"init\",\"cn.label\":\"Activity1Fragment2\",\"h.newstate\":\"ready(ControlFlow)\"},\"121\":{\"h.activityinstance_id\":6692,\"h.scenarioinstance_id\":966,\"cn.label\":\"Activity1Fragment2\",\"h.newstate\":\"init\"},\"120\":{\"h.activityinstance_id\":6690,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"ready(ControlFlow)\",\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"ready\"}}\n").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }

    /**
     *
     */
    @Test
    public void testGetActivitiesLogWithState() {
        Response response = base.path("scenario/1/instance/966/activities").queryParam("state", "terminated").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"119\":{\"h.activityinstance_id\":6686,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"running\",\"cn.label\":\"Activity1Fragment2\",\"h.newstate\":\"terminated\"},\"102\":{\"h.activityinstance_id\":6685,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"running\",\"cn.label\":\"Activity1Fragment1\",\"h.newstate\":\"terminated\"},\"113\":{\"h.activityinstance_id\":6687,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"running\",\"cn.label\":\"ActivityFragment3\",\"h.newstate\":\"terminated\"},\"108\":{\"h.activityinstance_id\":6689,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"running\",\"cn.label\":\"Activity2Fragment1\",\"h.newstate\":\"terminated\"},\"124\":{\"h.activityinstance_id\":6688,\"h.scenarioinstance_id\":966,\"h.oldstate\":\"running\",\"cn.label\":\"ActivityFragment4\",\"h.newstate\":\"terminated\"}}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }
    
    /**
     *
     */
    @Test
    public void testGetDataObjectsLog() {
        Response response = base.path("scenario/1/instance/966/dataobjects").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"69\":{\"oldstate_name\":\"\",\"h.scenarioinstance_id\":966,\"h.newstate_id\":1,\"newstate_name\":\"init\",\"do.name\":\"object1\",\"h.dataobjectinstance_id\":744},\"70\":{\"oldstate_name\":\"\",\"h.scenarioinstance_id\":966,\"h.newstate_id\":5,\"newstate_name\":\"init\",\"do.name\":\"object2\",\"h.dataobjectinstance_id\":745},\"71\":{\"oldstate_name\":\"init\",\"h.scenarioinstance_id\":966,\"h.newstate_id\":2,\"newstate_name\":\"bearbeitet\",\"do.name\":\"object1\",\"h.old_state_id\":1,\"h.dataobjectinstance_id\":744},\"72\":{\"oldstate_name\":\"init\",\"h.scenarioinstance_id\":966,\"h.newstate_id\":6,\"newstate_name\":\"fertig\",\"do.name\":\"object2\",\"h.old_state_id\":5,\"h.dataobjectinstance_id\":745},\"73\":{\"oldstate_name\":\"bearbeitet\",\"h.scenarioinstance_id\":966,\"h.newstate_id\":3,\"newstate_name\":\"gepr?ft\",\"do.name\":\"object1\",\"h.old_state_id\":2,\"h.dataobjectinstance_id\":744}}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }
}
