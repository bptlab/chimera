package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 *
 */
public class DbFragmentTest {

    private static DbFragment dbFragment;
    private static Connector connector;

    private static int fragment1;
    private static int fragment2;

    @BeforeClass
    public static void clearAndInitialize() throws IOException, SQLException {
        dbFragment = new DbFragment();
        connector = new Connector();
        clearFragments();
        insertTestData();
    }

    @AfterClass
    public static void clearFragments() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    private static void insertTestData() {
        fragment1 = connector.insertFragmentIntoDatabase("fragment1", 1, 1);
        fragment2 = connector.insertFragmentIntoDatabase("fragment2", 1, 1);
        connector.insertXmlIntoDatabase(fragment1, "<xml 1>");
        connector.insertXmlIntoDatabase(fragment2, "<xml 2>");
    }

    @Test
    public void testGetFragmentsForScenario(){
        assertEquals(new Integer(1), dbFragment.getFragmentsForScenario(1).get(0));
        assertEquals(new Integer(2), dbFragment.getFragmentsForScenario(1).get(1));
    }

    @Test
    public void testGetXmlStringsForScenario() {
        assertEquals("<xml 1>", dbFragment.getXmlStringsForScenario(1).get(0));
        assertEquals("<xml 2>", dbFragment.getXmlStringsForScenario(1).get(1));
    }
}
