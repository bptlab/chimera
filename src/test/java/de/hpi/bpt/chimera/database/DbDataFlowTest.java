package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class DbDataFlowTest {
    private final int INPUTSET_ID = 1;
    private final int OUTPUTSET_ID = 2;
    private final int CONTROLNODE_ID = 1;


    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

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
