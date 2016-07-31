package de.hpi.bpt.chimera.database;


import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class DbScenarioInstanceTest extends AbstractDatabaseDependentTest {
    /**
     *
     */
    @Ignore @Test
    public void testExistScenario(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        assertTrue(dbScenarioInstance.existScenario(50));
        assertFalse(dbScenarioInstance.existScenario(999999));
        assertTrue(dbScenarioInstance.existScenario(1, 50));
        assertFalse(dbScenarioInstance.existScenario(1, 999));
    }

    /**
     *
     */
    @Ignore @Test
    public void testGetScenarioInstanceID(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        assertEquals(86, dbScenarioInstance.getScenarioInstanceID(100));
    }

    /**
     *
     */
    @Ignore @Test
    public void testGetScenarioInstances(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        List<Integer> instances = dbScenarioInstance.getScenarioInstances(100);
        assertEquals(85, (int)instances.get(0));
        assertEquals(86, (int)instances.get(1));
    }

    /**
     *
     */
    @Ignore @Test
    public void testCreateNewScenarioInstance(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        dbScenarioInstance.createNewScenarioInstance(101);
        assertNotNull(dbScenarioInstance.getScenarioInstanceID(101));
    }
}
