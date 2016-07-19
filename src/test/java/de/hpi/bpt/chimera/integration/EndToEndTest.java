package de.hpi.bpt.chimera.integration;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.jcore.rest.*;
import de.hpi.bpt.chimera.jcore.rest.filters.AuthorizationRequestFilter;
import net.javacrumbs.jsonunit.core.Option;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONArray;
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
                DataDependencyRestService.class, AuthorizationRequestFilter.class);
    }

    @Test
    public void testScenarioWithIOSetsViaRest() throws IOException {
        // TODO get dataobject ids via rest
        WebTarget base = target();
        String path = "src/test/resources/Scenarios/IOSetScenario.json";
        String jsonString = FileUtils.readFileToString(new File(path));
        Response createScenario =
                base.path("interface/v2/scenario").request().post(Entity.json(jsonString));
        assertEquals(201, createScenario.getStatus());

        // Start scenario instance
        Response startScenario = base.path("interface/v2/scenario/1/instance").request().post(null);
        assertEquals(201, startScenario.getStatus());

        Response startFirstActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/2/begin")
                .request().post(Entity.json("{}"));
        assertEquals(202, startFirstActivity.getStatus());

        Response terminateFirstActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/2/terminate")
                .request().post(Entity.json(buildOutputSetSelection1()));
        assertEquals(202, terminateFirstActivity.getStatus());

        Response inputSets = base.path("interface/v2/scenario/1/instance/1/activityinstance/5/input")
                .request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, inputSets.getStatus());
        assertThat(buildInputSetMap(), jsonEquals(inputSets.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));

        Response outputSets = base.path("interface/v2/scenario/1/instance/1/activityinstance/5/output")
                .request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, outputSets.getStatus());
        assertThat(buildOutputSetMap(), jsonEquals(outputSets.readEntity(String.class)).when(Option.IGNORING_ARRAY_ORDER));

        Response startSecondActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/5/begin")
                .request().post(Entity.json(buildInputSetSelection()));
        assertEquals(202, startSecondActivity.getStatus());

        Response terminateActivityUsingOutputSet = base.path("interface/v2/scenario/1/instance/1/activityinstance/5/terminate")
                .request().post(Entity.json(buildOutputSetSelection2()));
        assertEquals(202, terminateActivityUsingOutputSet.getStatus());

        Response startActivityUsingInputSet = base.path("interface/v2/scenario/1/instance/1/activityinstance/6/begin")
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
            int customerActivityId = idForEnabledActivityInstance("Create Costumer", base);
            Response beginCreateCostumer = base.path(
                    "interface/v2/scenario/1/instance/1/activityinstance/" + customerActivityId + "/begin")
                    .request().post(Entity.json("{}"));
            Response createCostumer = base.path(
                    "interface/v2/scenario/1/instance/1/activityinstance/" + customerActivityId + "/terminate")
                    .request().post(Entity.json(buildOutputSetSelectionCustomer()));
            assertEquals(202, beginCreateCostumer.getStatus());
            assertEquals(202, createCostumer.getStatus());
        }

        // TODO search for activities via REST
        Response startFirstActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/2/begin")
                .request().post(Entity.json("{}"));
        assertEquals(202, startFirstActivity.getStatus());

        Response terminateFirstActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/2/terminate")
                .request().post(Entity.json("{}"));
        assertEquals(202, terminateFirstActivity.getStatus());

        Response dataInput = base.path("interface/v2/scenario/1/instance/1/activity/3/availableInput")
                .request().get();
        System.out.println(dataInput.readEntity(String.class));

        Response startSecondActivity = base.path("interface/v2/scenario/1/instance/1/activityinstance/13/begin")
                .request().post(Entity.json("{}"));
        assertEquals(202, startSecondActivity.getStatus());

        String attributeUpdate = "{\"1\":\"bar\"}";
        Response setDataAttribute = base.path("interface/v2/scenario/1/instance/1/activityinstance/13").request()
                .put(Entity.json(attributeUpdate));
        assertEquals(202, setDataAttribute.getStatus());


        Response terminateActivityUsingOutputSet = base.path("interface/v2/scenario/1/instance/1/activityinstance/13/terminate")
                .request().post(Entity.json(buildOutputSetSelection2()));
        assertEquals(202, terminateActivityUsingOutputSet.getStatus());
    }

    private int idForEnabledActivityInstance(String label, WebTarget base) {
//        Response getActivities = base.path("interface/v2/scenario/1/instance/1/activity/?state=ready")
//                .request().get();
        Response getActivities = base.path("interface/v2/scenario/1/instance/1/activity")
                .queryParam("state", "ready").request().get();

        String json = getActivities.readEntity(String.class);
        JSONArray activityInstances = new JSONObject(json).getJSONArray("activities");
        for (int i = 0; i < activityInstances.length(); i++) {
            if (label.equals(activityInstances.getJSONObject(i).getString("label"))) {
                return activityInstances.getJSONObject(i).getInt("id");
            }
        }
        return -1;
    }


    private String buildInputSetMap() {
        return "{\"Order\":[\"init\"],\"Customer\":[\"init\"]}";
    }

    private String buildOutputSetMap() {
        return "{\"Order\":[\"accepted\", \"rejected\"],\"Customer\":[\"accepted\", \"rejected\"]}";
    }

    private String buildOutputSetSelection1() {
        return "{\"Order\":\"init\",\"Customer\":\"init\"}";
    }

    private String buildOutputSetSelection2() {
        return "{\"Order\":\"accepted\",\"Customer\":\"accepted\"}";
    }

    private String buildOutputSetSelectionCustomer() {
        return "{\"Customer\":\"init\"}";
    }

    private String buildInputSetSelection() {
        return "{\"dataobjects\" : [1, 2]}";
    }
}