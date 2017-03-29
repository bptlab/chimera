package de.hpi.bpt.chimera.jcore;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

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
