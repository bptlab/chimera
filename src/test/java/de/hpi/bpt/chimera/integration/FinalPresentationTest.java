package de.hpi.bpt.chimera.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.database.DbCaseStart;
import de.hpi.bpt.chimera.jcomparser.json.ScenarioData;
import de.hpi.bpt.chimera.jcomparser.json.StartQuery;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;

public class FinalPresentationTest extends AbstractTest {


    WebTarget base;

    ScenarioInstance instance;

    @Before
    public void setUpBase() {
        base = target("eventdispatcher");
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
        tr("{\"PresentationDate\":\"13.06.2016\"}");
        ScenarioTestHelper.beginActivityByName("Create Feature", instance);
        tr("{}");
        instance = new ScenarioInstance(1, 1);

        List<String> attributes = instance.getDataManager().getDataAttributeInstances()
                .stream().map(x -> x.getValue().toString()).collect(Collectors.toList());
        assertTrue(attributes.contains("13.06.2016"));

        ex("Fix Tests");
        ex("Create Feature");

        ex("Start hacking intensively");
        ex("Prepare presentation");
        ex("Hold presentation");

        ex("Clean up code");
        ex("Write documentation");

        ex("Find title and write draft");
        Thread.sleep(7000); // Wait for timer event to trigger
        instance = new ScenarioInstance(1, 1);
        ex("Review draft");
        ex("Complete Thesis");

        assertTrue(instance.checkTerminationCondition());
    }

    private void ex(String name) {
        ScenarioTestHelper.executeActivityByName(name, instance);
    }

    private void tr(String val) {
        ScenarioTestHelper.triggerEventInScenario(instance, base, val);
    }
}
