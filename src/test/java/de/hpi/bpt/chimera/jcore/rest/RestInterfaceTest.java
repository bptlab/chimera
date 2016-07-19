package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.AbstractTest;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.*;

public class RestInterfaceTest extends AbstractTest {

    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2RESTTest_new.sql";
    }

    private WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(RestInterface.class);
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }


    @Test
    public void testGetVersion() {
        Response response = base.path("version").request().get();
        assertEquals("The Response code of getVersion was not 200",
                200, response.getStatus());
        assertEquals("Get Version does not return a JSON",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"version\": \"1.0-SNAPSHOT\"}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));
    }
}