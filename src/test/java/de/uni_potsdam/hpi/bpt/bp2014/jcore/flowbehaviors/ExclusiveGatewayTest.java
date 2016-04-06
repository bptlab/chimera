package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class ExclusiveGatewayTest {
    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testNestedXorGateways() throws IOException {
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(
                "src/test/resources/core/NestedXorScenario.json");

        // Every activity after a nested Xor should be enabled.
        List<AbstractControlNodeInstance> enabledControlNodes =
                instance.getEnabledControlNodeInstances();
        assertEquals(3, enabledControlNodes.size());

        // After terminating one of the activities all other activities are disabled
        ScenarioTestHelper.beginActivityByName("A", instance);
        enabledControlNodes = instance.getEnabledControlNodeInstances();
        assertEquals(0, enabledControlNodes.size());
    }
    
}