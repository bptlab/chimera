package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.NamedJaxBean;
import net.javacrumbs.jsonunit.core.Option;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.*;

public class RestInterfaceTest extends AbstractTest {

    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2RESTTest_new.sql";
    }

    private WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface.class);
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