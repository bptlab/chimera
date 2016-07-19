package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class DbControlNodeTest extends AbstractDatabaseDependentTest {
    /**
     *
     */
    @Test
    public void testGetStartEventID(){
        DbControlNode dbControlNode = new DbControlNode();
        assertEquals(1, dbControlNode.getStartEventID(1));
    }

    /**
     *
     */
    @Test
     public void testGetType(){
        DbControlNode dbControlNode = new DbControlNode();
        assertEquals("StartEvent", dbControlNode.getType(1));
        assertEquals("Activity", dbControlNode.getType(2));
    }

    /**
     *
     */
    @Test
    public void testGetLabel(){
        DbControlNode dbControlNode = new DbControlNode();
        assertEquals("Activity2Fragment1", dbControlNode.getLabel(5));
        assertEquals("test1", dbControlNode.getLabel(12));
    }
}
