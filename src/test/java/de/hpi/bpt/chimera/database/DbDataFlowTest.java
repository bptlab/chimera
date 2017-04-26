package de.hpi.bpt.chimera.database;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;


public class DbDataFlowTest extends AbstractDatabaseDependentTest {
    private final int INPUTSET_ID = 1;
    private final int OUTPUTSET_ID = 2;
    private final int CONTROLNODE_ID = 1;

    @Test
    public void testGetInputSetsForControlNode(){
        Connector connector = new Connector();
        connector.insertDataFlow(CONTROLNODE_ID, INPUTSET_ID, true);
        DbDataFlow dbDataFlow = new DbDataFlow();
        List<Integer> inputSets = dbDataFlow.getInputSetsForControlNode(CONTROLNODE_ID);
        assertEquals(1, inputSets.size());
        assertEquals(INPUTSET_ID, (long) inputSets.get(0));
    }

    @Test
    public void testGetOutputSetsForControlNode(){Connector connector = new Connector();
        connector.insertDataFlow(CONTROLNODE_ID, OUTPUTSET_ID, false);
        DbDataFlow dbDataFlow = new DbDataFlow();
        List<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(CONTROLNODE_ID);
        assertEquals(1, outputSets.size());
        assertEquals(OUTPUTSET_ID, (long) outputSets.get(0));
    }
}
