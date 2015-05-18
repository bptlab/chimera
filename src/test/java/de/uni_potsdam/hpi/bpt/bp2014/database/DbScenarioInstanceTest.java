package de.uni_potsdam.hpi.bpt.bp2014.database;


import static org.junit.Assert.*;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractAcceptanceTest;
import org.junit.Test;
import java.util.LinkedList;

/**
 *
 */
public class DbScenarioInstanceTest extends AbstractAcceptanceTest {
    @Test
    public void testExistScenario(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        assertTrue(dbScenarioInstance.existScenario(50));
        assertFalse(dbScenarioInstance.existScenario(999999));
        assertTrue(dbScenarioInstance.existScenario(1, 50));
        assertFalse(dbScenarioInstance.existScenario(1, 999));
    }

    @Test
    public void testGetScenarioInstanceID(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        assertEquals(86, dbScenarioInstance.getScenarioInstanceID(100));
    }

    @Test
    public void testGetScenarioInstances(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        LinkedList<Integer> instances = dbScenarioInstance.getScenarioInstances(100);
        assertEquals(85, (int)instances.get(0));
        assertEquals(86, (int)instances.get(1));
    }

    @Test
    public void testCreateNewScenarioInstance(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        dbScenarioInstance.createNewScenarioInstance(101);
        assertNotNull(dbScenarioInstance.getScenarioInstanceID(101));
    }



}
