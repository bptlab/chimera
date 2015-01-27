package de.uni_potsdam.hpi.bpt.bp2014.database;

import static org.junit.Assert.*;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import org.junit.Test;

/**
 * Created by jaspar.mang on 12.01.15.
 */
public class DbDataFlowTest {

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
