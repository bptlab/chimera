package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import org.junit.Test;

import static org.junit.Assert.*;



/**
 *
 */
public class DbDataObjectInstanceTest extends AbstractDatabaseDependentTest {
    @Test
    public void testExistDataObjectInstance(){
        DbDataObjectInstance dbDataObjectInstance = new DbDataObjectInstance();
        assertTrue(dbDataObjectInstance.existDataObjectInstance(62, 2));
        assertFalse(dbDataObjectInstance.existDataObjectInstance(62,10));
        assertFalse(dbDataObjectInstance.existDataObjectInstance(2,2));
        assertFalse(dbDataObjectInstance.existDataObjectInstance(622,120));
    }

    @Test
    public void testGetStateID(){
        DbDataObjectInstance dbDataObjectInstance = new DbDataObjectInstance();
        assertEquals(5, dbDataObjectInstance.getStateID(8));
    }

    @Test
    public void testGetDataObjectInstanceID(){
        DbDataObjectInstance dbDataObjectInstance = new DbDataObjectInstance();
        assertEquals(11, dbDataObjectInstance.getDataObjectInstanceID(64, 1));
    }

    @Test
    public void SetState(){
        DbDataObjectInstance dbDataObjectInstance = new DbDataObjectInstance();
        dbDataObjectInstance.setState(23, 2);
        assertEquals(2, dbDataObjectInstance.getStateID(23));
    }
}
