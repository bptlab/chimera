package de.uni_potsdam.hpi.bpt.bp2014.integration;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.DataAttributeUpdateJaxBean;
import net.javacrumbs.jsonunit.core.Option;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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
                ActivityRestService.class, ScenarioRestService.class, DataObjectRestService.class,
                DataDependencyRestService.class);
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

        Response startFirstActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/1/begin")
                .request().post(Entity.json("{}"));
        assertEquals(202, startFirstActivity.getStatus());

        Response terminateFirstActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/1/terminate")
                .request().post(Entity.json("{}"));
        assertEquals(202, terminateFirstActivity.getStatus());

        Response inputSets = base.path("interface/v2/scenario/1/instance/1/activity/2/input")
                .request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, inputSets.getStatus());
        assertThat(buildInputSetMap(), jsonEquals(inputSets.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));

        Response outputSets = base.path("interface/v2/scenario/1/instance/1/activity/2/output")
                .request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, outputSets.getStatus());
        assertThat(buildOutputSetMap(), jsonEquals(outputSets.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));

        Response startSecondActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/2/begin")
                .request().post(Entity.json("{}"));
        assertEquals(202, startSecondActivity.getStatus());

        Response terminateActivityUsingOutputSet = base.path("interface/v2/scenario/1/instance/1/activityinstance/2/terminate")
                .request().post(Entity.json(buildOutputSetSelection()));
        assertEquals(202, terminateActivityUsingOutputSet.getStatus());

        Response startActivityUsingInputSet = base.path("interface/v2/scenario/1/instance/1/activityinstance/3/begin")
                .request().post(Entity.json(buildInputSetSelection()));
        assertEquals(202, startActivityUsingInputSet.getStatus());
    }

    @Test
    public void testSelectedAttributeChange() throws IOException {
        WebTarget base = target();
        String path = "src/test/resources/Scenarios/IOSetScenario.json";
        String jsonString = FileUtils.readFileToString(new File(path));
        Response createScenario =
                base.path("interface/v2/scenario").request().post(Entity.json(jsonString));
        assertEquals(201, createScenario.getStatus());

        // Start scenario instance
        Response startScenario = base.path("interface/v2/scenario/1/instance").request().post(null);
        assertEquals(201, startScenario.getStatus());

        // Create some costumer objects
        for (int i = 0; i < 4; i++) {
            // TODO search activity id via REST
            Response beginCreateCostumer = base.path(
                    "interface/v2/scenario/1/instance/1/activity/6/begin")
                    .request().post(Entity.json("{}"));
            Response createCostumer = base.path(
                    "interface/v2/scenario/1/instance/1/activity/6/terminate")
                    .request().post(Entity.json("{}"));
            assertEquals(202, beginCreateCostumer.getStatus());
            assertEquals(202, createCostumer.getStatus());
        }

        Response startFirstActivity = base.path("interface/v2/scenario/1/instance/1/activity/1/begin")
                .request().post(Entity.json("{}"));
        assertEquals(202, startFirstActivity.getStatus());

        Response terminateFirstActivity = base.path("interface/v2/scenario/1/instance/1/activity/1/terminate")
                .request().post(Entity.json("{}"));
        assertEquals(202, terminateFirstActivity.getStatus());

        Response dataInput = base.path("interface/v2/scenario/1/instance/1/activity/2/availableInput")
                .request().get();
        System.out.println(dataInput.readEntity(String.class));

        Response startSecondActivity = base.path("interface/v2/scenario/1/instance/1/activity/2/begin")
                .request().post(Entity.json("{}"));
        assertEquals(202, startSecondActivity.getStatus());

//
//        DataAttributeUpdateJaxBean update = new DataAttributeUpdateJaxBean();
//        update.setId(1);
//        update.setValue("bar");
//        Response setDataAttribute = base.path("interface/v2/scenario/1/instance/1/activity/2").request()
//                .put(Entity.json(update));
//        assertEquals(202, setDataAttribute.getStatus());


        Response terminateActivityUsingOutputSet = base.path("interface/v2/scenario/1/instance/1/activity/2/terminate")
                .request().post(Entity.json(buildOutputSetSelection()));
        assertEquals(202, terminateActivityUsingOutputSet.getStatus());
    }


    private String buildInputSetMap() {
        return "{\"Order\":[\"init\"],\"Customer\":[\"init\"]}";
    }

    private String buildOutputSetMap() {
        return "{\"Order\":[\"accepted\", \"rejected\"],\"Customer\":[\"accepted\", \"rejected\"]}";
    }

    private String buildOutputSetSelection() {
        return "{\"Order\":\"accepted\",\"Customer\":\"accepted\"}";
    }

    private String buildInputSetSelection() {
        return "{\"dataobjects\" : [3, 5]}";
    }
}