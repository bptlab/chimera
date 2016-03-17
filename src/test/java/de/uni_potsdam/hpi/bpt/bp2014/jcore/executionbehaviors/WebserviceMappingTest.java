package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.expectLastCall;

/**
 *
 */
public class WebserviceMappingTest {
    private String testJson = "     {\n" +
            "    \t\"a\": [{\n" +
            "    \t\t\"b\": {\n" +
            "    \t\t\t\"prop1\": \"foo\",\n" +
            "    \t\t\t\"prop2\": \"bar\"\n" +
            "    \t\t}\n" +
            "    \t}, {\n" +
            "    \t\t\"c\": {\n" +
            "    \t\t\t\"prop1\": \"bar\"\n" +
            "    \t\t}\n" +
            "    \t}],\n" +
            "    \t\"d\": 3\n" +
            "    }";

    @Test
    public void testWebserviceWriting() {
        WebserviceMapping mapping = new WebserviceMapping(0);
        List<DataAttributeInstance> attributeInstances = createExampleInstances();
        Map<String, String> jsonPathMap = new HashMap<>();
        mapping.setAttributeidToJsonPath(jsonPathMap);
        mapping.writeDataObjects(testJson, attributeInstances);

        for (DataAttributeInstance instance : attributeInstances) {
            verify(instance);
        }
    }

    private List<DataAttributeInstance> createExampleInstances() {
        DataAttributeInstance firstDataAttributeInstance = createNiceMock(DataAttributeInstance.class);
        expect(firstDataAttributeInstance.getEditorId()).andReturn("First");
        replay(firstDataAttributeInstance);

        DataAttributeInstance secondDataAttributeInstance = createNiceMock(DataAttributeInstance.class);
        expect(secondDataAttributeInstance.getEditorId()).andReturn("Second");
        secondDataAttributeInstance.setValue("foo");
        EasyMock.expectLastCall().once();
        replay(secondDataAttributeInstance);

        List<DataAttributeInstance> attributeInstances = new ArrayList<>();
        attributeInstances.add(firstDataAttributeInstance);
        attributeInstances.add(secondDataAttributeInstance);
        return attributeInstances;
    }
}