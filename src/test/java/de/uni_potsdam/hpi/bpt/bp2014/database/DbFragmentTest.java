package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class DbFragmentTest extends AbstractDatabaseDependentTest {
    @Test
    public void testGetFragmentsForScenario(){
        DbFragment dbFragment = new DbFragment();
        assertEquals(1, (int)dbFragment.getFragmentsForScenario(1).get(0));
        assertEquals(2, (int)dbFragment.getFragmentsForScenario(1).get(1));
        assertEquals(3, (int)dbFragment.getFragmentsForScenario(1).get(2));
    }
}
