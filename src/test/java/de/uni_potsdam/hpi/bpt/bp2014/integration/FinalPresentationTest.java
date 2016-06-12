package de.uni_potsdam.hpi.bpt.bp2014.integration;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbCaseStart;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.ScenarioData;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.StartQuery;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FinalPresentationTest extends JerseyTest {


    WebTarget base;

    ScenarioInstance instance;

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
    public void testFullScenario() throws IOException, JAXBException, InterruptedException {
        String path = "src/test/resources/EventScenarios/FinalPresentation.json";
        String json = FileUtils.readFileToString(new File(path));
        ScenarioData scenarioData = new ScenarioData(json);
        scenarioData.save();

        DbCaseStart caseStart = new DbCaseStart();
        List<StartQuery> startQueries =  scenarioData.getStartQueries();
        StartQuery startQuery = startQueries.get(0);
        String requestKey = caseStart.getEventKey(startQuery.getId());
        String triggerPath = String.format("casestart/%s", requestKey);
        Response triggerEvent = base.path(triggerPath).request().post(
                Entity.json("{}"));
        assertEquals(200, triggerEvent.getStatus());

        instance = new ScenarioInstance(1, 1);
        assertEquals(1, instance.getDataManager().getDataObjects().size());
        assertEquals("Docu", instance.getDataManager().getDataObjects().get(0).getName());

        ex("Find feature idea");
        ex("Plan and document in JIRA");
        ex("Write tests for feature");
        ScenarioTestHelper.beginActivityByName("Create Feature", instance);
        tr();
        tr();
        instance = new ScenarioInstance(1, 1);
        ex("Fix Tests");
        ex("Create Feature");

        ex("Start hacking intensively");
        ex("Prepare presentation");
        ex("Hold presentation");

        ex("Clean up code");
        ex("Write documentation");

        ex("Find title and write draft");
        Thread.sleep(5000);
        instance = new ScenarioInstance(1, 1);
        ex("Review draft");
        ex("Complete Thesis");

        assertTrue(instance.checkTerminationCondition());
    }

    private void ex(String name) {
        ScenarioTestHelper.executeActivityByName(name, instance);
    }

    private void tr() {
        ScenarioTestHelper.triggerEventInScenario(instance, base);
    }
}
