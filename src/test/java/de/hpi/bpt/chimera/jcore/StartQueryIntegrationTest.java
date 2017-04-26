package de.hpi.bpt.chimera.jcore;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.database.DbCaseStart;
import de.hpi.bpt.chimera.jcomparser.json.ScenarioData;
import de.hpi.bpt.chimera.jcomparser.json.StartQuery;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;

/**
 *
 */
public class StartQueryIntegrationTest extends AbstractTest {

    WebTarget base;

    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(EventDispatcher.class);
    }

    @Test
    public void testStartQueries() throws IOException, JAXBException {
        String path = "src/test/resources/Scenarios/StartScenario.json";
        String json = FileUtils.readFileToString(new File(path));
        ScenarioData scenarioData = new ScenarioData(json);
        scenarioData.save();

        DbCaseStart caseStart = new DbCaseStart();
        List<StartQuery> startQueries =  scenarioData.getStartQueries();
        StartQuery startQuery = startQueries.get(0);
        String requestKey = caseStart.getEventKey(startQuery.getId());
        String triggerPath = String.format("casestart/%s", requestKey);
        Response triggerEvent = base.path(triggerPath).request().post(
                Entity.json(getExampleJson().toString()));
        assertEquals(200, triggerEvent.getStatus());

        // Load scenario via 1, 1 since it is the only scenario in the database
        // TODO inspect data objects via Rest call
        ScenarioInstance scenarioInstance = new ScenarioInstance(1, 1);
        List<DataAttributeInstance> dataAttributeInstances = scenarioInstance.getDataManager()
                .getDataObjects().stream()
                .map(DataObject::getDataAttributeInstances)
                .flatMap(Collection::stream).collect(Collectors.toList());
        assertEquals(1L, dataAttributeInstances.stream().filter(
                x -> x.getValue().equals("someValue")).count());
        assertEquals(1L, dataAttributeInstances.stream().filter(
                x -> x.getValue().equals("someOtherValue")).count());
    }

    private JSONObject getExampleJson() {
        JSONObject json = new JSONObject();
        JSONObject bar = new JSONObject();
        bar.put("foo", "someValue");
        json.put("bar", bar);

        JSONObject a = new JSONObject();
        a.put("c", "someOtherValue");
        json.put("a", a);
        return json;
    }

    @Test
    public void testStartQueryDemoPaper() throws IOException, JAXBException {
        String path = "src/test/resources/Scenarios/DemoPaper.json";
        String json = FileUtils.readFileToString(new File(path));
        ScenarioData scenarioData = new ScenarioData(json);
        scenarioData.save();

        DbCaseStart caseStart = new DbCaseStart();
        List<StartQuery> startQueries =  scenarioData.getStartQueries();
        StartQuery startQuery = startQueries.get(0);
        String requestKey = caseStart.getEventKey(startQuery.getId());
        String triggerPath = String.format("casestart/%s", requestKey);
        Response triggerEvent = base.path(triggerPath).request().post(
                Entity.json(getDemoPaperJson().toString()));
        assertEquals(200, triggerEvent.getStatus());


    }

    private JSONObject getDemoPaperJson() {
        JSONObject json = new JSONObject();
        json.put("fieldId", 21);
        json.put("date", "blub");
        return json;
    }

}
