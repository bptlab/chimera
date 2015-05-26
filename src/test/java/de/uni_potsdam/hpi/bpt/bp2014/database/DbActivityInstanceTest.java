package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
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
        assertEquals("ready", dbActivityInstance.getState(77));
    }

    /**
     *
     */
    @Test
    public void testSetState() {
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        dbActivityInstance.setState(91, "terminated");
        assertEquals("terminated", dbActivityInstance.getState(91));
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
