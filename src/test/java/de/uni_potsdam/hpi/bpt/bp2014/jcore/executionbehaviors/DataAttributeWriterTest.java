package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
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
        DataAttributeWriter mapping = new DataAttributeWriter(
                dummyControlNodeId, dummyControlNodeInstanceId);
        List<DataAttributeInstance> attributeInstances = createExampleInstances();
        Map<Integer, String> jsonPathMap = createAttributeIdToJsonPathExample();

        File file = new File(path);

        try {
            String json = FileUtils.readFileToString(file);
            mapping.setAttributeIdToJsonPath(jsonPathMap);
            mapping.writeDataAttributesFromJson(json, attributeInstances);

            // Since $a[0].b.prop1 should evaluate to foo on the example, a setValue('foo') call
            // on the Dataattribute with id 67890 is expected.
            attributeInstances.forEach(EasyMock::verify);
        } catch (IOException e) {
            fail("Could not load test resource:" + e.getMessage());
        }
    }

    private Map<Integer,String> createAttributeIdToJsonPathExample() {
        Map<Integer, String> map = new HashMap<>();
        // retrieve "foo"
        map.put(67890, "$.a[0].b.prop1");
        return map;
    }

    private List<DataAttributeInstance> createExampleInstances() {
        DataAttributeInstance firstDataAttributeInstance = createNiceMock(DataAttributeInstance.class);
        expect(firstDataAttributeInstance.getDataAttributeInstanceId()).andReturn(12345);
        replay(firstDataAttributeInstance);

        DataAttributeInstance secondDataAttributeInstance = createNiceMock(DataAttributeInstance.class);
        expect(secondDataAttributeInstance.getDataAttributeInstanceId()).andReturn(67890);
        secondDataAttributeInstance.setValue("foo");
        EasyMock.expectLastCall().once();
        replay(secondDataAttributeInstance);

        List<DataAttributeInstance> attributeInstances = new ArrayList<>();
        attributeInstances.add(firstDataAttributeInstance);
        attributeInstances.add(secondDataAttributeInstance);
        return attributeInstances;
    }
}