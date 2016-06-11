package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbCaseStart;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.ScenarioData;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.StartQuery;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
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
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class DataObjectTest extends JerseyTest {

    WebTarget base;

    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(EventDispatcher.class);
    }


    @After
    public void tearDown() throws Exception {
        super.tearDown();
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testNoDataObjectsAtStart() throws IOException {
        ScenarioTestHelper.createScenarioInstance("src/test/resources/Scenarios/DataObjectTestScenario.json");
        assertEquals(0, getDataObjectCount());
    }

    @Test
    public void testObjectCreationAndUpdate() throws IOException {
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance("src/test/resources/Scenarios/DataObjectTestScenario.json");
        ScenarioTestHelper.executeActivityByName("create Old Class", instance);
        assertEquals(1, getDataObjectCount());
        ScenarioTestHelper.executeActivityByName("create New Class", instance);
        assertEquals(2, getDataObjectCount());
    }

    @Test
    public void testMultiObjectCreation() throws IOException {
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(
                "src/test/resources/Scenarios/DataObjectTestScenario.json");
        ScenarioTestHelper.executeActivityByName("create Old Class", instance);
        assertEquals(1, getDataObjectCount());
        ScenarioTestHelper.executeActivityByName("create New Class", instance);
        assertEquals(2, getDataObjectCount());
        ScenarioTestHelper.executeActivityByName("create Old Class", instance);
        assertEquals(3, getDataObjectCount());
    }

    @Test
    public void testInitializationViaCaseStart() throws IOException, JAXBException {
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
        ScenarioInstance scenarioInstance = new ScenarioInstance(1, 1);
        assertEquals(1, scenarioInstance.getDataManager().getDataObjects().size());
    }

    private int getDataObjectCount() {
        return new DbDataObject().executeStatementReturnsInt(
                "SELECT COUNT(*) as count FROM dataobject", "count");
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

}