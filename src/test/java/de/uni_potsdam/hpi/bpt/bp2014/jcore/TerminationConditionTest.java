package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class TerminationConditionTest {
    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testTerminationCondition() throws IOException {
        String path = "src/test/resources/Scenarios/TerminationConditionScenario.json";
        ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
        assertFalse(scenarioInstance.checkTerminationCondition());

        ScenarioTestHelper.executeActivityByName("Create Customer", scenarioInstance);
        assertTrue(scenarioInstance.checkTerminationCondition());
        ScenarioTestHelper.executeActivityByName("Register Customer", scenarioInstance);
        assertFalse(scenarioInstance.checkTerminationCondition());
    }

    @Test
    public void testMultiTerminationCondition() {
        try {
            String path = "src/test/resources/Scenarios/TerminationConditionScenario.json";
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            assertFalse(scenarioInstance.checkTerminationCondition());
            ScenarioTestHelper.executeActivityByName("Create Contract and Bill", scenarioInstance);
            assertFalse(scenarioInstance.checkTerminationCondition());
            ScenarioTestHelper.executeActivityByName("Pay bill", scenarioInstance);
            assertTrue(scenarioInstance.checkTerminationCondition());
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
