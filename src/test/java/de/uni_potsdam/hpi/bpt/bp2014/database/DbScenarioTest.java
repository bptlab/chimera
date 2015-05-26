package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DbScenarioTest extends AbstractDatabaseDependentTest {
    /**
     *
     */
    @Test
    public void testGetScenarioIDs() {
        DbScenario dbScenario = new DbScenario();
        LinkedList<Integer> scenarios = dbScenario.getScenarioIDs();
        assertEquals(1, (int) scenarios.get(0));
        assertEquals(2, (int) scenarios.get(1));
        assertEquals(3, (int) scenarios.get(2));
        assertEquals(100, (int) scenarios.get(3));
        assertEquals(101, (int) scenarios.get(4));
    }
}
