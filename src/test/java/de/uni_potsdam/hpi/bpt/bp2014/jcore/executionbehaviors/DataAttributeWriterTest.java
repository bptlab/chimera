package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

/**
 *
 */
public class DataAttributeWriterTest {

    private String path = "src/test/resources/json/jsonPathExample.json";

    @Test
    public void testWebserviceWriting() {
        int dummyControlNodeId = 0;
        int dummyControlNodeInstanceId = 0;
        ScenarioInstance instance = EasyMock.createNiceMock(ScenarioInstance.class);
        DataManager mockDM = createMockDM();
        expect(instance.getDataManager()).andReturn(mockDM).anyTimes();
        replay(instance);
        DataAttributeWriter writer = new DataAttributeWriter(
                dummyControlNodeId, dummyControlNodeInstanceId, instance);
        List<DataAttributeInstance> attributeInstances = createExampleInstances();
        Map<Integer, String> jsonPathMap = createAttributeIdToJsonPathExample();

        File file = new File(path);

        try {
            String json = FileUtils.readFileToString(file);
            writer.setAttributeIdToJsonPath(jsonPathMap);
            writer.writeDataAttributesFromJson(json, attributeInstances);
            EasyMock.verify(mockDM);

        } catch (IOException e) {
            fail("Could not load test resource:" + e.getMessage());
        }
    }

    private DataManager createMockDM() {
        DataManager manager = EasyMock.createNiceMock(DataManager.class);
        Map<Integer, String> expectedMap = new HashMap<>();
        expectedMap.put(678, "foo");
        expect(manager.setAttributeValues(0, expectedMap)).andReturn(true).anyTimes();
        replay(manager);
        return manager;
    }

    private Map<Integer,String> createAttributeIdToJsonPathExample() {
        Map<Integer, String> map = new HashMap<>();
        // retrieve "foo"
        map.put(678, "$.a[0].b.prop1");
        return map;
    }

    private List<DataAttributeInstance> createExampleInstances() {
        DataAttributeInstance firstDataAttributeInstance = createNiceMock(DataAttributeInstance.class);
        expect(firstDataAttributeInstance.getId()).andReturn(123);
        // expect(firstDataAttributeInstance.getType()).andReturn("String");
        expect(firstDataAttributeInstance.getDataAttributeId()).andReturn(677);
        replay(firstDataAttributeInstance);

        DataAttributeInstance secondDataAttributeInstance = createNiceMock(DataAttributeInstance.class);
        expect(secondDataAttributeInstance.getId()).andReturn(124);
        expect(secondDataAttributeInstance.isValueAllowed("foo")).andReturn(true);
        expect(secondDataAttributeInstance.getDataAttributeId()).andReturn(678);
        secondDataAttributeInstance.setValue("foo");
        EasyMock.expectLastCall().once();
        replay(secondDataAttributeInstance);

        List<DataAttributeInstance> attributeInstances = new ArrayList<>();
        attributeInstances.add(firstDataAttributeInstance);
        attributeInstances.add(secondDataAttributeInstance);
        return attributeInstances;
    }
}