package de.hpi.bpt.chimera.database;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;

/**
 *
 */
public class DbScenarioInstanceTest extends AbstractDatabaseDependentTest {

    private int SCENARIO_ID;
    private int SCENARIO_INSTANCE_ID;

    @Before
    public void setup() {
        Connector connector = new Connector();
        SCENARIO_ID = connector.insertScenario("TestScenario", 1);
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        SCENARIO_INSTANCE_ID = dbScenarioInstance.
                createNewScenarioInstance(SCENARIO_ID);
    }

    @Test
    public void testInstanceBelongsToScenario(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        assertTrue(dbScenarioInstance.existsScenarioInstance(SCENARIO_ID));
        assertFalse(dbScenarioInstance.existsScenarioInstance(9999));
    }

    @Test
    public void testGetScenarioInstanceID(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        assertEquals(SCENARIO_INSTANCE_ID,
                dbScenarioInstance.getScenarioInstanceID(SCENARIO_ID));
    }

    @Test
    public void testGetScenarioInstances(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        int newScenarioInstance = dbScenarioInstance.
                createNewScenarioInstance(SCENARIO_ID);
        List<Integer> instances = dbScenarioInstance.getScenarioInstances(
                SCENARIO_ID);
        assertEquals(instances, Arrays.asList(SCENARIO_INSTANCE_ID, newScenarioInstance));
    }

    @Test
    public void testCreateNewScenarioInstance(){
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        dbScenarioInstance.createNewScenarioInstance(101);
        assertNotNull(dbScenarioInstance.getScenarioInstanceID(101));
    }
}
