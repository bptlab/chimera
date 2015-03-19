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
        base = target("v2/history/");
    }




    //@Test
    public void testGetActivitiesLog() {
        Response response = base.path("scenario/1/instance/966/activities").request().get();
        assertThat("Get Scenarios did not contain the expected information",
        "{\"91\":{\"scenarioinstance_id\":966,\"newstate\":\"init\",\"activityinstance_id\":6685,\"label\":\"Activity1Fragment1\",\"timestamp\":\"2015-03-18 11:04:57.0\"},\"92\":{\"scenarioinstance_id\":966,\"newstate\":\"ready(ControlFlow)\",\"oldstate\":\"init\",\"activityinstance_id\":6685,\"label\":\"Activity1Fragment1\",\"timestamp\":\"2015-03-18 11:04:57.0\"},\"93\":{\"scenarioinstance_id\":966,\"newstate\":\"ready\",\"oldstate\":\"ready(ControlFlow)\",\"activityinstance_id\":6685,\"label\":\"Activity1Fragment1\",\"timestamp\":\"2015-03-18 11:04:57.0\"},\"94\":{\"scenarioinstance_id\":966,\"newstate\":\"init\",\"activityinstance_id\":6686,\"label\":\"Activity1Fragment2\",\"timestamp\":\"2015-03-18 11:04:57.0\"},\"95\":{\"scenarioinstance_id\":966,\"newstate\":\"ready(ControlFlow)\",\"oldstate\":\"init\",\"activityinstance_id\":6686,\"label\":\"Activity1Fragment2\",\"timestamp\":\"2015-03-18 11:04:57.0\"},\"96\":{\"scenarioinstance_id\":966,\"newstate\":\"init\",\"activityinstance_id\":6687,\"label\":\"ActivityFragment3\",\"timestamp\":\"2015-03-18 11:04:57.0\"},\"97\":{\"scenarioinstance_id\":966,\"newstate\":\"ready(ControlFlow)\",\"oldstate\":\"init\",\"activityinstance_id\":6687,\"label\":\"ActivityFragment3\",\"timestamp\":\"2015-03-18 11:04:57.0\"},\"98\":{\"scenarioinstance_id\":966,\"newstate\":\"init\",\"activityinstance_id\":6688,\"label\":\"ActivityFragment4\",\"timestamp\":\"2015-03-18 11:04:58.0\"},\"99\":{\"scenarioinstance_id\":966,\"newstate\":\"ready(ControlFlow)\",\"oldstate\":\"init\",\"activityinstance_id\":6688,\"label\":\"ActivityFragment4\",\"timestamp\":\"2015-03-18 11:04:58.0\"},\"100\":{\"scenarioinstance_id\":966,\"newstate\":\"ready\",\"oldstate\":\"ready(ControlFlow)\",\"activityinstance_id\":6688,\"label\":\"ActivityFragment4\",\"timestamp\":\"2015-03-18 11:04:58.0\"},\"101\":{\"scenarioinstance_id\":966,\"newstate\":\"running\",\"oldstate\":\"ready\",\"activityinstance_id\":6685,\"label\":\"Activity1Fragment1\",\"timestamp\":\"2015-03-18 11:05:09.0\"},\"102\":{\"scenarioinstance_id\":966,\"newstate\":\"terminated\",\"oldstate\":\"running\",\"activityinstance_id\":6685,\"label\":\"Activity1Fragment1\",\"timestamp\":\"2015-03-18 11:05:09.0\"},\"103\":{\"scenarioinstance_id\":966,\"newstate\":\"ready\",\"oldstate\":\"ready(ControlFlow)\",\"activityinstance_id\":6686,\"label\":\"Activity1Fragment2\",\"timestamp\":\"2015-03-18 11:05:09.0\"},\"104\":{\"scenarioinstance_id\":966,\"newstate\":\"init\",\"activityinstance_id\":6689,\"label\":\"Activity2Fragment1\",\"timestamp\":\"2015-03-18 11:05:09.0\"},\"105\":{\"scenarioinstance_id\":966,\"newstate\":\"ready(ControlFlow)\",\"oldstate\":\"init\",\"activityinstance_id\":6689,\"label\":\"Activity2Fragment1\",\"timestamp\":\"2015-03-18 11:05:09.0\"},\"106\":{\"scenarioinstance_id\":966,\"newstate\":\"ready\",\"oldstate\":\"ready(ControlFlow)\",\"activityinstance_id\":6689,\"label\":\"Activity2Fragment1\",\"timestamp\":\"2015-03-18 11:05:09.0\"},\"107\":{\"scenarioinstance_id\":966,\"newstate\":\"running\",\"oldstate\":\"ready\",\"activityinstance_id\":6689,\"label\":\"Activity2Fragment1\",\"timestamp\":\"2015-03-18 11:05:13.0\"},\"108\":{\"scenarioinstance_id\":966,\"newstate\":\"terminated\",\"oldstate\":\"running\",\"activityinstance_id\":6689,\"label\":\"Activity2Fragment1\",\"timestamp\":\"2015-03-18 11:05:14.0\"},\"109\":{\"scenarioinstance_id\":966,\"newstate\":\"ready\",\"oldstate\":\"ready(ControlFlow)\",\"activityinstance_id\":6687,\"label\":\"ActivityFragment3\",\"timestamp\":\"2015-03-18 11:05:14.0\"},\"110\":{\"scenarioinstance_id\":966,\"newstate\":\"init\",\"activityinstance_id\":6690,\"label\":\"Activity2Fragment1\",\"timestamp\":\"2015-03-18 11:05:14.0\"},\"111\":{\"scenarioinstance_id\":966,\"newstate\":\"ready(ControlFlow)\",\"oldstate\":\"init\",\"activityinstance_id\":6690,\"label\":\"Activity2Fragment1\",\"timestamp\":\"2015-03-18 11:05:14.0\"},\"112\":{\"scenarioinstance_id\":966,\"newstate\":\"running\",\"oldstate\":\"ready\",\"activityinstance_id\":6687,\"label\":\"ActivityFragment3\",\"timestamp\":\"2015-03-18 11:05:16.0\"},\"113\":{\"scenarioinstance_id\":966,\"newstate\":\"terminated\",\"oldstate\":\"running\",\"activityinstance_id\":6687,\"label\":\"ActivityFragment3\",\"timestamp\":\"2015-03-18 11:05:16.0\"},\"114\":{\"scenarioinstance_id\":966,\"newstate\":\"init\",\"activityinstance_id\":6691,\"label\":\"ActivityFragment3\",\"timestamp\":\"2015-03-18 11:05:16.0\"},\"115\":{\"scenarioinstance_id\":966,\"newstate\":\"ready(ControlFlow)\",\"oldstate\":\"init\",\"activityinstance_id\":6691,\"label\":\"ActivityFragment3\",\"timestamp\":\"2015-03-18 11:05:16.0\"},\"116\":{\"scenarioinstance_id\":966,\"newstate\":\"ready\",\"oldstate\":\"ready(ControlFlow)\",\"activityinstance_id\":6691,\"label\":\"ActivityFragment3\",\"timestamp\":\"2015-03-18 11:05:16.0\"},\"117\":{\"scenarioinstance_id\":966,\"newstate\":\"running\",\"oldstate\":\"ready\",\"activityinstance_id\":6686,\"label\":\"Activity1Fragment2\",\"timestamp\":\"2015-03-18 11:05:18.0\"},\"118\":{\"scenarioinstance_id\":966,\"newstate\":\"ready(ControlFlow)\",\"oldstate\":\"ready\",\"activityinstance_id\":6691,\"label\":\"ActivityFragment3\",\"timestamp\":\"2015-03-18 11:05:18.0\"},\"119\":{\"scenarioinstance_id\":966,\"newstate\":\"terminated\",\"oldstate\":\"running\",\"activityinstance_id\":6686,\"label\":\"Activity1Fragment2\",\"timestamp\":\"2015-03-18 11:05:18.0\"},\"120\":{\"scenarioinstance_id\":966,\"newstate\":\"ready\",\"oldstate\":\"ready(ControlFlow)\",\"activityinstance_id\":6690,\"label\":\"Activity2Fragment1\",\"timestamp\":\"2015-03-18 11:05:18.0\"},\"121\":{\"scenarioinstance_id\":966,\"newstate\":\"init\",\"activityinstance_id\":6692,\"label\":\"Activity1Fragment2\",\"timestamp\":\"2015-03-18 11:05:18.0\"},\"122\":{\"scenarioinstance_id\":966,\"newstate\":\"ready(ControlFlow)\",\"oldstate\":\"init\",\"activityinstance_id\":6692,\"label\":\"Activity1Fragment2\",\"timestamp\":\"2015-03-18 11:05:18.0\"},\"123\":{\"scenarioinstance_id\":966,\"newstate\":\"running\",\"oldstate\":\"ready\",\"activityinstance_id\":6688,\"label\":\"ActivityFragment4\",\"timestamp\":\"2015-03-18 11:05:20.0\"},\"124\":{\"scenarioinstance_id\":966,\"newstate\":\"terminated\",\"oldstate\":\"running\",\"activityinstance_id\":6688,\"label\":\"ActivityFragment4\",\"timestamp\":\"2015-03-18 11:05:20.0\"},\"125\":{\"scenarioinstance_id\":966,\"newstate\":\"init\",\"activityinstance_id\":6693,\"label\":\"ActivityFragment4\",\"timestamp\":\"2015-03-18 11:05:20.0\"},\"126\":{\"scenarioinstance_id\":966,\"newstate\":\"ready(ControlFlow)\",\"oldstate\":\"init\",\"activityinstance_id\":6693,\"label\":\"ActivityFragment4\",\"timestamp\":\"2015-03-18 11:05:20.0\"},\"127\":{\"scenarioinstance_id\":966,\"newstate\":\"ready\",\"oldstate\":\"ready(ControlFlow)\",\"activityinstance_id\":6693,\"label\":\"ActivityFragment4\",\"timestamp\":\"2015-03-18 11:05:20.0\"}}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    //@Test
    public void testGetDataObjectsLog() {
        Response response = base.path("scenario/1/instance/966/dataobjects").request().get();
        assertThat("Get Scenarios did not contain the expected information",
                "{\"69\":{\"old_state_name\":\"\",\"scenarioinstance_id\":966,\"new_state_id\":1,\"new_state_name\":\"init\",\"name\":\"object1\",\"dataobjectinstance_id\":744,\"timestamp\":\"2015-03-18 11:04:57.0\"},\"70\":{\"old_state_name\":\"\",\"scenarioinstance_id\":966,\"new_state_id\":5,\"new_state_name\":\"init\",\"name\":\"object2\",\"dataobjectinstance_id\":745,\"timestamp\":\"2015-03-18 11:04:57.0\"},\"71\":{\"old_state_name\":\"init\",\"scenarioinstance_id\":966,\"new_state_id\":2,\"new_state_name\":\"bearbeitet\",\"name\":\"object1\",\"old_state_id\":1,\"dataobjectinstance_id\":744,\"timestamp\":\"2015-03-18 11:05:09.0\"},\"72\":{\"old_state_name\":\"init\",\"scenarioinstance_id\":966,\"new_state_id\":6,\"new_state_name\":\"fertig\",\"name\":\"object2\",\"old_state_id\":5,\"dataobjectinstance_id\":745,\"timestamp\":\"2015-03-18 11:05:14.0\"},\"73\":{\"old_state_name\":\"bearbeitet\",\"scenarioinstance_id\":966,\"new_state_id\":3,\"new_state_name\":\"gepr?ft\",\"name\":\"object1\",\"old_state_id\":2,\"dataobjectinstance_id\":744,\"timestamp\":\"2015-03-18 11:05:18.0\"}}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }


}
