package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.IntegrationTests;


import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Scenario;
import org.apache.commons.io.FileUtils;
import org.junit.After;
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
        File file = new File("src/test/resources/jaxb/ExampleScenario.json");
        Scenario scenario = new Scenario();
        try {
            String json = FileUtils.readFileToString(file);
            scenario.initializeInstanceFromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
            assert(false);
        }
    }
}
