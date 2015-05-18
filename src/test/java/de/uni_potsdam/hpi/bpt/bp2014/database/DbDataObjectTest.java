package de.uni_potsdam.hpi.bpt.bp2014.database;



import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractAcceptanceTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class DbDataObjectTest extends AbstractAcceptanceTest {

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
