package de.uni_potsdam.hpi.bpt.bp2014;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by jaspar.mang on 12.01.15.
 */
public class DbControlNodeTest {
    @Test
    public void testGetStartEventID(){
        DbControlNode dbControlNode = new DbControlNode();
        assertEquals(1, dbControlNode.getStartEventID(1));
    }
    @Test
     public void testGetType(){
        DbControlNode dbControlNode = new DbControlNode();
        assertEquals("Startevent", dbControlNode.getType(1));
        assertEquals("Activity", dbControlNode.getType(2));
    }
}
