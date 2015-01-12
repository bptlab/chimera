package de.uni_potsdam.hpi.bpt.bp2014;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragment;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jaspar.mang on 12.01.15.
 */
public class DbFragmentTest {
    @Test
    public void testGetFragmentsForScenario(){
        DbFragment dbFragment = new DbFragment();
        assertEquals(1, (int)dbFragment.getFragmentsForScenario(1).get(0));
        assertEquals(2, (int)dbFragment.getFragmentsForScenario(1).get(1));
        assertEquals(3, (int)dbFragment.getFragmentsForScenario(1).get(2));
    }
}
