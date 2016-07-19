package de.hpi.bpt.chimera.jcore.data;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.ExampleValueInserter;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        ScenarioInstance scenarioInstance = EasyMock.createNiceMock(ScenarioInstance.class);

        final int scenarioId = 1;
        final int scenarioInstanceId = 1;
        insertExampleValues(scenarioId, scenarioInstanceId);

        expect(scenarioInstance.getScenarioId()).andReturn(scenarioId);
        expect(scenarioInstance.getId()).andReturn(scenarioInstanceId);
        replay(scenarioInstance);

        DataManager dataManager = new DataManager(scenarioInstance);
        assertEquals(2, dataManager.getDataObjects().size());
        assertEquals(1, dataManager.getDataObjects().get(0).getDataClassId());
        assertEquals(2, dataManager.getDataObjects().get(1).getDataClassId());
    }

    @Test
    public void testLoadDataObjectsWithAttributes() {
        ScenarioInstance scenarioInstance = EasyMock.createNiceMock(ScenarioInstance.class);
        final int scenarioId = 1;
        final int scenarioInstanceId = 1;
        expect(scenarioInstance.getScenarioId()).andReturn(scenarioId);
        expect(scenarioInstance.getId()).andReturn(scenarioInstanceId);
        replay(scenarioInstance);

        insertExampleValues(scenarioId, scenarioInstanceId);
        DataManager dataManager = new DataManager(scenarioInstance);
        List<DataAttributeInstance> dataAttributeInstances = dataManager
                .getDataAttributeInstances();
        assertEquals(3, dataAttributeInstances.size());
        List<Integer> attributeInstanceIds = dataAttributeInstances.stream()
                .map(DataAttributeInstance::getId)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 2, 3),  attributeInstanceIds);

    }

    @Test
    public void testChangeDataObjectInstanceState() {
        ScenarioInstance scenarioInstance = EasyMock.createNiceMock(ScenarioInstance.class);
        final int scenarioId = 1;
        final int scenarioInstanceId = 1;
        expect(scenarioInstance.getScenarioId()).andReturn(scenarioId);
        expect(scenarioInstance.getId()).andReturn(scenarioInstanceId);
        replay(scenarioInstance);

        DataManager dataManager = new DataManager(scenarioInstance);
        int activityInstanceId = 1;
        int dataobjectId = 1;
        int desiredState = 2;

        DataObject first = EasyMock.createNiceMock(DataObject.class);
        expect(first.getId()).andReturn(dataobjectId);
        first.setState(desiredState);
        expectLastCall();
        replay(first);
        dataManager.setDataObjects(Arrays.asList(first));

        dataManager.changeDataObjectState(dataobjectId, desiredState, activityInstanceId);
        verify(first);
    }

    @Test
    public void testGetDataobjectInstanceForId() {
        ScenarioInstance scenarioInstance = EasyMock.createNiceMock(ScenarioInstance.class);
        DataManager dataManager = new DataManager(scenarioInstance);
        DataObject first = EasyMock.createNiceMock(DataObject.class);
        expect(first.getId()).andReturn(1);
        replay(first);

        DataObject second = EasyMock.createNiceMock(DataObject.class);
        expect(second.getId()).andReturn(2);
        replay(second);

        dataManager.setDataObjects(Arrays.asList(first, second));
        Optional<DataObject> dataObject = dataManager.getDataObjectForId(2);
        assertTrue(dataObject.isPresent());
        assertEquals(second, dataObject.get());

        Optional<DataObject> nonExistingDataobject = dataManager.getDataObjectForId(3);
        assertFalse(nonExistingDataobject.isPresent());
    }

    @Test
    public void testGetAllDataAttributeInstances() {
        DataAttributeInstance attr1 = EasyMock.createNiceMock(DataAttributeInstance.class);
        DataAttributeInstance attr2 = EasyMock.createNiceMock(DataAttributeInstance.class);
        DataAttributeInstance attr3 = EasyMock.createNiceMock(DataAttributeInstance.class);

        DataObject dataObject1 = EasyMock.createNiceMock(DataObject.class);
        expect(dataObject1.getDataAttributeInstances()).andReturn(Arrays.asList(attr1, attr2));

        DataObject dataObject2 = EasyMock.createNiceMock(DataObject.class);
        expect(dataObject2.getDataAttributeInstances()).andReturn(Arrays.asList(attr3));

        ScenarioInstance scenarioInstance = EasyMock.createNiceMock(ScenarioInstance.class);
        replay(attr1, attr2, attr3, dataObject1, dataObject2, scenarioInstance);
        DataManager dataManager = new DataManager(scenarioInstance);
        dataManager.setDataObjects(Arrays.asList(dataObject1, dataObject2));
        assertEquals(Arrays.asList(attr1, attr2, attr3),
                dataManager.getDataAttributeInstances());
    }

    @Test
    public void testLockDataobject() {
        ScenarioInstance scenarioInstance = EasyMock.createNiceMock(ScenarioInstance.class);
        expect(scenarioInstance.getScenarioId()).andReturn(1);
        expect(scenarioInstance.getId()).andReturn(1);
        replay(scenarioInstance);
        insertExampleValues(1, 1);
        DataManager dataManager = new DataManager(scenarioInstance);
        List<DataObject> dataObjects = dataManager.getDataObjects();
        assertEquals(2, dataObjects.stream().filter(x -> !x.isLocked()).count());
        dataManager.lockDataobject(dataObjects.get(0).getId());
        assertEquals(1, dataObjects.stream().filter(x -> !x.isLocked()).count());
    }

    private void insertExampleValues(int scenarioId, int scenarioInstanceId) {
        ExampleValueInserter inserter = new ExampleValueInserter();
        int firstDataclass = inserter.insertDataClass("Customer", false);
        int secondDataclass = inserter.insertDataClass("Contract", false);

        int firstState = 1;
        int secondState = 2;
        int firstDataObject = inserter.insertDataObject(
                scenarioId, scenarioInstanceId, firstState, firstDataclass, false);
        int secondDataObject = inserter.insertDataObject(
                scenarioId, scenarioInstanceId, secondState, secondDataclass, false);

        int attribute1 = inserter.insertDataAttribute("name", "String", "", firstDataclass);
        int attribute2 = inserter.insertDataAttribute("age", "Integer", "1", firstDataclass);
        int attribute3 = inserter.insertDataAttribute("lines", "Integer", "0", secondDataclass);


        inserter.insertDataAttributeInstance("Klaus", attribute1, firstDataObject);
        inserter.insertDataAttributeInstance("3", attribute2, firstDataObject);
        inserter.insertDataAttributeInstance("1000", attribute3, secondDataObject);
    }

}