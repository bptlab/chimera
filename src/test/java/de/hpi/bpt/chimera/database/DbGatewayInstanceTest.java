package de.hpi.bpt.chimera.database;

/**
 *
 */

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.controlnodes.DbGatewayInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class DbGatewayInstanceTest extends AbstractDatabaseDependentTest {
    @Ignore @Test
    public void testGetType(){
        DbGatewayInstance gatewayInstance = new DbGatewayInstance();
        assertEquals("AND", gatewayInstance.getType(100));
    }

    @Ignore @Test
    public void testGetState(){
        DbGatewayInstance gatewayInstance = new DbGatewayInstance();
        assertEquals(State.INIT, gatewayInstance.getState(100));
    }
}
