package de.uni_potsdam.hpi.bpt.bp2014.database;


import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataNode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class DbDataNodeTest extends AbstractDatabaseDependentTest {
    /**
     *
     */
    @Test
    public void testGetDataObjectIdsForDataSets(){
        DbDataNode dbDataNode = new DbDataNode();
        List<Integer> dataclassIds = dbDataNode.getDataClassIdsForDataSets(11);
        assertEquals(2, dataclassIds.size());
        assertEquals(Arrays.asList(1, 2), dataclassIds);
    }

    /**
     *
     */
    @Test
    public void testGetDataStatesForDataSets(){
        DbDataNode dbDataNode = new DbDataNode();
        assertEquals(2, (int)dbDataNode.getDataStatesForDataSets(11).get(0));
        assertEquals(3, (int)dbDataNode.getDataStatesForDataSets(11).get(1));
    }
}
