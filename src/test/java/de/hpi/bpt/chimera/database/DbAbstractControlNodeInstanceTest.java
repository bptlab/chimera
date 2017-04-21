package de.hpi.bpt.chimera.database;

import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ControlNodeFactory;

/**
 *
 */
public class DbAbstractControlNodeInstanceTest extends AbstractDatabaseDependentTest {
    int controlNodeId = 1;
    int fragmentInstanceId  = 1;


    @Before
    public void setup() {
        ExampleValueInserter exampleValueInserter = new ExampleValueInserter();
        exampleValueInserter.insertControlNode("mock", "Activity", 1, "modelId");
        ScenarioInstance scenarioInstance = EasyMock.createNiceMock(ScenarioInstance.class);
        replay(scenarioInstance);
        ControlNodeFactory factory = new ControlNodeFactory();
        factory.createControlNodeInstance(controlNodeId, fragmentInstanceId, scenarioInstance);
    }

    /**
     *
     */
    @Test
    public void testExistControlNodeInstance(){
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        int controlNodeId = 1;
        int fragmentInstanceId = 1;
        assertTrue(dbControlNodeInstance.existControlNodeInstance(controlNodeId,
                fragmentInstanceId));
        assertFalse(dbControlNodeInstance.existControlNodeInstance(controlNodeId + 1,
                fragmentInstanceId));
        assertFalse(dbControlNodeInstance.existControlNodeInstance(controlNodeId,
                fragmentInstanceId + 1));
        assertFalse(dbControlNodeInstance.existControlNodeInstance(controlNodeId + 1,
                fragmentInstanceId + 1));
    }

    /**
     *
     */
    @Test
    public void testGetControlNodeInstanceID() {
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        assertEquals(1, dbControlNodeInstance.getControlNodeInstanceId(1, 1));
    }

    /**
     *
     */
    @Test
    public void testGetActivitiesForFragmentInstanceID() {
        ExampleValueInserter exampleValueInserter = new ExampleValueInserter();
        exampleValueInserter.insertControlNode("gateway", "XOR", 1, "modelId2");

        ControlNodeFactory factory = new ControlNodeFactory();
        ScenarioInstance scenarioInstance = EasyMock.createNiceMock(ScenarioInstance.class);
        replay(scenarioInstance);
        factory.createControlNodeInstance(2, fragmentInstanceId, scenarioInstance);

        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        assertEquals(1, dbControlNodeInstance.getActivitiesForFragmentInstanceId(1).size());
    }
}
