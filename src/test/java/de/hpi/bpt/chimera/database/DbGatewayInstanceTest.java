package de.hpi.bpt.chimera.database;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.controlnodes.DbGatewayInstance;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcore.controlnodes.GatewayType;
import de.hpi.bpt.chimera.jcore.controlnodes.State;

public class DbGatewayInstanceTest extends AbstractDatabaseDependentTest {

    private static DbGatewayInstance dbGatewayInstance;
    private static Connector connector;

    @BeforeClass
    public static void clearAndInitialize() throws IOException, SQLException {
        dbGatewayInstance = new DbGatewayInstance();
        connector = new Connector();
    }

    @Before
    public void insertTestData() {
        connector.insertGatewayInstance(1, GatewayType.AND.name(), State.INIT.name());
        connector.insertGatewayInstance(2, GatewayType.XOR.name(), State.INIT.name());
    }

    @Test
    public void testGetType() {
        assertEquals("AND", dbGatewayInstance.getType(1));
        assertEquals("XOR", dbGatewayInstance.getType(2));
    }

    @Test
    public void testGetState(){
        assertEquals(State.INIT, dbGatewayInstance.getState(1));
        assertEquals(State.INIT, dbGatewayInstance.getState(2));
    }

    @Test
    public void testSetState() {
        dbGatewayInstance.setState(1, State.EXECUTING.name());
        assertEquals(State.EXECUTING, dbGatewayInstance.getState(1));
        assertEquals(State.INIT, dbGatewayInstance.getState(2));
    }
}
