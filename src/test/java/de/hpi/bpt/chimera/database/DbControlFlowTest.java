package de.hpi.bpt.chimera.database;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;


public class DbControlFlowTest extends AbstractDatabaseDependentTest {
    private final int STARTEVENT_ID = 1;
    private final int FIRST_FOLLOWING = 2;
    private final int SECOND_FOLLOWING = 3;

    @Test
    public void testGetFollowingControlNodes(){
        Connector connector = new Connector();
        connector.insertControlFlow(STARTEVENT_ID, FIRST_FOLLOWING, "empty");
        connector.insertControlFlow(STARTEVENT_ID, SECOND_FOLLOWING, "empty");
        DbControlFlow dbControlFlow = new DbControlFlow();
        List<Integer> nodesAfterStartEvent = dbControlFlow.getFollowingControlNodes(STARTEVENT_ID);
        assertEquals(nodesAfterStartEvent, Arrays.asList(FIRST_FOLLOWING, SECOND_FOLLOWING));
   }

    @Test
    public void testGetPrecedingControlNodes(){
        Connector connector = new Connector();
        connector.insertControlFlow(STARTEVENT_ID, FIRST_FOLLOWING, "empty");
        connector.insertControlFlow(STARTEVENT_ID, SECOND_FOLLOWING, "empty");
        DbControlFlow dbControlFlow = new DbControlFlow();
        List<Integer> nodesAfterStartEvent = dbControlFlow.getPredecessorControlNodes(
                FIRST_FOLLOWING);
        assertEquals(nodesAfterStartEvent, Arrays.asList(STARTEVENT_ID));
    }

}

