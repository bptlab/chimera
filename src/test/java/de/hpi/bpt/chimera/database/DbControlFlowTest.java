package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class DbControlFlowTest {
    private final int STARTEVENT_ID = 1;
    private final int FIRST_FOLLOWING = 2;
    private final int SECOND_FOLLOWING = 3;

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testGetFollowingControlNodes(){
        Connector connector = new Connector();
        connector.insertControlFlowIntoDatabase(STARTEVENT_ID, FIRST_FOLLOWING, "empty");
        connector.insertControlFlowIntoDatabase(STARTEVENT_ID, SECOND_FOLLOWING, "empty");
        DbControlFlow dbControlFlow = new DbControlFlow();
        List<Integer> nodesAfterStartEvent = dbControlFlow.getFollowingControlNodes(STARTEVENT_ID);
        assertEquals(nodesAfterStartEvent, Arrays.asList(FIRST_FOLLOWING, SECOND_FOLLOWING));
   }

    @Test
    public void testGetPrecedingControlNodes(){
        Connector connector = new Connector();
        connector.insertControlFlowIntoDatabase(STARTEVENT_ID, FIRST_FOLLOWING, "empty");
        connector.insertControlFlowIntoDatabase(STARTEVENT_ID, SECOND_FOLLOWING, "empty");
        DbControlFlow dbControlFlow = new DbControlFlow();
        List<Integer> nodesAfterStartEvent = dbControlFlow.getPredecessorControlNodes(
                FIRST_FOLLOWING);
        assertEquals(nodesAfterStartEvent, Arrays.asList(STARTEVENT_ID));
    }

}

