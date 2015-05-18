package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 *
 */

import de.uni_potsdam.hpi.bpt.bp2014.database.DbGatewayInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractAcceptanceTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class DbGatewayInstanceTest extends AbstractAcceptanceTest {
    @Test
    public void testGetType(){
        DbGatewayInstance gatewayInstance = new DbGatewayInstance();
        assertEquals("AND", gatewayInstance.getType(100));
    }
    @Test
    public void testGetState(){
        DbGatewayInstance gatewayInstance = new DbGatewayInstance();
        assertEquals("init", gatewayInstance.getState(100));
    }
}
