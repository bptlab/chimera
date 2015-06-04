package de.uni_potsdam.hpi.bpt.bp2014.database;



import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * T
 */
public class DbDataObjectTest extends AbstractDatabaseDependentTest {

    @Test
    public void testGetDataObjectsForScenario(){
        DbDataObject dbDataObject = new DbDataObject();
        assertEquals(3, (int)dbDataObject.getDataObjectsForScenario(101).get(0));
        assertEquals(4, (int)dbDataObject.getDataObjectsForScenario(101).get(1));
    }

    @Test
    public void testGetStartStateIDo(){
        DbDataObject dbDataObject = new DbDataObject();
        assertEquals(1, dbDataObject.getStartStateID(3));
        assertEquals(3, dbDataObject.getStartStateID(4));
    }

}
