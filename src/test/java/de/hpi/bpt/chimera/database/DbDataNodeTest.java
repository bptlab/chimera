package de.hpi.bpt.chimera.database;


import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.data.DbDataNode;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.junit.Assert.*;

public class DbDataNodeTest extends AbstractDatabaseDependentTest {


    private static DbDataNode dbDataNode;
    private static Connector connector;

    private static int node1;
    private static int node2;

    @BeforeClass
    public static void clearAndInitialize() throws IOException, SQLException {
        dbDataNode = new DbDataNode();
        connector = new Connector();
        clearDataNodes();
        insertTestData();
    }

    @AfterClass
    public static void clearDataNodes() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    private static void insertTestData() {
        node1 = connector.insertDataNode(1, 1, 1);
        node2 = connector.insertDataNode(1, 2, 2);
        connector.insertDataSetConsistOfDataNode(1, node1);
        connector.insertDataSetConsistOfDataNode(1, node2);
    }


    @Test
    public void testGetDataStatesForDataSets(){
        assertEquals(new Integer(1), dbDataNode.getDataStatesForDataSet(1).get(0));
        assertEquals(new Integer(2), dbDataNode.getDataStatesForDataSet(1).get(1));
    }

    @Test
    public void testGetDataClassIdForDataNode() {
        assertEquals(1, dbDataNode.getDataClassIdForDataNode(node1));
    }

    @Test
    public void testGetDataSetClassToStateMap() {
        insertTestData();
        Map<Integer, Integer> classIdToState = generateStateMap();
        assertEquals(classIdToState, dbDataNode.getDataSetClassToStateMap(1));
    }

    private Map<Integer, Integer> generateStateMap() {
        Map<Integer, Integer> classIdToState = new HashMap<>();
        classIdToState.put(1, 1);
        classIdToState.put(2, 2);
        return classIdToState;
    }
}
