package de.hpi.bpt.chimera.database;


import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class DbScenarioInstanceTest {

    private int SCENARIO_ID;
    private int SCENARIO_INSTANCE_ID;

    @Before
    public void setup() {
        Connector connector = new Connector();
        SCENARIO_ID = connector.insertScenarioIntoDatabase("TestScenario", 1);
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        SCENARIO_INSTANCE_ID = dbScenarioInstance.
                createNewScenarioInstance(SCENARIO_ID);
    }

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
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
