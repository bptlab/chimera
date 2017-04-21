package de.hpi.bpt.chimera.database;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;

/**
 *
 */
public class DbFragmentTest extends AbstractDatabaseDependentTest {

    private static DbFragment dbFragment;
    private static Connector connector;

    @BeforeClass
    public static void clearAndInitialize() throws IOException, SQLException {
        dbFragment = new DbFragment();
        connector = new Connector();
    }

    @Before
    public void insertTestData() {
        int fragment1 = connector.insertFragment("fragment1", 1, 1);
        int fragment2 = connector.insertFragment("fragment2", 1, 1);
        connector.insertFragmentXml(fragment1, "<xml 1>");
        connector.insertFragmentXml(fragment2, "<xml 2>");
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
