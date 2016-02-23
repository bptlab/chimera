package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.IntegrationTests;


import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.Scenario;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataObjectInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface.DataObjectJaxBean;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 */

public class ScenarioTest {
    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testScenario() {
        //File file = new File("src/test/resources/jaxb/ExampleScenarioWithoutEvents.json");
        File file = new File("src/test/resources/EventScenarios/AsparagusScenarioWithoutEvents.json");
        Scenario scenario = new Scenario();
        try {
            String json = FileUtils.readFileToString(file);
            scenario.initializeInstanceFromJson(json);
            //ScenarioInstance instance = new ScenarioInstance(scenario.getScenarioDbId());
            ExecutionService service = ExecutionService.getInstance(scenario.getScenarioDbId());
            service.startNewScenarioInstance();
            DataObjectInstance[] dataObjectInstances = service
                    .getDataObjectInstancesForDataSetId(
                            7, 1);
            DataObjectJaxBean[] dataObjects = new DataObjectJaxBean[dataObjectInstances.length];
            for (int i = 0; i < dataObjectInstances.length; i++) {
                DataObjectJaxBean dataObject = new DataObjectJaxBean();
                dataObject.setSetId(7);
                dataObject.setId(dataObjectInstances[i].getDataObjectInstanceId());
                dataObject.setLabel(dataObjectInstances[i].getName());
                dataObject.setState(service
                        .getStateNameForDataObjectInstanceOutput(
                                dataObjectInstances[i], 7));
                dataObject.setAttributeConfiguration(service
                        .getDataAttributesForDataObjectInstance(
                                dataObjectInstances[i]));
                dataObjects[i] = dataObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test @Ignore
    public void testScenarioWithEvents() {
        File file = new File("src/test/resources/jaxb/scenarioWithEvents.json");
        Scenario scenario = new Scenario();
        try {
            String json = FileUtils.readFileToString(file);
            scenario.initializeInstanceFromJson(json);
            ScenarioInstance instance = new ScenarioInstance(1, 3);
        } catch (IOException e) {
            e.printStackTrace();
            assert(false);
        }
    }
}
