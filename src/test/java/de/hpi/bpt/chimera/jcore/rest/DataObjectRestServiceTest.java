package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.database.data.DbDataObject;
import de.hpi.bpt.chimera.jcore.rest.filters.AuthorizationRequestFilter;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class DataObjectRestServiceTest extends JerseyTest {

    private WebTarget base;

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig(DataObjectRestService.class);
        config.register(AuthorizationRequestFilter.class);
        return config;
    }

    @Before
    public void setUpBase() {
        base = target("interface/v2");
    }

    static {
        try {
            AbstractDatabaseDependentTest.resetDatabase();
            ScenarioTestHelper.createScenarioInstance("src/test/resources/Scenarios/DataRestServiceTest.json");
            new DbDataObject().createDataObject(1, 1, 1, 1);
            new DbDataObject().createDataObject(1, 1, 2, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void clearDataObjects() throws Exception {
        AbstractDatabaseDependentTest.resetDatabase();
    }


    @Test
    public void testGetDataObjects() {
        Response response = base.path("scenario/1/instance/1/dataobject").request().get();
        assertEquals("The Response code of getDataObjects was not 200",
                200, response.getStatus());
        assertEquals("getDataObjects returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[1,2],\"results\":{\"1\":{\"id\":1,\"label\":\"Customer\",\"state\":\"init\"},\"2\":{\"id\":2,\"label\":\"Customer\",\"state\":\"done\"}}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));//.when(Option.IGNORING_EXTRA_FIELDS));
    }

    @Test
    public void testGetDataObjectsWithFilter() {
        Response response = base.path("scenario/1/instance/1/dataobject")
                .queryParam("filter", "2").request().get();
        assertEquals("The Response code of getDataObjects was not 200",
                200, response.getStatus());
        assertEquals("getDataObjects returns a Response with the wrong media Type",
                MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"ids\":[2],\"results\":{\"2\":{\"id\":2,\"label\":\"Customer\",\"state\":\"done\"}}}",
                jsonEquals(response.readEntity(String.class))
                        .when(Option.IGNORING_ARRAY_ORDER));//.when(Option.IGNORING_EXTRA_FIELDS));
    }

}
