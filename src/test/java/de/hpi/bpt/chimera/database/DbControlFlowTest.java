package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 */
public class DbControlFlowTest extends AbstractDatabaseDependentTest {
    @Ignore @Test
    public void testGetNextControlNodeAfterStartEvent(){
        DbControlFlow dbControlFlow = new DbControlFlow();
        assertEquals(2, dbControlFlow.getNextControlNodeAfterStartEvent(1));
    }

    @Ignore @Test
    public void testGetFollowingControlNodes(){
       DbControlFlow dbControlFlow = new DbControlFlow();
       assertEquals(13, (int)dbControlFlow.getFollowingControlNodes(12).get(0));
       assertEquals(14, (int)dbControlFlow.getFollowingControlNodes(12).get(1));
   }
}

