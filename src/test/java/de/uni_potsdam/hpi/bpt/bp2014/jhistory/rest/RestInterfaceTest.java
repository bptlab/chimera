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
                jsonEquals("{\"93\":{\"activityinstance_id\":6685,\"scenarioinstance_id\":966,\"oldstate\":\"ready(ControlFlow)\",\"label\":\"Activity1Fragment1\",\"newstate\":\"ready\"},\"92\":{\"activityinstance_id\":6685,\"scenarioinstance_id\":966,\"oldstate\":\"init\",\"label\":\"Activity1Fragment1\",\"newstate\":\"ready(ControlFlow)\"},\"95\":{\"activityinstance_id\":6686,\"scenarioinstance_id\":966,\"oldstate\":\"init\",\"label\":\"Activity1Fragment2\",\"newstate\":\"ready(ControlFlow)\"},\"94\":{\"activityinstance_id\":6686,\"scenarioinstance_id\":966,\"label\":\"Activity1Fragment2\",\"newstate\":\"init\"},\"91\":{\"activityinstance_id\":6685,\"scenarioinstance_id\":966,\"label\":\"Activity1Fragment1\",\"newstate\":\"init\"},\"102\":{\"activityinstance_id\":6685,\"scenarioinstance_id\":966,\"oldstate\":\"running\",\"label\":\"Activity1Fragment1\",\"newstate\":\"terminated\"},\"103\":{\"activityinstance_id\":6686,\"scenarioinstance_id\":966,\"oldstate\":\"ready(ControlFlow)\",\"label\":\"Activity1Fragment2\",\"newstate\":\"ready\"},\"100\":{\"activityinstance_id\":6688,\"scenarioinstance_id\":966,\"oldstate\":\"ready(ControlFlow)\",\"label\":\"ActivityFragment4\",\"newstate\":\"ready\"},\"101\":{\"activityinstance_id\":6685,\"scenarioinstance_id\":966,\"oldstate\":\"ready\",\"label\":\"Activity1Fragment1\",\"newstate\":\"running\"},\"98\":{\"activityinstance_id\":6688,\"scenarioinstance_id\":966,\"label\":\"ActivityFragment4\",\"newstate\":\"init\"},\"99\":{\"activityinstance_id\":6688,\"scenarioinstance_id\":966,\"oldstate\":\"init\",\"label\":\"ActivityFragment4\",\"newstate\":\"ready(ControlFlow)\"},\"96\":{\"activityinstance_id\":6687,\"scenarioinstance_id\":966,\"label\":\"ActivityFragment3\",\"newstate\":\"init\"},\"97\":{\"activityinstance_id\":6687,\"scenarioinstance_id\":966,\"oldstate\":\"init\",\"label\":\"ActivityFragment3\",\"newstate\":\"ready(ControlFlow)\"},\"110\":{\"activityinstance_id\":6690,\"scenarioinstance_id\":966,\"label\":\"Activity2Fragment1\",\"newstate\":\"init\"},\"111\":{\"activityinstance_id\":6690,\"scenarioinstance_id\":966,\"oldstate\":\"init\",\"label\":\"Activity2Fragment1\",\"newstate\":\"ready(ControlFlow)\"},\"108\":{\"activityinstance_id\":6689,\"scenarioinstance_id\":966,\"oldstate\":\"running\",\"label\":\"Activity2Fragment1\",\"newstate\":\"terminated\"},\"109\":{\"activityinstance_id\":6687,\"scenarioinstance_id\":966,\"oldstate\":\"ready(ControlFlow)\",\"label\":\"ActivityFragment3\",\"newstate\":\"ready\"},\"106\":{\"activityinstance_id\":6689,\"scenarioinstance_id\":966,\"oldstate\":\"ready(ControlFlow)\",\"label\":\"Activity2Fragment1\",\"newstate\":\"ready\"},\"107\":{\"activityinstance_id\":6689,\"scenarioinstance_id\":966,\"oldstate\":\"ready\",\"label\":\"Activity2Fragment1\",\"newstate\":\"running\"},\"104\":{\"activityinstance_id\":6689,\"scenarioinstance_id\":966,\"label\":\"Activity2Fragment1\",\"newstate\":\"init\"},\"105\":{\"activityinstance_id\":6689,\"scenarioinstance_id\":966,\"oldstate\":\"init\",\"label\":\"Activity2Fragment1\",\"newstate\":\"ready(ControlFlow)\"},\"119\":{\"activityinstance_id\":6686,\"scenarioinstance_id\":966,\"oldstate\":\"running\",\"label\":\"Activity1Fragment2\",\"newstate\":\"terminated\"},\"118\":{\"activityinstance_id\":6691,\"scenarioinstance_id\":966,\"oldstate\":\"ready\",\"label\":\"ActivityFragment3\",\"newstate\":\"ready(ControlFlow)\"},\"117\":{\"activityinstance_id\":6686,\"scenarioinstance_id\":966,\"oldstate\":\"ready\",\"label\":\"Activity1Fragment2\",\"newstate\":\"running\"},\"116\":{\"activityinstance_id\":6691,\"scenarioinstance_id\":966,\"oldstate\":\"ready(ControlFlow)\",\"label\":\"ActivityFragment3\",\"newstate\":\"ready\"},\"115\":{\"activityinstance_id\":6691,\"scenarioinstance_id\":966,\"oldstate\":\"init\",\"label\":\"ActivityFragment3\",\"newstate\":\"ready(ControlFlow)\"},\"114\":{\"activityinstance_id\":6691,\"scenarioinstance_id\":966,\"label\":\"ActivityFragment3\",\"newstate\":\"init\"},\"113\":{\"activityinstance_id\":6687,\"scenarioinstance_id\":966,\"oldstate\":\"running\",\"label\":\"ActivityFragment3\",\"newstate\":\"terminated\"},\"112\":{\"activityinstance_id\":6687,\"scenarioinstance_id\":966,\"oldstate\":\"ready\",\"label\":\"ActivityFragment3\",\"newstate\":\"running\"},\"127\":{\"activityinstance_id\":6693,\"scenarioinstance_id\":966,\"oldstate\":\"ready(ControlFlow)\",\"label\":\"ActivityFragment4\",\"newstate\":\"ready\"},\"126\":{\"activityinstance_id\":6693,\"scenarioinstance_id\":966,\"oldstate\":\"init\",\"label\":\"ActivityFragment4\",\"newstate\":\"ready(ControlFlow)\"},\"125\":{\"activityinstance_id\":6693,\"scenarioinstance_id\":966,\"label\":\"ActivityFragment4\",\"newstate\":\"init\"},\"124\":{\"activityinstance_id\":6688,\"scenarioinstance_id\":966,\"oldstate\":\"running\",\"label\":\"ActivityFragment4\",\"newstate\":\"terminated\"},\"123\":{\"activityinstance_id\":6688,\"scenarioinstance_id\":966,\"oldstate\":\"ready\",\"label\":\"ActivityFragment4\",\"newstate\":\"running\"},\"122\":{\"activityinstance_id\":6692,\"scenarioinstance_id\":966,\"oldstate\":\"init\",\"label\":\"Activity1Fragment2\",\"newstate\":\"ready(ControlFlow)\"},\"121\":{\"activityinstance_id\":6692,\"scenarioinstance_id\":966,\"label\":\"Activity1Fragment2\",\"newstate\":\"init\"},\"120\":{\"activityinstance_id\":6690,\"scenarioinstance_id\":966,\"oldstate\":\"ready(ControlFlow)\",\"label\":\"Activity2Fragment1\",\"newstate\":\"ready\"}}\n").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }

    /**
     *
     */
    @Test
    public void testGetActivitiesLogWithState() {
        Response response = base.path("scenario/1/instance/966/activities").queryParam("state", "terminated").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"119\":{\"activityinstance_id\":6686,\"scenarioinstance_id\":966,\"oldstate\":\"running\",\"label\":\"Activity1Fragment2\",\"newstate\":\"terminated\"},\"102\":{\"activityinstance_id\":6685,\"scenarioinstance_id\":966,\"oldstate\":\"running\",\"label\":\"Activity1Fragment1\",\"newstate\":\"terminated\"},\"113\":{\"activityinstance_id\":6687,\"scenarioinstance_id\":966,\"oldstate\":\"running\",\"label\":\"ActivityFragment3\",\"newstate\":\"terminated\"},\"108\":{\"activityinstance_id\":6689,\"scenarioinstance_id\":966,\"oldstate\":\"running\",\"label\":\"Activity2Fragment1\",\"newstate\":\"terminated\"},\"124\":{\"activityinstance_id\":6688,\"scenarioinstance_id\":966,\"oldstate\":\"running\",\"label\":\"ActivityFragment4\",\"newstate\":\"terminated\"}}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }




    /**
     *
     */
    @Test
    public void testGetDataObjectsLog() {
        Response response = base.path("scenario/1/instance/966/dataobjects").request().get();
        assertThat("Get activities did not contain the expected information",
                response.readEntity(String.class),
                jsonEquals("{\"69\":{\"old_state_name\":\"\",\"scenarioinstance_id\":966,\"new_state_id\":1,\"new_state_name\":\"init\",\"name\":\"object1\",\"dataobjectinstance_id\":744},\"70\":{\"old_state_name\":\"\",\"scenarioinstance_id\":966,\"new_state_id\":5,\"new_state_name\":\"init\",\"name\":\"object2\",\"dataobjectinstance_id\":745},\"71\":{\"old_state_name\":\"init\",\"scenarioinstance_id\":966,\"new_state_id\":2,\"new_state_name\":\"bearbeitet\",\"name\":\"object1\",\"old_state_id\":1,\"dataobjectinstance_id\":744},\"72\":{\"old_state_name\":\"init\",\"scenarioinstance_id\":966,\"new_state_id\":6,\"new_state_name\":\"fertig\",\"name\":\"object2\",\"old_state_id\":5,\"dataobjectinstance_id\":745},\"73\":{\"old_state_name\":\"bearbeitet\",\"scenarioinstance_id\":966,\"new_state_id\":3,\"new_state_name\":\"gepr?ft\",\"name\":\"object1\",\"old_state_id\":2,\"dataobjectinstance_id\":744}}").when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }
}
