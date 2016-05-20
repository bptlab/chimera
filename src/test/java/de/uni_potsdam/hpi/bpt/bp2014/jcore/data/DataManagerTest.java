package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.ExampleValueInserter;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DataManagerTest {

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testLoadDataObjects() {
        ExampleValueInserter inserter = new ExampleValueInserter();
        int firstDataclass = 1;
        int secondDataclass = 2;
        final int scenarioId = 1;
        int scenarioInstanceId = 1;
        int firstState = 1;
        int secondState = 2;
        inserter.insertDataObject(scenarioId, scenarioInstanceId, firstState, firstDataclass, false);
        inserter.insertDataObject(scenarioId, scenarioInstanceId, secondState, secondDataclass, false);

        ScenarioInstance scenarioInstance = EasyMock.createNiceMock(ScenarioInstance.class);
        expect(scenarioInstance.getScenarioId()).andReturn(scenarioId);
        expect(scenarioInstance.getScenarioInstanceId()).andReturn(scenarioInstanceId);
        replay(scenarioInstance);

        DataManager dataManager = new DataManager(scenarioInstance);
        assertEquals(2, dataManager.getDataObjects().size());
        assertEquals(1, dataManager.getDataObjects().get(0).getDataClassId());
        assertEquals(2, dataManager.getDataObjects().get(1).getDataClassId());
    }

    @Test
    public void testChangeDataObjectInstanceState() {
        Assert.fail();
    }

    @Test
    public void testGetDataobjectInstanceForId() {
        Assert.fail();
    }

    @Test
    public void testGetAllDataAttributeInstances() {
        Assert.fail();
    }

    @Test
    public void testGetDataObjectStates() {
        Assert.fail();
    }


    @Test
    public void testGetDataObjects() {
        Assert.fail();
    }

    @Test
    public void testLockDataobject() {
        Assert.fail();
    }
}