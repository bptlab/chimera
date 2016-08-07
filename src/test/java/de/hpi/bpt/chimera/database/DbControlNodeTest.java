package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNode;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;


public class DbControlNodeTest {

    private static final String START_EVENT = "StartEvent";
    private static final String ACTIVITY = "Activity";
    private static DbControlNode dbControlNode;
    private static Connector connector;

    private static int node1;
    private static int node2;

    @BeforeClass
    public static void clearAndInitialize() throws IOException, SQLException {
        dbControlNode = new DbControlNode();
        connector = new Connector();
        clearControlNodes();
        insertTestData();
    }

    @AfterClass
    public static void clearControlNodes() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    private static void insertTestData() {
        node1 = connector.insertControlNodeIntoDatabase("node1", START_EVENT, 1, "");
        node2 = connector.insertControlNodeIntoDatabase("node2", ACTIVITY, 1, "");
        connector.insertFragmentIntoDatabase("fragment", 1, 1);
    }

    @Test
    public void testGetStartEventId(){
        assertEquals(1, dbControlNode.getStartEventID(1));
    }

    @Test
    public void testGetFragmentId() {
        assertEquals(1, dbControlNode.getFragmentId(node1));
    }

    @Test
    public void testGetType(){
        assertEquals(START_EVENT, dbControlNode.getType(node1));
        assertEquals(ACTIVITY, dbControlNode.getType(node2));
    }

    @Test
    public void testGetLabel(){
        assertEquals("node1", dbControlNode.getLabel(node1));
        assertEquals("node2", dbControlNode.getLabel(node2));
    }

    @Test
    public void testExistControlNode() {
        assertTrue(dbControlNode.existControlNode(node1, 1));
        assertTrue(dbControlNode.existControlNode(node2, 1));
        assertFalse(dbControlNode.existControlNode(node1, 1337));
        assertFalse(dbControlNode.existControlNode(1337, 1));
    }
}
