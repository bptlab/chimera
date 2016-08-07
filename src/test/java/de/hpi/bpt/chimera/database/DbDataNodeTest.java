package de.hpi.bpt.chimera.database;


import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.data.DbDataNode;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.junit.Assert.*;

public class DbDataNodeTest extends AbstractDatabaseDependentTest {


    private static DbDataNode dbDataNode;
    private static Connector connector;
    private static ExampleValueInserter inserter;

    private static final String DELETE1 =
            "DELETE FROM datanode;";
    private static final String DELETE2 =
            "DELETE FROM datasetconsistsofdatanode;";

    @BeforeClass
    public static void clearAndInitialize() {
        dbDataNode = new DbDataNode();
        connector = new Connector();
        inserter = new ExampleValueInserter();
        dbDataNode.executeUpdateStatement(DELETE1);
        dbDataNode.executeUpdateStatement(DELETE2);
    }

    @After
    public void clearDataNodes() {
        dbDataNode.executeUpdateStatement(DELETE1);
        dbDataNode.executeUpdateStatement(DELETE2);
    }

    @Test
    public void testGetDataClassIdsForDataSets(){
        insertTestData();

        List<Integer> dataClassIds = dbDataNode.getDataClassIdsForDataSets(1);
        assertEquals(2, dataClassIds.size());
        assertEquals(Arrays.asList(1, 2), dataClassIds);
    }


    @Test
    public void testGetDataStatesForDataSets(){
        insertTestData();

        assertEquals(new Integer(1), dbDataNode.getDataStatesForDataSet(1).get(0));
        assertEquals(new Integer(2), dbDataNode.getDataStatesForDataSet(1).get(1));
    }

    @Test
    public void testGetDataClassIdForDataNode() {
        int node1 = connector.insertDataNodeIntoDatabase(1, 1, 1);
        assertEquals(1, dbDataNode.getDataClassIdForDataNode(node1));
    }

    @Test
    public void testGetDataSetClassToStateMap() {
        insertTestData();
        Map<Integer, Integer> classIdToState = generateStateMap();
        assertEquals(classIdToState, dbDataNode.getDataSetClassToStateMap(1));
    }

    private void insertTestData() {
        int node1 = connector.insertDataNodeIntoDatabase(1, 1, 1);
        int node2 = connector.insertDataNodeIntoDatabase(1, 2, 2);
        connector.insertDataSetConsistOfDataNodeIntoDatabase(1, node1);
        connector.insertDataSetConsistOfDataNodeIntoDatabase(1, node2);
    }

    private Map<Integer, Integer> generateStateMap() {
        Map<Integer, Integer> classIdToState = new HashMap<>();
        classIdToState.put(1, 1);
        classIdToState.put(2, 2);
        return classIdToState;
    }
}
