package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DbScenarioTest extends AbstractDatabaseDependentTest {
    /**
     *
     */
    @Ignore @Test
    public void testGetScenarioIDs() {
        DbScenario dbScenario = new DbScenario();
        List<Integer> scenarios = dbScenario.getScenarioIDs();
        assertEquals(1, (int) scenarios.get(0));
        assertEquals(2, (int) scenarios.get(1));
        assertEquals(3, (int) scenarios.get(2));
        assertEquals(100, (int) scenarios.get(3));
        assertEquals(101, (int) scenarios.get(4));
    }
}
