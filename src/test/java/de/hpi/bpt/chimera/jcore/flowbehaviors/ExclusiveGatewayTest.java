package de.hpi.bpt.chimera.jcore.flowbehaviors;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;

/**
 *
 */
public class ExclusiveGatewayTest extends AbstractDatabaseDependentTest {

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
        List <AbstractControlNodeInstance> skippedControlNodes = instance.getSkippedControlNodeInstances();
        assertEquals(2, skippedControlNodes.size());
    }

}