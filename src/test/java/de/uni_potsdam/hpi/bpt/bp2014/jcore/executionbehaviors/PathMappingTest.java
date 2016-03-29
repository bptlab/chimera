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
public class PathMappingTest {

    private String path = "src/test/resources/json/jsonPathExample.json";

    @Test
    public void testWebserviceWriting() {
        PathMapping mapping = new PathMapping();
        List<DataAttributeInstance> attributeInstances = createExampleInstances();

        Map<Integer, String> jsonPathMap = generateJsonPathMap();

        File file = new File(path);

        try {
            String json = FileUtils.readFileToString(file);
            mapping.setAttributeIdToJsonPath(jsonPathMap);
            mapping.writeDataAttributesFromJson(json, attributeInstances);

            for (DataAttributeInstance instance : attributeInstances) {
                verify(instance);
            }
        } catch (IOException e) {
            fail("Could not load test resource:" + e.getMessage());
        }


    }

    private Map<Integer,String> generateJsonPathMap() {
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