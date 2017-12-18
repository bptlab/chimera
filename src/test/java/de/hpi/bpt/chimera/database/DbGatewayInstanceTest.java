package de.hpi.bpt.chimera.database;


import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.controlnodes.DbGatewayInstance;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcore.controlnodes.GatewayType;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class DbGatewayInstanceTest {

    private static DbGatewayInstance dbGatewayInstance;
    private static Connector connector;

    @BeforeClass
    public static void clearAndInitialize() throws IOException, SQLException {
        dbGatewayInstance = new DbGatewayInstance();
        connector = new Connector();
        clearGatewayInstances();
        insertTestData();
    }

    @AfterClass
    public static void clearGatewayInstances() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    private static void insertTestData() {
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
