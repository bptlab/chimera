package de.hpi.bpt.chimera.database;


import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.data.DbDataNode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DbDataNodeTest extends AbstractDatabaseDependentTest {

    @Test
    public void testGetDataObjectIdsForDataSets(){
        DbDataNode dbDataNode = new DbDataNode();
        List<Integer> dataClassIds = dbDataNode.getDataClassIdsForDataSets(11);
        assertEquals(2, dataClassIds.size());
        assertEquals(Arrays.asList(1, 2), dataClassIds);
    }

    @Test
    public void testGetDataStatesForDataSets(){
        DbDataNode dbDataNode = new DbDataNode();
        assertEquals(2, (int)dbDataNode.getDataStatesForDataSets(11).get(0));
        assertEquals(3, (int)dbDataNode.getDataStatesForDataSets(11).get(1));
    }
}
