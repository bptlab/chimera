package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.IntegrationTests;


import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.ScenarioData;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataObjectInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface.DataObjectJaxBean;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import javax.xml.bind.JAXBException;
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
    public void testScenario() throws JAXBException {
        File file = new File("src/test/resources/EventScenarios/AsparagusScenario.json");

        try {
            String json = FileUtils.readFileToString(file);
            ScenarioData scenario = new ScenarioData(json);
            int databaseId = scenario.save();
            ExecutionService service = ExecutionService.getInstance(databaseId);
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
            Assert.fail();
        }
    }
}
