package de.uni_potsdam.hpi.bpt.bp2014.integration;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.ActivityRestService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.DataObjectRestService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.ScenarioInstanceRestService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.ScenarioRestService;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class EndToEndTest extends JerseyTest {


    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(EventDispatcher.class, ScenarioInstanceRestService.class,
                ActivityRestService.class, ScenarioRestService.class, DataObjectRestService.class);
    }

    @Test
    public void testScenarioWithIOSetsViaRest() throws IOException {
        WebTarget base = target();
        String path = "src/test/resources/Scenarios/IOSetScenario.json";
        String jsonString = FileUtils.readFileToString(new File(path));
        Response createScenario =
                base.path("interface/v2/scenario").request().post(Entity.json(jsonString));
        assertEquals(201, createScenario.getStatus());

        // Start scenario instance
        Response startScenario = base.path("interface/v2/scenario/1/instance").request().post(null);
        assertEquals(201, startScenario.getStatus());

        Response startFirstActivity = base.path("interface/v2/scenario/1/instance/1/activity/2/begin")
                .request().post(Entity.json("[]"));
        assertEquals(202, startFirstActivity.getStatus());

        Response termianteFirstActivity = base.path("interface/v2/scenario/1/instance/1/activity/2/terminate")
                .request().post(Entity.json("[]"));
        assertEquals(202, termianteFirstActivity.getStatus());

        Response inputSets = base.path("interface/v2/scenario/1/instance/1/activity/2/input")
                .request().get();
        assertEquals(200, inputSets.getStatus());
        String inputSetMap = buildInputSetMap();
        assertEquals(inputSetMap, inputSets.readEntity(String.class));
    }

    private String buildInputSetMap() {
        return "";
    }
}
