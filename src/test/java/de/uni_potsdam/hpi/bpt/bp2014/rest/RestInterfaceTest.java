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
public class RestInterfaceTest extends AbstractTest {

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JUserManagement.sql";
    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JUserManagement_RESTTest.sql";
    }
    /**
     * The base url of the rest interface.
     * Allows us to send requests to the {@link de.uni_potsdam.hpi.bpt.bp2014.rest.RestInterface}.
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
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.rest.RestInterface.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v1");
    }

    /***********************************************************************************
     * TESTs
     */

    /**
     *
     */
    @Test
    public void testGetAllUserStatusCheck() {
        Response response = base.path("user").request().get();
        assertEquals("The Response code of get Scenario was not 200",
                200, response.getStatus());
        assertEquals("Get user returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     *
     */
    @Test
    public void testGetAllUserContentCheck() {
        Response response = base.path("user").request().get();
        assertThat("Get Scenarios did not contain the expected information",
                "{\"myArrayList\":[{\"map\":{\"id\":1,\"username\":\"max\",\"role_id\":1,\"description\":\"Max der Gro?e\"}},{\"map\":{\"id\":2,\"username\":\"robert\",\"role_id\":1,\"description\":\"Mitarbeiter des Monats, admin der rolle 1\"}},{\"map\":{\"id\":3,\"username\":\"Lisa\",\"role_id\":1,\"description\":\"\"}},{\"map\":{\"id\":4,\"username\":\"Steffi\",\"role_id\":10,\"description\":\"Manager\"}},{\"map\":{\"id\":5,\"username\":\"Rolf\",\"role_id\":10,\"description\":\"Top Manager\"}}]}\n",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     *
     */
    @Test
    public void testGetAllRoleStatusCheck() {
        Response response = base.path("role").request().get();
        assertEquals("The Response code of get Scenario was not 200",
                200, response.getStatus());
        assertEquals("Get user returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
    }

    /**
     *
     */
    @Test
    public void testGetAllRoleContentCheck() {
        Response response = base.path("role").request().get();
        assertThat("Get Scenarios did not contain the expected information",
                "{\"myArrayList\":[{\"map\":{\"id\":1,\"rolename\":\"service_mitarbeiter\",\"description\":\"Die Bearbeiten Antr?ge und rufen Leute an\",\"admin_id\":2}},{\"map\":{\"id\":10,\"rolename\":\"manager\",\"description\":\"Die koordinieren und managen die Aufgaben\",\"admin_id\":0}}]}\n",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    /**
     *
     */
    @Test
    public void testDeleteUserStatusCheck() {
        Response response = base.path("user/1/").request().delete();
        assertEquals("The Response code of deleting a user was not 200",
                202, response.getStatus());
    }

    /**
     *
     */
    @Test
    public void testDeleteRoleStatusCheck() {
        Response response = base.path("role/1/").request().delete();
        assertEquals("The Response code of deleting a user was not 200",
                202, response.getStatus());
    }
}