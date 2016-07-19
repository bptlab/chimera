package de.uni_potsdam.hpi.bpt.bp2014.database.data;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.ConnectionWrapper;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.ExampleValueInserter;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class DbDataObjectTest {

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testSetState() {
        insertExampleObject();
        DbDataObject dataObject = new DbDataObject();
        dataObject.setState(1, 2);

        String sql = "SELECT * From dataobject WHERE id = 1;";
        int state = new DbObject().executeStatementReturnsInt(sql, "state_id");
        assertEquals(2, state);
    }

    @Test
    public void testCreateDataObject() {
        DbDataObject dataObject = new DbDataObject();
        dataObject.createDataObject(1, 1, 1, 1);
        // Since it is the first data object the id is 1
        String sql = "SELECT * From dataobject WHERE id = 1;";
        java.sql.Connection con = ConnectionWrapper.getInstance().connect();
        try (Statement stat = con.createStatement();
             ResultSet rs = stat.executeQuery(sql)) {
            if (rs.next()) {
                assertEquals(1, rs.getInt("scenario_id"));
                assertEquals(1, rs.getInt("scenarioinstance_id"));
                assertEquals(1, rs.getInt("dataclass_id"));
            } else {
                Assert.fail("Data object creation failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDataClassId() {
        insertExampleObject();
        DbDataObject dataObject = new DbDataObject();
        assertEquals(1, dataObject.getDataClassId(1));
    }

    @Test
    public void testGetStateId() {
        insertExampleObject();
        DbDataObject dataObject = new DbDataObject();
        assertEquals(1, dataObject.getStateId(1));
    }

    @Test
    public void testSetLocked() throws Exception {
        insertExampleObject();
        DbDataObject dataObject = new DbDataObject();
        dataObject.setLocked(1, false);

        String sql = "SELECT * From dataobject WHERE id = 1;";
        boolean islocked = new DbObject().executeStatementReturnsBoolean(sql, "state");
        assertFalse(islocked);
    }

    @Test
    public void testIsLocked() throws Exception {
        insertExampleObject();
        DbDataObject dataObject = new DbDataObject();
        assertTrue(dataObject.isLocked(1));
    }

    @Test
    public void testInitializeFromDb() {
        insertExampleObject();
        ExampleValueInserter inserter = new ExampleValueInserter();
        inserter.insertDataClass("1", false);
        DataObject dataObject = createDataObjectMock();
        DbDataObject dataObjectDao = new DbDataObject();
        dataObjectDao.loadFromDb(dataObject);
        verify(dataObject);
    }

    @Test
    public void testGetDataobjectIds() {
        insertExampleObject();
        insertExampleObject();
        DbDataObject dbDataObject = new DbDataObject();
        List<Integer> dataObjectIds = dbDataObject.getDataObjectIds(1);
        assertEquals(2, dataObjectIds.size());
        assertEquals(1, dataObjectIds.get(0).intValue());
        assertEquals(2, dataObjectIds.get(1).intValue());
    }

    private void insertExampleObject() {
        String insertDataobject = "INSERT INTO dataobject (scenario_id, scenarioinstance_id, state_id," +
                " dataclass_id, locked) VALUES (1, 1, 1, 1, 1);";
        DbObject dbObject = new DbObject();
        dbObject.executeInsertStatement(insertDataobject);
    }

    /*
    TODO think about moving this into MockProvider but maybe too specific?
     */
    private DataObject createDataObjectMock() {
        DataObject dataObject = EasyMock.createNiceMock(DataObject.class);
        expect(dataObject.getId()).andReturn(1);
        dataObject.setDataClassId(1);
        EasyMock.expectLastCall();
        dataObject.lock();
        EasyMock.expectLastCall();
        dataObject.setState(1);
        EasyMock.expectLastCall();
        replay(dataObject);
        return dataObject;
    }

}