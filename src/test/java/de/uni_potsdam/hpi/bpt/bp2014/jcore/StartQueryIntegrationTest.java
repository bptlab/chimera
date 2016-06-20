package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbCaseStart;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.ScenarioData;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.StartQuery;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class StartQueryIntegrationTest extends JerseyTest {

    WebTarget base;

    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
    }


    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
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
