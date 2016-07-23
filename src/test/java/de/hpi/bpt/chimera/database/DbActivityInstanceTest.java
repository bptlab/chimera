package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.controlnodes.DbActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DbActivityInstanceTest extends AbstractDatabaseDependentTest {
    /**
     *
     */
    @Test
    public void testGetState(){
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        assertEquals(State.READY, dbActivityInstance.getState(77)) ;
    }

    /**
     *
     */
    @Test
    public void testSetState() {
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        dbActivityInstance.setState(91, State.TERMINATED);
        assertEquals(State.TERMINATED, dbActivityInstance.getState(91));
    }

    /**
     *
     */
    @Test
    public void testGetTerminatedActivitiesForScenarioInstance(){
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        assertEquals(2, (int)dbActivityInstance.getTerminatedActivitiesForScenarioInstance(223).get(0));
        assertEquals(5, (int)dbActivityInstance.getTerminatedActivitiesForScenarioInstance(223).get(1));
    }
}
