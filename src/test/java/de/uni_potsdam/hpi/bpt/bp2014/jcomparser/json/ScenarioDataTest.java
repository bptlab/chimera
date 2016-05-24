package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 *
 */
public class ScenarioDataTest {
    private static final Logger LOGGER = Logger.getLogger(ScenarioDataTest.class);

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testSave() throws Exception {
        try {
            String path = "src/test/resources/Scenarios/IOSetScenario.json";
            String jsonString = FileUtils.readFileToString(new File(path));
            ScenarioData data = new ScenarioData(jsonString);
            int scenarioId = data.save();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            Assert.fail();
        }
    }

    @Test
    public void testAssociateDataNodesWithDataClasses() {
        Assert.fail();
    }

    @Test
    public void testAssociateStatesWithDataClasses() {
        Assert.fail();
    }

    @Test
    public void testGenerateFragmentList() {
        Assert.fail();
    }
}