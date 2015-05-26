package de.uni_potsdam.hpi.bpt.bp2014.database;


import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import org.junit.Test;

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
        assertEquals(3, (int)dbDataNode.getDataObjectIdsForDataSets(11).get(0));
        assertEquals(4, (int)dbDataNode.getDataObjectIdsForDataSets(11).get(1));
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
