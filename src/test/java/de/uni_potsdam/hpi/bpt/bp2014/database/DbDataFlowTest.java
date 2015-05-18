package de.uni_potsdam.hpi.bpt.bp2014.database;

import static org.junit.Assert.*;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependendTest;
import org.junit.Test;

/**
 *
 */
public class DbDataFlowTest extends AbstractDatabaseDependendTest {

    @Test
    public void testGetInputSetsForControlNode(){
        DbDataFlow dbDataFlow = new DbDataFlow();
        assertEquals(10, (int)dbDataFlow.getInputSetsForControlNode(12).get(0));
        assertEquals(11, (int)dbDataFlow.getInputSetsForControlNode(12).get(1));
    }
    @Test
    public void testGetOutputSetsForControlNode(){
        DbDataFlow dbDataFlow = new DbDataFlow();
        assertEquals(12, (int)dbDataFlow.getOutputSetsForControlNode(13).get(0));
        assertEquals(13, (int)dbDataFlow.getOutputSetsForControlNode(13).get(1));
    }
}
