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
public class RestConfigTest extends AbstractTest {

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2.sql";

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
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jconfiguration.rest.RestConfigurator.class);
    }

    @Before
    public void setUpBase() {
        base = target("config/v2");
    }

    //@Test
    //TODO: test open
    public void testChangingConfigOfeMailTasks() {
        //Response response = base.path("emailtask/1/").queryParam("filter", "HELLO").request().put();
        //assertThat("Get e did not contain the expected information",
        //         "{\"ids\":[1,2],\"labels\":{\"1\":\"HELLOWORLD\",\"2\":\"helloWorld2\"}}",
        //       jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

}