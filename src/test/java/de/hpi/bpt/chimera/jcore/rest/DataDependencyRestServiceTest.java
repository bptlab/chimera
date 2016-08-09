package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.database.DbFragmentInstance;
import de.hpi.bpt.chimera.database.DbScenarioInstance;
import de.hpi.bpt.chimera.database.ExampleValueInserter;
import de.hpi.bpt.chimera.database.controlnodes.DbActivityInstance;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.database.data.DbDataObject;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jcore.rest.filters.AuthorizationRequestFilter;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.sql.SQLException;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.*;

/**
 *
 */
public class DataDependencyRestServiceTest extends JerseyTest {

    private WebTarget base;

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig(DataDependencyRestService.class);
        config.register(AuthorizationRequestFilter.class);
        return config;
    }

    @Before
    public void setUpBaseAndInitialize() throws Exception {
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
    public static void clearDataDependecies() throws Exception {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testGetInputDataObjects(){
        Response response = base.path("scenario/1/instance/1/activityinstance/2/input").request().get();
        assertEquals("The response code of getInputDataObjects was not 200", 200, response.getStatus());
        assertEquals("GetInputDataObjects does not return a JSON", MediaType.APPLICATION_JSON,
                response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"Customer\":[\"init\"]}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testGetOutputDataObjects(){
        Response response = base.path("scenario/1/instance/1/activityinstance/2/output").request().get();
        assertEquals("The response code of getOutputDataObjects was not 200", 200, response.getStatus());
        assertEquals("GetOutputDataObjects does not return a JSON", MediaType.APPLICATION_JSON,
                response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "{\"Customer\":[\"done\"]}",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

    @Test
    public void testGetAvailableInput() {
        Response response = base.path("scenario/1/instance/1/activity/1/availableInput").request().get();
        assertEquals("The response code of getOutputDataObjects was not 202", 202, response.getStatus());
        assertEquals("GetOutputDataObjects does not return a JSON", MediaType.APPLICATION_JSON,
                response.getMediaType().toString());
        assertThat("The returned JSON does not contain the expected content",
                "[{\"label\":\"Customer\", \"id\":1, \"state\":\"init\", \"attributeConfiguration\":[]}]",
                jsonEquals(response.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));
    }

}