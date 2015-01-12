package de.uni_potsdam.hpi.bpt.bp2014;

import static org.junit.Assert.*;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import org.junit.Test;

/**
 * Created by jaspar.mang on 12.01.15.
 */
public class DbActivityInstanceTest {

    @Test
    public void testGetState(){
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        assertEquals("ready", dbActivityInstance.getState(77));
    }
    @Test
    public void testSetState() {
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        dbActivityInstance.setState(91, "terminated");
        assertEquals("terminated", dbActivityInstance.getState(91));
    }

    //TODO: testCreateNewActivityInstance
}
