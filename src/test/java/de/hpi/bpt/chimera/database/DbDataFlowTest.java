package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.data.DbDataFlow;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DbDataFlowTest extends AbstractDatabaseDependentTest {
    /**
     *
     */
    @Ignore @Test
    public void testGetInputSetsForControlNode(){
        DbDataFlow dbDataFlow = new DbDataFlow();
        assertEquals(10, (int)dbDataFlow.getInputSetsForControlNode(12).get(0));
        assertEquals(11, (int)dbDataFlow.getInputSetsForControlNode(12).get(1));
    }

    /**
     *
     */
    @Ignore @Test
    public void testGetOutputSetsForControlNode(){
        DbDataFlow dbDataFlow = new DbDataFlow();
        assertEquals(12, (int)dbDataFlow.getOutputSetsForControlNode(13).get(0));
        assertEquals(13, (int)dbDataFlow.getOutputSetsForControlNode(13).get(1));
    }
}
