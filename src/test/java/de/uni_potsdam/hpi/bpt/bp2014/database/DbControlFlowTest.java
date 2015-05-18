package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependendTest;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 */
public class DbControlFlowTest extends AbstractDatabaseDependendTest {
    @Test
    public void testGetNextControlNodeAfterStartEvent(){
        DbControlFlow dbControlFlow = new DbControlFlow();
        assertEquals(2, dbControlFlow.getNextControlNodeAfterStartEvent(1));
    }

   @Test
    public void testGetFollowingControlNodes(){
       DbControlFlow dbControlFlow = new DbControlFlow();
       assertEquals(13, (int)dbControlFlow.getFollowingControlNodes(12).get(0));
       assertEquals(14, (int)dbControlFlow.getFollowingControlNodes(12).get(1));
   }
}

