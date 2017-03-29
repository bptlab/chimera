package de.hpi.bpt.chimera.jcore.eventhandling;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.ScenarioTestHelper;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TimerExecutionTest {
    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testTimerEventExecution() {
        String path = "src/test/resources/Scenarios/TimerEventScenario.json";
        try {
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            List<String> registeredEventKeys = scenarioInstance.getRegisteredEventKeys();
            assertEquals(1, registeredEventKeys.size());
            Thread.sleep(6000);
            assertEquals(0, scenarioInstance.getRegisteredEventKeys().size());
            // TODO assert also that timer event is terminated not only unregistered
        } catch (IOException e) {
            fail();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
